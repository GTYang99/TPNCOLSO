package com.example.tp_ncolso_android.feature.mapshell

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class MapShellScreenTest {
    @get:Rule val composeRule = createComposeRule()

    private val identity = AppIdentity("測試人員", AppRole.INVESTIGATOR)

    @Test fun shellRendersControlsAndExactlyThreeBasemaps() {
        composeRule.setContent {
            AppTheme { MapShellScreen(MapShellUiState(identity), {}) }
        }

        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("開啟選單").assertHasClickAction()
        composeRule.onNodeWithContentDescription("關鍵字搜尋").assertHasClickAction()
        composeRule.onNodeWithContentDescription("通知").assertHasClickAction()
        composeRule.onNodeWithText("電子地圖").assertIsDisplayed()
        composeRule.onNodeWithText("正射圖").assertIsDisplayed()
        composeRule.onNodeWithText("地形圖").assertIsDisplayed()
    }

    @Test fun drawerLogoutEmitsOneCallbackAndExposesAccessibleAction() {
        val viewModel = MapShellViewModel(identity)
        var logoutCount = 0
        composeRule.setContent {
            AppTheme {
                MapShellRoute(
                    identity = identity,
                    viewModel = viewModel,
                    callbacks = MapShellCallbacks(onLogoutRequested = { logoutCount += 1 }),
                )
            }
        }

        composeRule.onNodeWithContentDescription("開啟選單").performClick()
        composeRule.onNodeWithTag("map-drawer").assertIsDisplayed()
        composeRule.onNodeWithText("登出").assertHasClickAction().performClick()
        composeRule.waitForIdle()

        assertEquals(1, logoutCount)
        composeRule.onNodeWithTag("map-drawer").assertDoesNotExist()
    }

    @Test fun permissionDeniedStateShowsReadableRetryAction() {
        var event: MapShellEvent? = null
        composeRule.setContent {
            AppTheme {
                MapShellScreen(
                    state = MapShellUiState(identity, locationState = LocationState.PERMISSION_DENIED),
                    onEvent = { event = it },
                )
            }
        }

        composeRule.onNodeWithTag("location-denied").assertIsDisplayed()
        composeRule.onNodeWithTag("location-retry").assertIsDisplayed().performClick()
        assertEquals(MapShellEvent.RetryLocation, event)
    }
}
