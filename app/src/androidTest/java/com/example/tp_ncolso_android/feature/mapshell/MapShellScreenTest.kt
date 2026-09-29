package com.example.tp_ncolso_android.feature.mapshell

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
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
        val viewModel = MapShellViewModel(identity)
        composeRule.setContent {
            AppTheme { MapShellRoute(identity = identity, viewModel = viewModel) }
        }

        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("開啟選單").assertHasClickAction()
        composeRule.onNodeWithContentDescription("關鍵字搜尋").assertHasClickAction()
        composeRule.onNodeWithContentDescription("通知").assertHasClickAction()
        composeRule.onNodeWithText("電子地圖").assertIsDisplayed()
        composeRule.onNodeWithText("正射圖").assertIsDisplayed()
        composeRule.onNodeWithText("地形圖").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("電子地圖").assertIsSelected()
        composeRule.onNodeWithContentDescription("正射圖").assertIsNotSelected()
        composeRule.onNodeWithContentDescription("地形圖").assertIsNotSelected()
        composeRule.onNodeWithContentDescription("正射圖").performClick()
        composeRule.onNodeWithContentDescription("電子地圖").assertIsNotSelected()
        composeRule.onNodeWithContentDescription("正射圖").assertIsSelected()
        composeRule.onNodeWithContentDescription("地形圖").assertIsNotSelected()
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
        composeRule.onNodeWithTag("map-platform").assertHasClickAction()
        composeRule.onNodeWithTag("map-dashboard").assertHasClickAction()
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
        composeRule.onNodeWithText("重試").assertIsDisplayed().performClick()
        assertEquals(MapShellEvent.RetryLocation, event)
    }

    @Test fun loadingStateUsesCommonLoadingSemantics() {
        composeRule.setContent {
            AppTheme {
                MapShellScreen(
                    state = MapShellUiState(identity, locationState = LocationState.LOADING),
                    onEvent = {},
                )
            }
        }

        composeRule.onNodeWithTag("location-loading").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("載入中：定位中…").assertIsDisplayed()
    }

    @Test fun drawerNavigationCallbacksAreForwarded() {
        var mapPlatform = 0
        var dashboard = 0
        val viewModel = MapShellViewModel(identity)
        composeRule.setContent {
            AppTheme {
                MapShellRoute(
                    identity = identity,
                    viewModel = viewModel,
                    callbacks = MapShellCallbacks(
                        onMapPlatform = { mapPlatform += 1 },
                        onDashboard = { dashboard += 1 },
                    ),
                )
            }
        }

        composeRule.onNodeWithContentDescription("開啟選單").performClick()
        composeRule.onNodeWithTag("map-platform").performClick()
        composeRule.onNodeWithTag("map-dashboard").performClick()

        assertEquals(1, mapPlatform)
        assertEquals(1, dashboard)
    }

    @Test fun topAndLocationControlsForwardExplicitCallbacks() {
        var search = 0
        var notifications = 0
        var navigation = 0
        var location = 0
        composeRule.setContent {
            AppTheme {
                MapShellRoute(
                    identity = identity,
                    callbacks = MapShellCallbacks(
                        onSearch = { search += 1 },
                        onNotifications = { notifications += 1 },
                        onMapNavigation = { navigation += 1 },
                        onLocation = { location += 1 },
                    ),
                )
            }
        }

        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNodeWithContentDescription("通知").performClick()
        composeRule.onNodeWithContentDescription("定位目前位置").performClick()
        composeRule.onNodeWithText("地塊 A-001").performClick()

        assertEquals(1, search)
        assertEquals(1, notifications)
        assertEquals(1, location)
        assertEquals(1, navigation)
    }
}
