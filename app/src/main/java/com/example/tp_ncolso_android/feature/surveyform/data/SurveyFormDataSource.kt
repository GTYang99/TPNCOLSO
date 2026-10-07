package com.example.tp_ncolso_android.feature.surveyform.data

import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormSeed
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormValues

sealed interface LocalSurveySubmitResult {
    data object Success : LocalSurveySubmitResult
    data class Failure(val message: String) : LocalSurveySubmitResult
}

interface SurveyFormDataSource {
    fun load(keyNo: String): SurveyFormSeed?
    fun submit(keyNo: String, values: SurveyFormValues, photos: List<SurveyFormPhoto>): LocalSurveySubmitResult
}
