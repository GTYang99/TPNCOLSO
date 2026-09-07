package com.example.tp_ncolso_android.ui.foundation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.tp_ncolso_android.ui.foundation.component.AppPasswordField
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppRadioGroup
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FoundationComponentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun textFieldShowsRequiredLabelAndErrorText() {
        composeRule.setContent {
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
        composeRule.setContent {
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
        composeRule.setContent {
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
    fun radioGroupAllowsExactlyOneSelectedOption() {
        val selected = mutableStateOf("A")
        composeRule.setContent {
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
}
