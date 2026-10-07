package com.example.tp_ncolso_android.feature.surveyform

import androidx.lifecycle.ViewModel
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
) : ViewModel() {
    private val seed = dataSource.load(keyNo) ?: SurveyFormSeed(
        status = "未調查",
        values = SurveyFormValues("", "", ""),
    )
    private val originalValues = seed.values
    private val originalPhotos = seed.photos

    private val mutableState = MutableStateFlow(
        SurveyFormUiState(
            keyNo = keyNo,
            status = seed.status,
            values = seed.values,
            photos = seed.photos,
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
            LocalSurveySubmitResult.Success -> mutableState.value = mutableState.value.copy(
                dirty = false,
                submitState = SurveyFormSubmitState.SUCCESS,
            )
            is LocalSurveySubmitResult.Failure -> mutableState.value = mutableState.value.copy(
                submitState = SurveyFormSubmitState.IDLE,
                errorMessage = result.message,
                dirty = true,
            )
        }
    }
}
