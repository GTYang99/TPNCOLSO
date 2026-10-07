package com.example.tp_ncolso_android.feature.surveyform

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SurveyFormValidationTest {
    @Test
    fun noOccupancyWithFourThreePhotosIsValid() {
        val result = validateSurveyForm(
            values = validValues(siteConditions = listOf("無占用")),
            photos = fourPhotos(),
        )

        assertTrue(result.isValid)
    }

    @Test
    fun occupiedFormRequiresPositiveIntegerAndAddress() {
        val result = validateSurveyForm(
            values = validValues(
                siteConditions = listOf("建物"),
                occupyForm = "",
                householdCount = "1.5",
                occupyAddress = "",
            ),
            photos = fourPhotos(),
        )

        assertFalse(result.isValid)
        assertEquals("請選擇占用型態", result.fieldErrors[SurveyFormField.OCCUPY_FORM])
        assertEquals("占用戶數需為 1 以上整數", result.fieldErrors[SurveyFormField.HOUSEHOLD_COUNT])
        assertEquals("請輸入占用門牌號", result.fieldErrors[SurveyFormField.OCCUPY_ADDRESS])
    }

    @Test
    fun photoCountAndAspectAreValidatedTogether() {
        val result = validateSurveyForm(validValues(), listOf(SurveyFormPhoto("bad", "local://bad", "now", SurveyPhotoAspect.OTHER)))

        assertFalse(result.isValid)
        assertEquals("現況照片需正好 4 張", result.photoError)
    }

    private fun validValues(
        siteConditions: List<String> = listOf("無占用"),
        occupyForm: String = "",
        householdCount: String = "",
        occupyAddress: String = "",
    ) = SurveyFormValues(
        surveyDate = "民國 115 年 10 月 7 日",
        surveyUser = "測試人員",
        batchNo = "B-001",
        landLocation = "測試路 1 號",
        siteConditions = siteConditions,
        occupyForm = occupyForm,
        householdCount = householdCount,
        occupyAddress = occupyAddress,
    )

    private fun fourPhotos() = (1..4).map { SurveyFormPhoto("$it", "local://$it", "now") }
}
