package com.example.tp_ncolso_android

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class DebugDirectLoginTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun initialStateShowsDebugModeAndRoles() {
        setTestContent()

        composeRule.onNodeWithText("開發模式").performClick()
        composeRule.onNodeWithText("開發模式").assertExists()
        composeRule.onNodeWithText("調查人員").assertExists()
        composeRule.onNodeWithText("內業人員").assertExists()
        composeRule.onNodeWithText("管理者").assertExists()
    }

    @Test
    fun directLoginAndLogoutReturnToSignedOut() {
        setTestContent()

        composeRule.onNodeWithText("開發模式").performClick()
        composeRule.onNodeWithText("管理者").performClick()
        composeRule.onNodeWithText("直接進入").performClick()
        composeRule.onNodeWithTag("map-surface").assertExists()
        composeRule.onNodeWithContentDescription("開啟選單").performClick()
        composeRule.onNodeWithText("開發測試人員 · ADMINISTRATOR").assertExists()
        composeRule.onNodeWithText("登出").assertExists()

        composeRule.onNodeWithText("登出").performClick()
        composeRule.onNodeWithText("開發模式").assertExists()
        composeRule.onNodeWithText("帳號").assertExists()
        composeRule.onNodeWithText("登入").assertExists()
    }

    private fun setTestContent() {
        composeRule.setContent {
            AppTheme {
                AppEntry()
            }
        }
        composeRule.waitForIdle()
    }
}
