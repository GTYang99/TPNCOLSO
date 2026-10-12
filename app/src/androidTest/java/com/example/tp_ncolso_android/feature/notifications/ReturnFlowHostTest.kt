package com.example.tp_ncolso_android.feature.notifications

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tp_ncolso_android.Ui009StatefulFlowTestActivity
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailViewModel
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEvent
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormField
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormViewModel
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReturnFlowHostTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<Ui009StatefulFlowTestActivity>()

    @Test
    fun notificationListShowsSixRowsClosesAndOpensTheMatchingDetail() {
        composeRule.onNodeWithContentDescription("通知").performClick()
        composeRule.onNodeWithTag("notifications-overlay").assertIsDisplayed()
        listOf(
            "A110151065400000",
            "A110151065400001",
            "A110151065400002",
            "A110151065400003",
            "A110151065400004",
            "A110151065400005",
        ).forEach { keyNo ->
            composeRule.onNodeWithTag("notification-returned-$keyNo").performScrollTo().assertIsDisplayed()
        }
        composeRule.onAllNodesWithText("退回").assertCountEquals(6)
        composeRule.onNodeWithText("意見：請補充現場勘查情形與說明。", substring = true).assertIsDisplayed()

        pressBack()
        composeRule.onNodeWithTag("notifications-overlay").assertDoesNotExist()
        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("通知").performClick()
        composeRule.onNodeWithTag("notification-returned-A110151065400003").performClick()
        composeRule.onNodeWithTag("parcel-detail-overlay").assertIsDisplayed()
        composeRule.onAllNodesWithText("A110151065400003").assertCountEquals(2)
        composeRule.onNodeWithText("請補充現場勘查情形與說明。", substring = true).performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("notifications-overlay").assertDoesNotExist()
    }

    @Test
    fun correctionSaveCancelAndConfirmKeepAllSurfacesConsistent() {
        val keyNo = "A110151065400000"
        composeRule.onNodeWithContentDescription("通知").performClick()
        composeRule.onNodeWithTag("notification-returned-$keyNo").performClick()
        composeRule.onNodeWithTag("parcel-detail-edit").performClick()
        composeRule.onNodeWithTag("survey-return-context").assertIsDisplayed()

        val surveyViewModel = ViewModelProvider(composeRule.activity).get(
            "survey-form-$keyNo-1-0",
            SurveyFormViewModel::class.java,
        )
        val detailViewModel = ViewModelProvider(composeRule.activity).get(
            "parcel-detail-$keyNo-0",
            ParcelDetailViewModel::class.java,
        )
        surveyViewModel.onEvent(SurveyFormEvent.ChangeField(SurveyFormField.NOTE, "本機修正內容"))
        surveyViewModel.onEvent(
            SurveyFormEvent.CapturePhoto(SurveyFormPhoto("correction-photo", "local://correction-photo", "now")),
        )
        composeRule.onNodeWithTag("survey-submit").performClick()

        composeRule.onNodeWithTag("survey-form-overlay").assertDoesNotExist()
        composeRule.onNodeWithTag("parcel-detail-status").assertIsDisplayed()
        assertEquals("退回", detailViewModel.state.value.detail?.status)
        assertEquals("本機修正內容", detailViewModel.state.value.detail?.surveyRecords?.first()?.note)
        composeRule.onNodeWithTag("parcel-detail-confirm-correction").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("是否確認資料已修正？").assertIsDisplayed()
        composeRule.onNodeWithText("送出後將無法再編輯，請確認資料已修正完成。", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("取消").performClick()

        assertEquals("退回", detailViewModel.state.value.detail?.status)
        assertEquals("本機修正內容", detailViewModel.state.value.detail?.surveyRecords?.first()?.note)
        composeRule.onNodeWithTag("parcel-detail-confirm-correction").performClick()
        composeRule.onNodeWithText("確認").performClick()

        assertEquals("已修正", detailViewModel.state.value.detail?.status)
        assertTrue(detailViewModel.state.value.showCorrectionSuccess)
        composeRule.onNodeWithTag("parcel-detail-return-reason").assertDoesNotExist()
        composeRule.onNodeWithTag("parcel-detail-confirm-correction").assertDoesNotExist()
        composeRule.onNodeWithTag("parcel-detail-edit").assertDoesNotExist()
        composeRule.onNodeWithText("關閉詳情").performClick()
        composeRule.onNodeWithTag("notifications-overlay").assertIsDisplayed()
        composeRule.onNodeWithTag("notification-returned-$keyNo").assertDoesNotExist()
    }

    @Test
    fun dirtyCorrectionCanContinueOrDiscardAndCleanExitSkipsPrompt() {
        val keyNo = "A110151065400001"
        composeRule.onNodeWithContentDescription("通知").performClick()
        composeRule.onNodeWithTag("notification-returned-$keyNo").performClick()
        composeRule.onNodeWithTag("parcel-detail-edit").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()
        val firstForm = ViewModelProvider(composeRule.activity).get(
            "survey-form-$keyNo-1-0",
            SurveyFormViewModel::class.java,
        )
        val originalNote = firstForm.state.value.values.note
        firstForm.onEvent(SurveyFormEvent.ChangeField(SurveyFormField.NOTE, "尚未儲存"))
        firstForm.onEvent(SurveyFormEvent.CapturePhoto(SurveyFormPhoto("draft-photo", "local://draft-photo", "now")))
        assertEquals(4, firstForm.state.value.photos.size)

        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertIsDisplayed()
        composeRule.onNodeWithText("繼續編輯").performClick()
        assertEquals("尚未儲存", firstForm.state.value.values.note)
        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()

        pressBack()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertIsDisplayed()
        pressBack()
        assertEquals("尚未儲存", firstForm.state.value.values.note)
        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()

        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("捨棄編輯").performClick()
        composeRule.onNodeWithTag("survey-form-overlay").assertDoesNotExist()
        composeRule.onNodeWithTag("parcel-detail-edit").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("survey-form-overlay").assertIsDisplayed()
        val reopenedForm = ViewModelProvider(composeRule.activity).get(
            "survey-form-$keyNo-2-0",
            SurveyFormViewModel::class.java,
        )
        assertEquals(originalNote, reopenedForm.state.value.values.note)
        assertEquals(3, reopenedForm.state.value.photos.size)
        assertTrue(reopenedForm.state.value.photos.none { it.id == "draft-photo" })
        composeRule.onNodeWithText("關閉填報").performClick()
        composeRule.onNodeWithText("是否捨棄未儲存的內容？").assertDoesNotExist()
        composeRule.onNodeWithTag("survey-form-overlay").assertDoesNotExist()
    }
}
