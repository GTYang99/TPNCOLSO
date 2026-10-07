package com.example.tp_ncolso_android.feature.surveyform

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.geometry.Offset
import androidx.activity.compose.BackHandler
import com.example.tp_ncolso_android.DebugSignedInContent
import com.example.tp_ncolso_android.feature.surveyform.data.LocalSurveySubmitResult
import com.example.tp_ncolso_android.feature.surveyform.data.SurveyFormDataSource
import com.example.tp_ncolso_android.feature.surveyform.data.FakeSurveyFormDataSource
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.Espresso.pressBack

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
    fun deletePhotoRequiresConfirmationAndCancelKeepsPhoto() {
        val photos = (1..4).map { SurveyFormPhoto("photo-$it", "local://$it", "now") }
        val source = object : SurveyFormDataSource {
            override fun load(keyNo: String) = SurveyFormSeed(
                status = "退回",
                values = SurveyFormValues("今天", "測試人員", "B-001", siteConditions = listOf("建物")),
                photos = photos,
            )

            override fun submit(keyNo: String, values: SurveyFormValues, photos: List<SurveyFormPhoto>) =
                LocalSurveySubmitResult.Success
        }
        val viewModel = SurveyFormViewModel("TEST-KEY-002", source)
        composeRule.setContent {
            AppTheme {
                SurveyFormRoute(
                    viewModel = viewModel,
                    callbacks = SurveyFormCallbacks(onCloseRequested = { _, _ -> }),
                )
            }
        }

        composeRule.onNodeWithContentDescription("刪除照片 photo-1").performScrollTo().performClick()
        composeRule.onNodeWithText("刪除確認").assertIsDisplayed()
        composeRule.onNodeWithText("刪除後無法恢復。").assertIsDisplayed()
        assertEquals(4, viewModel.state.value.photos.size)
        composeRule.onNodeWithText("取消").performClick()
        assertEquals(4, viewModel.state.value.photos.size)
        composeRule.onNodeWithTag("survey-camera").assertDoesNotExist()

        composeRule.onNodeWithContentDescription("刪除照片 photo-1").performScrollTo().performClick()
        pressBack()
        assertEquals(4, viewModel.state.value.photos.size)
        composeRule.onNodeWithContentDescription("刪除照片 photo-1").performScrollTo().performClick()
        composeRule.onNodeWithTag("survey-form-overlay").performTouchInput { click(Offset(2f, 2f)) }
        assertEquals(4, viewModel.state.value.photos.size)

        composeRule.onNodeWithContentDescription("刪除照片 photo-1").performScrollTo().performClick()
        composeRule.onNodeWithText("刪除").performClick()
        assertEquals(listOf("photo-2", "photo-3", "photo-4"), viewModel.state.value.photos.map { it.id })
        composeRule.onNodeWithTag("survey-camera").assertExists()
    }

    @Test
    fun dirtyCloseAndBackUseDiscardDialogAndContinuePreservesEdits() {
        val viewModel = SurveyFormViewModel("TEST-KEY-001", FakeSurveyFormDataSource())
        var closeCount = 0
        composeRule.setContent {
            AppTheme {
                SurveyFormRoute(
                    viewModel = viewModel,
                    callbacks = SurveyFormCallbacks(onCloseRequested = { _, _ -> closeCount += 1 }),
                )
            }
        }

        viewModel.onEvent(SurveyFormEvent.ChangeField(SurveyFormField.NOTE, "未儲存備註"))
        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertIsDisplayed()
        composeRule.onNodeWithText("如果現在離開，剛才編輯的內容將會遺失。").assertIsDisplayed()
        composeRule.onNodeWithText("繼續編輯").performClick()
        assertEquals("未儲存備註", viewModel.state.value.values.note)
        assertEquals(0, closeCount)

        pressBack()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertIsDisplayed()
        pressBack()
        assertEquals("未儲存備註", viewModel.state.value.values.note)
        assertEquals(0, closeCount)

        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("捨棄編輯").performClick()
        assertEquals(1, closeCount)
        assertEquals("未儲存備註", viewModel.state.value.values.note)
    }

    @Test
    fun cleanSystemBackClosesWithoutDiscardDialog() {
        val viewModel = SurveyFormViewModel("TEST-KEY-001", FakeSurveyFormDataSource())
        var closeCount = 0
        composeRule.setContent {
            AppTheme {
                SurveyFormRoute(
                    viewModel = viewModel,
                    callbacks = SurveyFormCallbacks(onCloseRequested = { _, _ -> closeCount += 1 }),
                )
            }
        }

        pressBack()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertDoesNotExist()
        assertEquals(1, closeCount)
    }

    @Test
    fun cleanCloseSkipsDiscardDialogAndRouteBackIsDisabledWhenNotForegrounded() {
        val viewModel = SurveyFormViewModel("TEST-KEY-001", FakeSurveyFormDataSource())
        var closeCount = 0
        var parentBackCount = 0
        composeRule.setContent {
            AppTheme {
                BackHandler { parentBackCount += 1 }
                SurveyFormRoute(
                    viewModel = viewModel,
                    backEnabled = false,
                    callbacks = SurveyFormCallbacks(onCloseRequested = { _, _ -> closeCount += 1 }),
                )
            }
        }

        pressBack()
        assertEquals(1, parentBackCount)
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertDoesNotExist()
        assertEquals(0, closeCount)
        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertDoesNotExist()
        assertEquals(1, closeCount)
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
        composeRule.onNodeWithTag("parcel-detail-edit").performClick()

        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()
        composeRule.onNodeWithTag("survey-system-fields").assertIsDisplayed()
        composeRule.onNodeWithTag("survey-occupation-fields").performScrollTo().assertIsDisplayed()

        composeRule.onNodeWithContentDescription("刪除照片 returned-1").performScrollTo().performClick()
        composeRule.onNodeWithText("刪除").performClick()
        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("捨棄編輯").performClick()
        composeRule.onNodeWithTag("parcel-detail-edit").performClick()
        composeRule.onNodeWithTag("survey-photo-returned-1").assertExists()
    }
}
