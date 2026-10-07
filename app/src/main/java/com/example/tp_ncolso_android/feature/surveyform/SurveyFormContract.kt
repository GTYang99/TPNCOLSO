package com.example.tp_ncolso_android.feature.surveyform

import androidx.compose.runtime.Immutable

enum class SurveyFormField {
    LAND_LOCATION,
    SITE_CONDITION,
    OCCUPY_FORM,
    HOUSEHOLD_COUNT,
    OCCUPY_ADDRESS,
    NOTE,
}

enum class SurveyPhotoAspect {
    FOUR_BY_THREE,
    OTHER,
}

@Immutable
data class SurveyFormPhoto(
    val id: String,
    val uri: String,
    val capturedAt: String,
    val aspect: SurveyPhotoAspect = SurveyPhotoAspect.FOUR_BY_THREE,
)

@Immutable
data class SurveyFormValues(
    val surveyDate: String,
    val surveyUser: String,
    val batchNo: String,
    val landLocation: String = "",
    val siteConditions: List<String> = emptyList(),
    val occupyForm: String = "",
    val householdCount: String = "",
    val occupyAddress: String = "",
    val note: String = "",
)

@Immutable
data class SurveyFormSeed(
    val status: String,
    val values: SurveyFormValues,
    val photos: List<SurveyFormPhoto> = emptyList(),
)

enum class SurveyFormSubmitState {
    IDLE,
    SUBMITTING,
    SUCCESS,
}

@Immutable
data class SurveyFormUiState(
    val keyNo: String,
    val status: String = "未調查",
    val values: SurveyFormValues = SurveyFormValues("", "", ""),
    val photos: List<SurveyFormPhoto> = emptyList(),
    val dirty: Boolean = false,
    val submitState: SurveyFormSubmitState = SurveyFormSubmitState.IDLE,
    val errorMessage: String? = null,
    val fieldErrors: Map<SurveyFormField, String> = emptyMap(),
    val photoError: String? = null,
)

sealed interface SurveyFormEvent {
    data class ChangeField(val field: SurveyFormField, val value: String) : SurveyFormEvent
    data class ToggleSiteCondition(val value: String) : SurveyFormEvent
    data class CapturePhoto(val photo: SurveyFormPhoto) : SurveyFormEvent
    data class DeletePhoto(val photoId: String) : SurveyFormEvent
    data object RequestCapture : SurveyFormEvent
    data object Submit : SurveyFormEvent
    data object CloseRequested : SurveyFormEvent
}

sealed interface SurveyFormEffect {
    data object CaptureRequested : SurveyFormEffect
    data class CloseRequested(val keyNo: String, val dirty: Boolean) : SurveyFormEffect
}

data class SurveyFormCallbacks(
    val onCloseRequested: (keyNo: String, dirty: Boolean) -> Unit,
    val onCaptureRequested: () -> Unit = {},
)

val SurveyConditionOptions = listOf(
    "無占用",
    "建物",
    "雨遮",
    "斜坡道",
    "落柱",
    "雜物",
    "招牌",
    "盆栽",
    "攤販",
    "鐵皮屋",
    "工地",
)

val OccupyFormOptions = listOf("固定式", "活動式", "固定式與活動式")
