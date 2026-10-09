package com.example.tp_ncolso_android.feature

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import com.example.tp_ncolso_android.Ui009StatefulFlowTestActivity
import com.example.tp_ncolso_android.feature.mapshell.Basemap
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailTab
import org.junit.Rule
import org.junit.Test

class StatefulUiFlowRecreationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<Ui009StatefulFlowTestActivity>()

    @Test
    fun mapBasemapSelectionSurvivesActivityRecreation() {
        composeRule.onNodeWithContentDescription(Basemap.ORTHOPHOTO.label).performClick()
        composeRule.onNodeWithContentDescription(Basemap.ORTHOPHOTO.label).assertIsSelected()
        recreateActivity()

        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()
        composeRule.onNodeWithContentDescription(Basemap.ORTHOPHOTO.label).assertIsSelected()
    }

    @Test
    fun parcelSearchUiFlowRestoresQueryAndSummaryAfterActivityRecreation() {
        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput("TEST-KEY-001")
        composeRule.onNodeWithTag("parcel-search-submit").performClick()
        composeRule.onNodeWithTag("parcel-summary-card").assertIsDisplayed()
        recreateActivity()

        composeRule.onNodeWithTag("parcel-search-overlay").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-summary-card").assertIsDisplayed()
        composeRule.onNodeWithText("測試地塊 T-001").assertIsDisplayed()
        composeRule.onNodeWithText("關閉摘要").performClick()
        composeRule.onNodeWithTag("parcel-summary-card").assertDoesNotExist()
    }

    @Test
    fun parcelDetailHistoryUiFlowRestoresRouteAndSelectionAfterActivityRecreation() {
        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput("TEST-KEY-001")
        composeRule.onNodeWithTag("parcel-search-submit").performClick()
        composeRule.onNodeWithTag("parcel-open-detail").performScrollTo().performClick()
        composeRule.onNodeWithTag("parcel-detail-tab-${ParcelDetailTab.SURVEY.ordinal}")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("parcel-detail-period-selector")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithText("2025 年歷史期別").performClick()
        composeRule.onNodeWithTag("parcel-detail-history-readonly").performScrollTo().assertIsDisplayed()
        recreateActivity()

        composeRule.onNodeWithTag("parcel-detail-overlay").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-detail-history-readonly").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("民國 114 年 9 月 3 日").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("關閉詳情").performClick()
        composeRule.onNodeWithTag("parcel-search-overlay").assertIsDisplayed()
    }

    private fun recreateActivity() {
        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()
    }
}
