package com.example.tp_ncolso_android.feature.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme

@RunWith(AndroidJUnit4::class)
class AuthScreenTest {
    @get:Rule val composeRule = createComposeRule()

    @Test fun loginRendersRequiredEntryPoints() {
        composeRule.setContent {
            AppTheme { LoginScreen(LoginFormState(), {},) }
        }
        composeRule.onNodeWithText("帳號").assertIsDisplayed()
        composeRule.onNodeWithText("密碼").assertIsDisplayed()
        composeRule.onNodeWithText("驗證碼").assertIsDisplayed()
        composeRule.onNodeWithText("記住我").assertIsDisplayed()
        composeRule.onNodeWithText("登入").assertIsDisplayed()
        composeRule.onNodeWithText("沒有帳號? 註冊").assertIsDisplayed()
    }

    @Test fun loginRegisterEntryDispatchesEvent() {
        var event: AuthEvent? = null
        composeRule.setContent { AppTheme { LoginScreen(LoginFormState(), { event = it }) } }
        composeRule.onNodeWithText("沒有帳號? 註冊").performClick()
        assert(event == AuthEvent.OpenRegister)
    }

    @Test fun registerRendersSixFieldsAndUnselectedWorkType() {
        composeRule.setContent { AppTheme { RegisterScreen(RegisterFormState(), listOf(VendorOption("1", "廠商")), {}) } }
        composeRule.onNodeWithText("帳號").assertIsDisplayed()
        composeRule.onNodeWithText("密碼").assertIsDisplayed()
        composeRule.onNodeWithText("確認密碼").assertIsDisplayed()
        composeRule.onNodeWithText("廠商名稱").assertIsDisplayed()
        composeRule.onNodeWithText("作業性質").assertIsDisplayed()
        composeRule.onNodeWithText("姓名(請輸入真實姓名)").assertIsDisplayed()
        composeRule.onNodeWithText("外業人員").assertIsDisplayed()
        composeRule.onNodeWithText("內業人員").assertIsDisplayed()
    }

    @Test fun registerCancelDispatchesBackToLogin() {
        var event: AuthEvent? = null
        composeRule.setContent { AppTheme { RegisterScreen(RegisterFormState(), emptyList(), { event = it }) } }
        composeRule.onNodeWithText("取消").performClick()
        assert(event == AuthEvent.CancelRegister)
    }
}
