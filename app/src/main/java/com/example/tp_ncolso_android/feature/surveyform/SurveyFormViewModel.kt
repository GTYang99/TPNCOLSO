package com.example.tp_ncolso_android.feature.surveyform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.tp_ncolso_android.feature.surveyform.data.LocalSurveySubmitResult
import com.example.tp_ncolso_android.feature.surveyform.data.SurveyFormDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class SurveyFormViewModel(
    private val keyNo: String,
    private val dataSource: SurveyFormDataSource,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {
    private val seed = dataSource.load(keyNo) ?: SurveyFormSeed(
        status = "未調查",
        values = SurveyFormValues("", "", ""),
    )
    private val originalValues = seed.values
    private val originalPhotos = seed.photos

    private val restored = restoreDraft(seed)
    private val mutableState = MutableStateFlow(
        SurveyFormUiState(
            keyNo = keyNo,
            status = seed.status,
            returnContext = seed.returnContext,
            values = restored.first,
            photos = restored.second,
            dirty = restored.first != originalValues || restored.second != originalPhotos,
            photoError = restored.third,
        ),
    )
    val state: StateFlow<SurveyFormUiState> = mutableState.asStateFlow()

    private val effects = Channel<SurveyFormEffect>(capacity = Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    fun onEvent(event: SurveyFormEvent) {
        when (event) {
            is SurveyFormEvent.ChangeField -> changeField(event.field, event.value)
            is SurveyFormEvent.ToggleSiteCondition -> toggleSiteCondition(event.value)
            is SurveyFormEvent.CapturePhoto -> capturePhoto(event.photo)
            is SurveyFormEvent.DeletePhoto -> deletePhoto(event.photoId)
            SurveyFormEvent.RequestCapture -> if (mutableState.value.photos.size < 4) effects.trySend(SurveyFormEffect.CaptureRequested)
            SurveyFormEvent.Submit -> submit()
            SurveyFormEvent.CloseRequested -> effects.trySend(
                SurveyFormEffect.CloseRequested(keyNo, mutableState.value.dirty),
            )
        }
    }

    private fun changeField(field: SurveyFormField, value: String) {
        val current = mutableState.value
        val updated = when (field) {
            SurveyFormField.LAND_LOCATION -> current.values.copy(landLocation = value)
            SurveyFormField.OCCUPY_FORM -> current.values.copy(occupyForm = value)
            SurveyFormField.HOUSEHOLD_COUNT -> current.values.copy(householdCount = value)
            SurveyFormField.OCCUPY_ADDRESS -> current.values.copy(occupyAddress = value)
            SurveyFormField.SITE_CONDITION -> current.values.copy(siteConditions = listOf(value))
            SurveyFormField.NOTE -> current.values.copy(note = value)
        }
        updateValues(updated)
    }

    private fun toggleSiteCondition(value: String) {
        val current = mutableState.value.values.siteConditions
        val updatedConditions = if (value == "無占用") {
            if (current.contains(value)) emptyList() else listOf(value)
        } else {
            current.filterNot { it == "無占用" }.let { conditions ->
                if (conditions.contains(value)) conditions - value else conditions + value
            }
        }
        val updatedValues = mutableState.value.values.copy(
            siteConditions = updatedConditions,
            occupyForm = if (updatedConditions.contains("無占用")) "" else mutableState.value.values.occupyForm,
            householdCount = if (updatedConditions.contains("無占用")) "" else mutableState.value.values.householdCount,
            occupyAddress = if (updatedConditions.contains("無占用")) "" else mutableState.value.values.occupyAddress,
        )
        updateValues(updatedValues)
    }

    private fun updateValues(values: SurveyFormValues) {
        val current = mutableState.value
        mutableState.value = current.copy(
            values = values,
            dirty = values != originalValues || current.photos != originalPhotos,
            submitState = if (current.submitState == SurveyFormSubmitState.SUCCESS) SurveyFormSubmitState.IDLE else current.submitState,
            errorMessage = null,
            fieldErrors = changedField(current.values, values)?.let { current.fieldErrors - it } ?: current.fieldErrors,
            photoError = null,
        )
        persistDraft(mutableState.value)
    }

    private fun changedField(previous: SurveyFormValues, updated: SurveyFormValues): SurveyFormField? = when {
        previous.landLocation != updated.landLocation -> SurveyFormField.LAND_LOCATION
        previous.siteConditions != updated.siteConditions -> SurveyFormField.SITE_CONDITION
        previous.occupyForm != updated.occupyForm -> SurveyFormField.OCCUPY_FORM
        previous.householdCount != updated.householdCount -> SurveyFormField.HOUSEHOLD_COUNT
        previous.occupyAddress != updated.occupyAddress -> SurveyFormField.OCCUPY_ADDRESS
        previous.note != updated.note -> SurveyFormField.NOTE
        else -> null
    }

    private fun capturePhoto(photo: SurveyFormPhoto) {
        val current = mutableState.value
        if (current.photos.size >= 4 || current.photos.any { it.id == photo.id }) return
        mutableState.value = current.copy(
            photos = current.photos + photo,
            dirty = current.values != originalValues || current.photos + photo != originalPhotos,
            submitState = if (current.submitState == SurveyFormSubmitState.SUCCESS) SurveyFormSubmitState.IDLE else current.submitState,
            errorMessage = null,
            photoError = null,
        )
        persistDraft(mutableState.value)
    }

    private fun deletePhoto(photoId: String) {
        val current = mutableState.value
        val updatedPhotos = current.photos.filterNot { it.id == photoId }
        if (updatedPhotos.size == current.photos.size) return
        mutableState.value = current.copy(
            photos = updatedPhotos,
            dirty = current.values != originalValues || updatedPhotos != originalPhotos,
            submitState = if (current.submitState == SurveyFormSubmitState.SUCCESS) SurveyFormSubmitState.IDLE else current.submitState,
            errorMessage = null,
            photoError = null,
        )
        persistDraft(mutableState.value)
    }

    private fun submit() {
        val current = mutableState.value
        if (current.submitState == SurveyFormSubmitState.SUBMITTING) return
        val validation = validateSurveyForm(current.values, current.photos)
        if (!validation.isValid) {
            mutableState.value = current.copy(
                fieldErrors = validation.fieldErrors,
                photoError = validation.photoError,
                errorMessage = null,
            )
            return
        }

        mutableState.value = current.copy(
            submitState = SurveyFormSubmitState.SUBMITTING,
            fieldErrors = emptyMap(),
            photoError = null,
            errorMessage = null,
        )
        when (val result = dataSource.submit(keyNo, current.values, current.photos)) {
            LocalSurveySubmitResult.Success -> {
                mutableState.value = mutableState.value.copy(
                    dirty = false,
                    submitState = SurveyFormSubmitState.SUCCESS,
                )
                if (seed.returnContext != null) effects.trySend(SurveyFormEffect.CorrectionSaved(keyNo))
            }
            is LocalSurveySubmitResult.Failure -> mutableState.value = mutableState.value.copy(
                submitState = SurveyFormSubmitState.IDLE,
                errorMessage = result.message,
                dirty = true,
            )
        }
        persistDraft(mutableState.value)
    }

    private fun restoreDraft(seed: SurveyFormSeed): Triple<SurveyFormValues, List<SurveyFormPhoto>, String?> {
        if (savedStateHandle.get<Boolean>(KEY_HAS_DRAFT) != true) return Triple(seed.values, seed.photos, null)
        val values = seed.values.copy(
            landLocation = savedStateHandle.get<String>(KEY_LAND_LOCATION).orEmpty(),
            siteConditions = savedStateHandle.get<ArrayList<String>>(KEY_SITE_CONDITIONS)?.toList().orEmpty(),
            occupyForm = savedStateHandle.get<String>(KEY_OCCUPY_FORM).orEmpty(),
            householdCount = savedStateHandle.get<String>(KEY_HOUSEHOLD_COUNT).orEmpty(),
            occupyAddress = savedStateHandle.get<String>(KEY_OCCUPY_ADDRESS).orEmpty(),
            note = savedStateHandle.get<String>(KEY_NOTE).orEmpty(),
        )
        val ids = savedStateHandle.get<ArrayList<String>>(KEY_PHOTO_IDS).orEmpty()
        val uris = savedStateHandle.get<ArrayList<String>>(KEY_PHOTO_URIS).orEmpty()
        val dates = savedStateHandle.get<ArrayList<String>>(KEY_PHOTO_DATES).orEmpty()
        val aspects = savedStateHandle.get<ArrayList<String>>(KEY_PHOTO_ASPECTS).orEmpty()
        val photosValid = ids.size <= MAX_PHOTOS && ids.size == uris.size && ids.size == dates.size && ids.size == aspects.size
        val photos = if (photosValid) ids.indices.mapNotNull { index ->
            val aspect = runCatching { SurveyPhotoAspect.valueOf(aspects[index]) }.getOrNull()
            aspect?.let { SurveyFormPhoto(ids[index], uris[index], dates[index], it) }
        } else emptyList()
        val malformed = !photosValid || (photos.size != ids.size)
        return Triple(values, if (malformed) seed.photos else photos, if (malformed) PHOTO_RESTORE_ERROR else null)
    }

    private fun persistDraft(state: SurveyFormUiState) {
        savedStateHandle[KEY_HAS_DRAFT] = true
        savedStateHandle[KEY_LAND_LOCATION] = state.values.landLocation
        savedStateHandle[KEY_SITE_CONDITIONS] = ArrayList(state.values.siteConditions)
        savedStateHandle[KEY_OCCUPY_FORM] = state.values.occupyForm
        savedStateHandle[KEY_HOUSEHOLD_COUNT] = state.values.householdCount
        savedStateHandle[KEY_OCCUPY_ADDRESS] = state.values.occupyAddress
        savedStateHandle[KEY_NOTE] = state.values.note
        savedStateHandle[KEY_PHOTO_IDS] = ArrayList(state.photos.map { it.id })
        savedStateHandle[KEY_PHOTO_URIS] = ArrayList(state.photos.map { it.uri })
        savedStateHandle[KEY_PHOTO_DATES] = ArrayList(state.photos.map { it.capturedAt })
        savedStateHandle[KEY_PHOTO_ASPECTS] = ArrayList(state.photos.map { it.aspect.name })
    }

    private companion object {
        const val MAX_PHOTOS = 4
        const val PHOTO_RESTORE_ERROR = "照片資料無法完整還原，請重新檢查照片"
        const val KEY_HAS_DRAFT = "survey.has-draft"
        const val KEY_LAND_LOCATION = "survey.land-location"
        const val KEY_SITE_CONDITIONS = "survey.site-conditions"
        const val KEY_OCCUPY_FORM = "survey.occupy-form"
        const val KEY_HOUSEHOLD_COUNT = "survey.household-count"
        const val KEY_OCCUPY_ADDRESS = "survey.occupy-address"
        const val KEY_NOTE = "survey.note"
        const val KEY_PHOTO_IDS = "survey.photo-ids"
        const val KEY_PHOTO_URIS = "survey.photo-uris"
        const val KEY_PHOTO_DATES = "survey.photo-dates"
        const val KEY_PHOTO_ASPECTS = "survey.photo-aspects"
    }
}
