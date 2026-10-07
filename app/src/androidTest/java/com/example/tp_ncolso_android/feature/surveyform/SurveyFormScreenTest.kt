package com.example.tp_ncolso_android.feature.surveyform

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.tp_ncolso_android.DebugSignedInContent
import com.example.tp_ncolso_android.feature.surveyform.data.FakeSurveyFormDataSource
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SurveyFormScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test
    fun noOccupancyHidesOccupationAndSelectingOccupiedShowsIt() {
        val viewModel = SurveyFormViewModel("TEST-KEY-001", FakeSurveyFormDataSource())
        composeRule.setContent {
            AppTheme {
                SurveyFormRoute(
                    viewModel = viewModel,
                    callbacks = SurveyFormCallbacks(onCloseRequested = { _, _ -> }),
                )
            }
        }

        composeRule.onNodeWithTag("survey-system-fields").assertIsDisplayed()
        composeRule.onNodeWithTag("survey-occupation-fields").assertDoesNotExist()
        composeRule.onNodeWithTag("survey-site-option-建物").performClick()
        composeRule.onNodeWithTag("survey-occupation-fields").performScrollTo().assertIsDisplayed()
        assertEquals(listOf("建物"), viewModel.state.value.values.siteConditions)
    }

    @Test
    fun cameraEntryUsesAccessibleCaptureCallbackAndFourPhotoSubmitSucceeds() {
        val source = FakeSurveyFormDataSource()
        val viewModel = SurveyFormViewModel("TEST-KEY-001", source)
        var captureRequests = 0
        composeRule.setContent {
            AppTheme {
                SurveyFormRoute(
                    viewModel = viewModel,
                    callbacks = SurveyFormCallbacks(
                        onCloseRequested = { _, _ -> },
                        onCaptureRequested = { captureRequests += 1 },
                    ),
                )
            }
        }

        composeRule.onNodeWithContentDescription("拍攝現況照片").performScrollTo().performClick()
        assertEquals(1, captureRequests)

        (1..4).forEach { viewModel.onEvent(SurveyFormEvent.CapturePhoto(SurveyFormPhoto("$it", "local://$it", "now"))) }
        composeRule.onNodeWithTag("survey-submit").performClick()
        composeRule.onNodeWithTag("survey-submit-success").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("survey-camera").assertDoesNotExist()
    }

    @Test
    fun invalidPhotoCountBlocksSubmitWithLocalError() {
        val viewModel = SurveyFormViewModel("TEST-KEY-001", FakeSurveyFormDataSource())
        composeRule.setContent {
            AppTheme {
                SurveyFormRoute(
                    viewModel = viewModel,
                    callbacks = SurveyFormCallbacks(onCloseRequested = { _, _ -> }),
                )
            }
        }

        composeRule.onNodeWithTag("survey-submit").performClick()
        composeRule.onNodeWithTag("survey-photo-error").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun ui005EditCallbackOpensSurveyFormInDebugHost() {
        composeRule.setContent {
            AppTheme {
                DebugSignedInContent(
                    identity = AppIdentity("測試人員", AppRole.INVESTIGATOR),
                    onLogout = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput("TEST-KEY-002")
        composeRule.onNodeWithTag("parcel-search-submit").performClick()
        composeRule.onNodeWithTag("parcel-open-detail").performClick()
        composeRule.onNodeWithText("編輯調查").performClick()

        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()
        composeRule.onNodeWithTag("survey-system-fields").assertIsDisplayed()
        composeRule.onNodeWithTag("survey-occupation-fields").performScrollTo().assertIsDisplayed()
    }
}
