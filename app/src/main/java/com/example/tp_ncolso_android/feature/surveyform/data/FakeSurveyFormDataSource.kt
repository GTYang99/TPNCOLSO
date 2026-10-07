package com.example.tp_ncolso_android.feature.surveyform.data

import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormSeed
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormValues

class FakeSurveyFormDataSource(
    private val submitResult: LocalSurveySubmitResult = LocalSurveySubmitResult.Success,
) : SurveyFormDataSource {
    var submitCount: Int = 0
        private set

    override fun load(keyNo: String): SurveyFormSeed = when (keyNo) {
        "TEST-KEY-002" -> SurveyFormSeed(
            status = "退回",
            values = SurveyFormValues(
                surveyDate = "民國 115 年 9 月 28 日",
                surveyUser = "測試調查人員",
                batchNo = "B-001",
                landLocation = "測試區示例路 2 號",
                siteConditions = listOf("建物"),
                occupyForm = "固定式",
                householdCount = "2",
                occupyAddress = "測試區示例路 2 號",
                note = "補拍照片",
            ),
            photos = listOf(
                fakePhoto("returned-1", "民國 115 年 9 月 28 日 10:12"),
                fakePhoto("returned-2", "民國 115 年 9 月 28 日 10:14"),
                fakePhoto("returned-3", "民國 115 年 9 月 28 日 10:16"),
            ),
        )
        else -> SurveyFormSeed(
            status = "未調查",
            values = SurveyFormValues(
                surveyDate = "民國 115 年 10 月 7 日",
                surveyUser = "測試人員",
                batchNo = "B-001",
                landLocation = "測試區示例路 1 號",
                siteConditions = listOf("無占用"),
            ),
        )
    }

    override fun submit(keyNo: String, values: SurveyFormValues, photos: List<SurveyFormPhoto>): LocalSurveySubmitResult {
        submitCount += 1
        return submitResult
    }

    private fun fakePhoto(id: String, capturedAt: String) = SurveyFormPhoto(
        id = id,
        uri = "local://survey/$id",
        capturedAt = capturedAt,
    )
}
