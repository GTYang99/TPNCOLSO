package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.tp_ncolso_android.DebugSignedInContent
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class ParcelSearchHostTest {
    @get:Rule val composeRule = createComposeRule()

    private val identity = AppIdentity("測試人員", AppRole.INVESTIGATOR)

    @Test fun toolbarSearchSelectsFakeParcelAndShowsSummaryOverMap() {
        composeRule.setContent {
            AppTheme { DebugSignedInContent(identity = identity, onLogout = {}) }
        }

        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNodeWithTag("parcel-search-input").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("定位目前位置").performClick()
        composeRule.onNodeWithTag("location-loading").assertIsDisplayed()

        composeRule.onNode(hasSetTextAction()).performTextInput("TEST-KEY-001")
        composeRule.onNodeWithTag("parcel-search-submit").performClick()

        composeRule.onNode(hasSetTextAction()).assertTextEquals("TEST-KEY-001")
        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()
        composeRule.onNodeWithText("測試地塊 T-001").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-summary-card").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-status").assertIsDisplayed()
        composeRule.onAllNodesWithText("TEST-KEY-001").assertCountEquals(2)
        composeRule.onNodeWithText("測試段 0012-0000").assertIsDisplayed()
        composeRule.onNodeWithText("無占用").assertIsDisplayed()

        composeRule.onNodeWithText("關閉摘要").performClick()
        composeRule.onNodeWithTag("parcel-summary-card").assertDoesNotExist()
        composeRule.onNodeWithTag("parcel-search-input").assertIsDisplayed()
        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()
        composeRule.onNodeWithText("測試地塊 T-001").assertIsDisplayed()
    }

    @Test fun noMatchKeepsQueryAndMapContainerVisible() {
        composeRule.setContent {
            AppTheme { DebugSignedInContent(identity = identity, onLogout = {}) }
        }

        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNode(hasSetTextAction()).performTextInput("未知地號 88")
        composeRule.onNodeWithTag("parcel-search-submit").performClick()

        composeRule.onNode(hasSetTextAction()).assertTextEquals("未知地號 88")
        composeRule.onNodeWithTag("parcel-search-no-result").assertIsDisplayed()
        composeRule.onNodeWithText("查無資料").assertIsDisplayed()
        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()
        composeRule.onNodeWithText("地塊 A-001").assertIsDisplayed()
        composeRule.onNodeWithTag("parcel-summary-card").assertDoesNotExist()
    }
}
