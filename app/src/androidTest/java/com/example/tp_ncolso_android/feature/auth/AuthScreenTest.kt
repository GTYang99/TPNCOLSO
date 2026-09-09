package com.example.tp_ncolso_android.feature.auth

import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.io.FileOutputStream

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

    @Test fun loginAuthErrorKeepsValuesAndShowsAccessibleGlobalMessage() {
        composeRule.setContent {
            AppTheme {
                LoginScreen(
                    LoginFormState(account = "sunrise000", password = "********", captcha = "0926", rememberMe = true, requestError = "帳號、密碼或驗證碼錯誤"),
                    {},
                )
            }
        }
        composeRule.onNodeWithText("sunrise000").assertIsDisplayed()
        composeRule.onNodeWithText("帳號、密碼或驗證碼錯誤").assertIsDisplayed()
        composeRule.onNodeWithTag("login-request-error").assertIsDisplayed()
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

    @Test fun registerShowsWorkTypeValidationMessage() {
        composeRule.setContent {
            AppTheme { RegisterScreen(RegisterFormState(fieldErrors = mapOf(RegisterField.WORK_TYPE to "請選擇作業性質")), emptyList(), {}) }
        }
        composeRule.onNodeWithText("請選擇作業性質").assertIsDisplayed()
    }

    @Test fun registerFilledStateUsesPlaintextFieldsWithoutVisibilityAction() {
        composeRule.setContent {
            AppTheme {
                RegisterScreen(
                    RegisterFormState(account = "sunrise1234", password = "sfk;wfj1~", confirmPassword = "sfk;wfj1~", vendor = VendorOption("1", "日陞"), workType = WorkType.FIELD, name = "劉大君"),
                    listOf(VendorOption("1", "日陞")),
                    {},
                )
            }
        }
        composeRule.onNodeWithText("sunrise1234").assertIsDisplayed()
        composeRule.onAllNodesWithText("sfk;wfj1~").assertCountEquals(2)
        composeRule.onNodeWithText("顯示密碼").assertDoesNotExist()
        composeRule.onNodeWithText("外業人員").assertIsDisplayed()
    }

    @Test fun captureRegisterFilledStateForVisualEvidence() {
        composeRule.setContent {
            AppTheme {
                RegisterScreen(
                    RegisterFormState(account = "sunrise1234", password = "sfk;wfj1~", confirmPassword = "sfk;wfj1~", vendor = VendorOption("1", "日陞"), workType = WorkType.FIELD, name = "劉大君"),
                    listOf(VendorOption("1", "日陞")),
                    {},
                )
            }
        }
        composeRule.waitForIdle()
        val output = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "register-filled-compose.png")
        FileOutputStream(output).use { stream ->
            composeRule.onRoot().captureToImage().asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        }
        InstrumentationRegistry.getInstrumentation().uiAutomation
            .executeShellCommand("screencap -p /sdcard/register-filled-compose.png")
            .close()
        check(output.length() > 0)
    }
}
