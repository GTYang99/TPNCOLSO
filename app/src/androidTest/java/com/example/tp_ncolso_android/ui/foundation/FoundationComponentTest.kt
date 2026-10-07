package com.example.tp_ncolso_android.ui.foundation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.tp_ncolso_android.ui.foundation.component.AppCheckboxRow
import com.example.tp_ncolso_android.ui.foundation.component.AppErrorContent
import com.example.tp_ncolso_android.ui.foundation.component.AppIconButton
import com.example.tp_ncolso_android.ui.foundation.component.AppLoadingContent
import com.example.tp_ncolso_android.ui.foundation.component.AppPasswordField
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppRadioGroup
import com.example.tp_ncolso_android.ui.foundation.component.AppReadOnlyField
import com.example.tp_ncolso_android.ui.foundation.component.AppSelectField
import com.example.tp_ncolso_android.ui.foundation.component.AppStatusBadge
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState

@RunWith(AndroidJUnit4::class)
class FoundationComponentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun textFieldShowsRequiredLabelAndErrorText() {
        setTestContent {
            AppTheme {
                AppTextField(
                    value = "",
                    onValueChange = {},
                    label = "帳號",
                    required = true,
                    isError = true,
                    supportingText = "請輸入帳號",
                )
            }
        }

        composeRule.onNodeWithText("帳號 *").assertIsDisplayed()
        composeRule.onNodeWithText("請輸入帳號").assertIsDisplayed()
    }

    @Test
    fun loadingButtonSuppressesClick() {
        var clicks = 0
        setTestContent {
            AppTheme {
                AppPrimaryButton(text = "送出", loading = true, onClick = { clicks++ })
            }
        }

        composeRule.onNodeWithText("送出").assertIsNotEnabled()
        assertEquals(0, clicks)
    }

    @Test
    fun passwordVisibilityActionTogglesCallerState() {
        val visible = mutableStateOf(false)
        setTestContent {
            AppTheme {
                AppPasswordField(
                    value = "secret",
                    onValueChange = {},
                    label = "密碼",
                    visible = visible.value,
                    onVisibilityChange = { visible.value = it },
                )
            }
        }

        assertFalse(visible.value)
        composeRule.onNodeWithText("顯示密碼").performClick()
        assertTrue(visible.value)
    }

    @Test
    fun passwordFieldCanDisableVisibilityAction() {
        setTestContent {
            AppTheme {
                AppPasswordField(
                    value = "secret",
                    onValueChange = {},
                    label = "註冊密碼",
                    visible = false,
                    onVisibilityChange = {},
                    visibilityActionEnabled = false,
                )
            }
        }

        composeRule.onNodeWithText("註冊密碼").assertIsDisplayed()
        composeRule.onNodeWithText("顯示密碼").assertDoesNotExist()
    }

    @Test
    fun passwordFieldSupportsRequiredDisabledReadonlyAndErrorSemantics() {
        setTestContent {
            AppTheme {
                AppPasswordField(
                    value = "secret",
                    onValueChange = {},
                    label = "密碼",
                    visible = false,
                    onVisibilityChange = {},
                    required = true,
                    enabled = false,
                    readOnly = true,
                    isError = true,
                    supportingText = "密碼格式錯誤",
                )
            }
        }

        composeRule.onNodeWithText("密碼 *").assertIsDisplayed().assertIsNotEnabled()
        composeRule.onNodeWithText("密碼格式錯誤").assertIsDisplayed()
    }

    @Test
    fun selectStatusReadOnlyLoadingAndErrorExposeContent() {
        setTestContent {
            AppTheme {
                Column {
                    AppSelectField(
                        selected = null,
                        options = listOf("地籍", "地址"),
                        itemLabel = { it },
                        label = "查詢方式",
                        onSelect = {},
                        isError = true,
                    )
                    AppStatusBadge(label = "已完成", tone = com.example.tp_ncolso_android.ui.foundation.component.AppStatusTone.Success)
                    AppLoadingContent(message = "請稍候")
                    AppErrorContent(message = "載入失敗")
                    AppReadOnlyField(label = "案件編號", value = "TEST-001")
                }
            }
        }

        composeRule.onNodeWithText("查詢方式").assertIsDisplayed()
        composeRule.onNodeWithText("已完成").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("載入中：請稍候").assertIsDisplayed()
        composeRule.onNodeWithText("載入失敗").assertIsDisplayed()
        composeRule.onNodeWithText("案件編號").assertIsDisplayed()
        composeRule.onNodeWithText("TEST-001").assertIsDisplayed()
    }

    @Test
    fun iconButtonExposesDescriptionAndClickAction() {
        var clicks = 0
        setTestContent {
            AppTheme {
                AppIconButton(
                    icon = testIcon(),
                    contentDescription = "關閉",
                    onClick = { clicks++ },
                )
            }
        }

        composeRule.onNodeWithContentDescription("關閉").assertHasClickAction().performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun radioGroupAllowsExactlyOneSelectedOption() {
        val selected = mutableStateOf("A")
        setTestContent {
            AppTheme {
                AppRadioGroup(
                    options = listOf("A", "B"),
                    selected = selected.value,
                    onSelect = { selected.value = it },
                    itemLabel = { it },
                    label = "角色",
                )
            }
        }

        composeRule.onNodeWithText("B").assertHasClickAction().performClick()
        assertEquals("B", selected.value)
    }

    @Test
    fun checkboxRowUsesFullSelectableRow() {
        val checked = mutableStateOf(false)
        setTestContent {
            AppTheme {
                AppCheckboxRow(
                    checked = checked.value,
                    onCheckedChange = { checked.value = it },
                    label = "記住我",
                )
            }
        }

        composeRule.onNodeWithText("記住我")
            .assertHasClickAction()
            .assertIsToggleable()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.ToggleableState, ToggleableState.Off))
        composeRule.onNodeWithText("記住我").performClick()
        assertTrue(checked.value)
    }

    private fun setTestContent(content: @Composable () -> Unit) {
        composeRule.setContent {
            content()
        }
        composeRule.waitForIdle()
    }

    private fun testIcon(): ImageVector = ImageVector.Builder(
        name = "TestIcon",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).build()
}
