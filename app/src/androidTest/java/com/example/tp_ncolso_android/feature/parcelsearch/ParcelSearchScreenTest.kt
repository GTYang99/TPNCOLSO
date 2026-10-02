package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.tp_ncolso_android.feature.mapshell.MapShellCallbacks
import com.example.tp_ncolso_android.feature.mapshell.MapShellRoute
import com.example.tp_ncolso_android.feature.mapshell.MapShellViewModel
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class ParcelSearchScreenTest {
    @get:Rule val composeRule = createComposeRule()

    private val identity = AppIdentity("測試人員", AppRole.INVESTIGATOR)

    @Test
    fun matchingQueryShowsSummaryAndUpdatesOnlyMapSearchContext() {
        val mapViewModel = MapShellViewModel(identity)
        val searchViewModel = ParcelSearchViewModel()
        composeRule.setContent {
            AppTheme {
                Box(Modifier.fillMaxSize()) {
                    MapShellRoute(identity = identity, viewModel = mapViewModel)
                    ParcelSearchRoute(
                        viewModel = searchViewModel,
                        onClose = {},
                        onParcelSelected = { query, target ->
                            mapViewModel.onEvent(
                                com.example.tp_ncolso_android.feature.mapshell.MapShellEvent.UpdateSearchContext(query, target),
                            )
                        },
                    )
                }
            }
        }

        composeRule.onNode(hasSetTextAction()).performTextInput("KS-10001")
        composeRule.onNodeWithText("搜尋土地").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("土地摘要").assertIsDisplayed()
        composeRule.onNodeWithText("已登記").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).assertTextEquals("KS-10001")
        composeRule.onAllNodesWithText("KS-10001").assertCountEquals(2)
        composeRule.onNodeWithText("中正段 0012-0000").assertIsDisplayed()
        composeRule.onNodeWithText("建物使用中").assertIsDisplayed()
        assertEquals("KS-10001", mapViewModel.state.value.overlay.activeQuery)
        assertEquals("地塊 A-001", mapViewModel.state.value.overlay.selectedTarget)
    }

    @Test
    fun noResultRetainsQueryAndKeepsMapMounted() {
        val mapViewModel = MapShellViewModel(identity)
        val searchViewModel = ParcelSearchViewModel()
        composeRule.setContent {
            AppTheme {
                Box(Modifier.fillMaxSize()) {
                    MapShellRoute(identity = identity, viewModel = mapViewModel)
                    ParcelSearchRoute(viewModel = searchViewModel, onClose = {}, onParcelSelected = { _, _ -> })
                }
            }
        }

        composeRule.onNode(hasSetTextAction()).performTextInput("不存在的地號")
        composeRule.onNodeWithText("搜尋土地").performClick()

        composeRule.onNodeWithText("查無資料").assertIsDisplayed()
        composeRule.onNode(hasSetTextAction()).assertTextEquals("不存在的地號").assertIsDisplayed()
        composeRule.onNodeWithTag("map-surface").assertIsDisplayed()
        assertEquals("不存在的地號", searchViewModel.state.value.query)
        assertEquals("", mapViewModel.state.value.overlay.activeQuery)
    }

    @Test
    fun topToolbarOpensSearchAndLocationControlRemainsSeparate() {
        val mapViewModel = MapShellViewModel(identity)
        val searchViewModel = ParcelSearchViewModel()
        var locationCalls = 0
        composeRule.setContent {
            val searchIsOpen = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
            AppTheme {
                Box(Modifier.fillMaxSize()) {
                    MapShellRoute(
                        identity = identity,
                        viewModel = mapViewModel,
                        callbacks = MapShellCallbacks(
                            onSearch = { searchIsOpen.value = true },
                            onLocation = { locationCalls += 1 },
                        ),
                    )
                    if (searchIsOpen.value) {
                        ParcelSearchRoute(viewModel = searchViewModel, onClose = { searchIsOpen.value = false }, onParcelSelected = { _, _ -> })
                    }
                }
            }
        }

        composeRule.onNodeWithContentDescription("關鍵字搜尋").performClick()
        composeRule.onNodeWithText("土地查詢").assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription("關鍵字搜尋").assertCountEquals(1)
        composeRule.onNodeWithContentDescription("定位目前位置").performClick()
        assertEquals(1, locationCalls)
    }
}
