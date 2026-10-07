package com.example.tp_ncolso_android.feature.surveyform

import com.example.tp_ncolso_android.feature.surveyform.data.FakeSurveyFormDataSource
import com.example.tp_ncolso_android.feature.surveyform.data.LocalSurveySubmitResult
import com.example.tp_ncolso_android.feature.surveyform.data.SurveyFormDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SurveyFormViewModelTest {
    @Test
    fun selectingNoOccupancyClearsAndHidesOccupationValues() {
        val source = FakeSurveyFormDataSource()
        val viewModel = SurveyFormViewModel("TEST-KEY-002", source)

        viewModel.onEvent(SurveyFormEvent.ToggleSiteCondition("無占用"))

        val state = viewModel.state.value
        assertEquals(listOf("無占用"), state.values.siteConditions)
        assertEquals("", state.values.occupyForm)
        assertEquals("", state.values.householdCount)
        assertEquals("", state.values.occupyAddress)
    }

    @Test
    fun photoCaptureDeleteAndCloseExposeDirtyState() = runBlocking {
        val viewModel = SurveyFormViewModel("TEST-KEY-001", FakeSurveyFormDataSource())
        val photo = SurveyFormPhoto("new", "local://new", "now")

        viewModel.onEvent(SurveyFormEvent.CapturePhoto(photo))
        assertTrue(viewModel.state.value.dirty)
        viewModel.onEvent(SurveyFormEvent.DeletePhoto(photo.id))
        assertFalse(viewModel.state.value.dirty)

        viewModel.onEvent(SurveyFormEvent.ChangeField(SurveyFormField.NOTE, "現場備註"))
        viewModel.onEvent(SurveyFormEvent.CloseRequested)
        assertEquals(
            SurveyFormEffect.CloseRequested("TEST-KEY-001", true),
            viewModel.effect.first(),
        )
    }

    @Test
    fun successfulSubmitClearsDirtyAndReentrantSubmitIsIgnored() {
        lateinit var viewModel: SurveyFormViewModel
        var submitCalls = 0
        val source = object : SurveyFormDataSource {
            override fun load(keyNo: String) = FakeSurveyFormDataSource().load(keyNo).copy(
                values = FakeSurveyFormDataSource().load(keyNo).values.copy(
                    siteConditions = listOf("無占用"),
                ),
                photos = (1..4).map { SurveyFormPhoto("$it", "local://$it", "now") },
            )

            override fun submit(keyNo: String, values: SurveyFormValues, photos: List<SurveyFormPhoto>): LocalSurveySubmitResult {
                submitCalls += 1
                viewModel.onEvent(SurveyFormEvent.Submit)
                return LocalSurveySubmitResult.Success
            }
        }
        viewModel = SurveyFormViewModel("TEST-KEY-001", source)

        viewModel.onEvent(SurveyFormEvent.Submit)

        assertEquals(1, submitCalls)
        assertEquals(SurveyFormSubmitState.SUCCESS, viewModel.state.value.submitState)
        assertFalse(viewModel.state.value.dirty)
    }

    @Test
    fun failedSubmitPreservesValuesAndPhotos() {
        val source = FakeSurveyFormDataSource(LocalSurveySubmitResult.Failure("本機送出失敗，請稍後重試"))
        val viewModel = SurveyFormViewModel("TEST-KEY-001", source)
        val values = viewModel.state.value.values.copy(
            siteConditions = listOf("無占用"),
        )
        viewModel.onEvent(SurveyFormEvent.ChangeField(SurveyFormField.NOTE, "保留內容"))
        (1..4).forEach { viewModel.onEvent(SurveyFormEvent.CapturePhoto(SurveyFormPhoto("$it", "local://$it", "now"))) }
        viewModel.onEvent(SurveyFormEvent.Submit)

        assertEquals(SurveyFormSubmitState.IDLE, viewModel.state.value.submitState)
        assertEquals("本機送出失敗，請稍後重試", viewModel.state.value.errorMessage)
        assertEquals("保留內容", viewModel.state.value.values.note)
        assertEquals(4, viewModel.state.value.photos.size)
        assertEquals(values.siteConditions, viewModel.state.value.values.siteConditions)
    }
}
