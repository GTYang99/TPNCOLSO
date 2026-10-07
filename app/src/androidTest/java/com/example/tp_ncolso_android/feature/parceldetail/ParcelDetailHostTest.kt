package com.example.tp_ncolso_android.feature.parceldetail

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.tp_ncolso_android.DebugSignedInContent
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class ParcelDetailHostTest {
    @get:Rule val composeRule = createComposeRule()

    private val identity = AppIdentity("測試人員", AppRole.INVESTIGATOR)

    @Test
    fun matchedSummaryOpensReadOnlyDetailOverMountedMap() {
        composeRule.setContent {
            AppTheme { DebugSignedInContent(identity = identity, onLogout = {}) }
        }

        openFirstParcelDetail()

        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-overlay").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-fixed-summary").assertIsDisplayed()
        composeRule.onNodeWithText("未調查").assertIsDisplayed()
        composeRule.onNodeWithText("TEST-KEY-001").assertIsDisplayed()
        composeRule.onAllNodesWithText("測試段 0012-0000").assertCountEquals(2)
        composeRule.onNodeWithText("無占用").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-return-reason").assertDoesNotExist()
        composeRule.onNodeWithTag("parcel-detail-land-field-地號").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-land-field-都市計劃使用分區").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-land-field-宗地面積").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-land-field-權利範圍-分子").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-land-field-權利範圍-分母").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-land-field-持分面積(㎡)").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-land-field-所有權人").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-land-field-登記原因").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-gis-field-是否遭占").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-gis-field-占用狀況").performScrollTo().assertIsDisplayed()
        composeRule.onAllNodes(hasSetTextAction()).assertCountEquals(0)
    }

    @Test
    fun surveyTabShowsLatestAndHistoryAsReadOnly() {
        composeRule.setContent {
            AppTheme { DebugSignedInContent(identity = identity, onLogout = {}) }
        }

        openFirstParcelDetail()
        composeRule.onNodeWithTag("parcel-detail-tab-1").performClick()

        composeRule.onNodeWithTag("parcel-detail-latest").assertIsDisplayed()
        composeRule.onNodeWithText("民國 115 年 10 月 7 日").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-survey-field-現況照片")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithText("尚未拍攝").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-period-selector")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithText("2025 年歷史期別").performClick()

        composeRule.onNodeWithTag("parcel-detail-history-readonly").assertIsDisplayed()
        composeRule.onNodeWithText("民國 114 年 9 月 3 日").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-survey-field-備註")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithText("歷史檢視資料").assertIsDisplayed()
        composeRule.onAllNodes(hasSetTextAction()).assertCountEquals(0)
    }

    @Test
    fun returnedParcelShowsReasonAndEditOnlyHandsOffToUi006() {
        var editKey: String? = null
        composeRule.setContent {
            AppTheme {
                DebugSignedInContent(
                    identity = identity,
                    onLogout = {},
                    onEditSurvey = { editKey = it },
                )
            }
        }

        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput("TEST-KEY-002")
        composeRule.onNodeWithTag("parcel-search-submit").performClick()
        composeRule.onNodeWithTag("parcel-open-detail").performClick()

        composeRule.onNodeWithTag("parcel-detail-return-reason").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-return-tag").assertIsDisplayed()
        composeRule.onNodeWithText("請補拍完整現況照片後重新送出。").assertIsDisplayed()
        composeRule.onNodeWithText("編輯調查").performClick()

        assertEquals("TEST-KEY-002", editKey)
        composeRule.onNodeWithTag("parcel-detail-overlay").assertIsDisplayed()
    }

    @Test
    fun closeDetailReturnsToSearchContext() {
        composeRule.setContent {
            AppTheme { DebugSignedInContent(identity = identity, onLogout = {}) }
        }

        openFirstParcelDetail()
        composeRule.onNodeWithText("關閉詳情").performClick()

        composeRule.onNodeWithTag("parcel-search-overlay").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-overlay").assertDoesNotExist()
        composeRule.onNodeWithText("查看詳情").assertIsDisplayed()
    }

    private fun openFirstParcelDetail() {
        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput("TEST-KEY-001")
        composeRule.onNodeWithTag("parcel-search-submit").performClick()
        composeRule.onNodeWithTag("parcel-open-detail").performClick()
    }
}
