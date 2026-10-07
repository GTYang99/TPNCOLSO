package com.example.tp_ncolso_android.feature.surveyform

data class SurveyFormValidationResult(
    val fieldErrors: Map<SurveyFormField, String> = emptyMap(),
    val photoError: String? = null,
) {
    val isValid: Boolean
        get() = fieldErrors.isEmpty() && photoError == null
}

fun validateSurveyForm(
    values: SurveyFormValues,
    photos: List<SurveyFormPhoto>,
): SurveyFormValidationResult {
    val errors = buildMap {
        if (values.landLocation.isBlank()) put(SurveyFormField.LAND_LOCATION, "請輸入土地坐落")
        if (values.siteConditions.isEmpty()) put(SurveyFormField.SITE_CONDITION, "請至少選擇一項現場勘查土地情形")

        if (!values.siteConditions.contains("無占用")) {
            if (values.occupyForm.isBlank()) put(SurveyFormField.OCCUPY_FORM, "請選擇占用型態")
            val householdCount = values.householdCount.toIntOrNull()
            if (householdCount == null || householdCount < 1) {
                put(SurveyFormField.HOUSEHOLD_COUNT, "占用戶數需為 1 以上整數")
            }
            if (values.occupyAddress.isBlank()) put(SurveyFormField.OCCUPY_ADDRESS, "請輸入占用門牌號")
        }
    }

    val photoError = when {
        photos.size != 4 -> "現況照片需正好 4 張"
        photos.any { it.aspect != SurveyPhotoAspect.FOUR_BY_THREE } -> "現況照片必須為 4:3"
        else -> null
    }
    return SurveyFormValidationResult(errors, photoError)
}
