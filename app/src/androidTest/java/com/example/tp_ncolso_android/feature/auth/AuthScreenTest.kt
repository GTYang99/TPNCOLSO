package com.example.tp_ncolso_android.feature.auth

import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.foundation.layout.Box
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.io.PlatformTestStorageRegistry
import java.io.File
import java.io.FileOutputStream
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class AuthScreenTest {
    @get:Rule val composeRule = createComposeRule()

    private fun exportComposeCapture(output: File, remoteName: String) {
        PlatformTestStorageRegistry.getInstance().openOutputFile(remoteName).use { destination ->
            output.inputStream().use { source -> source.copyTo(destination) }
        }
    }

    @Test fun loginRendersRequiredEntryPoints() {
        composeRule.setContent {
            AppTheme { LoginScreen(LoginFormState(), {},) }
        }
        composeRule.onNodeWithText("帳號").assertIsDisplayed()
        composeRule.onNodeWithText("密碼").assertIsDisplayed()
        composeRule.onAllNodesWithText("驗證碼").assertCountEquals(0)
        composeRule.onAllNodesWithTag("login-captcha").assertCountEquals(0)
        composeRule.onAllNodesWithTag("captcha-image").assertCountEquals(0)
        composeRule.onAllNodesWithTag("captcha-refresh").assertCountEquals(0)
        composeRule.onNodeWithText("記住我").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("登入").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("沒有帳號? 註冊").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("login-register").assertHeightIsEqualTo(48.dp)
        composeRule.onAllNodesWithContentDescription("品牌標誌").assertCountEquals(0)
    }

    @Test fun loginRegisterEntryDispatchesEvent() {
        var event: AuthEvent? = null
        composeRule.setContent { AppTheme { LoginScreen(LoginFormState(), { event = it }) } }
        composeRule.onNodeWithText("沒有帳號? 註冊").performScrollTo().performClick()
        assert(event == AuthEvent.OpenRegister)
    }

    @Test fun loginActionsReflowAfterPasswordAndUseFoundationSpacing() {
        composeRule.setContent {
            AppTheme { LoginScreen(LoginFormState(), {}) }
        }
        val password = composeRule.onNodeWithTag("login-password").fetchSemanticsNode().boundsInRoot
        val remember = composeRule.onNodeWithTag("login-remember").fetchSemanticsNode().boundsInRoot
        val register = composeRule.onNodeWithTag("login-register").fetchSemanticsNode().boundsInRoot
        val submit = composeRule.onNodeWithTag("login-submit").fetchSemanticsNode().boundsInRoot
        assertTrue(remember.top >= password.bottom)
        assertTrue(register.top >= remember.top)
        assertTrue(submit.top >= remember.bottom)
        assertEquals(16.dp.value, (submit.top - remember.bottom) / composeRule.density.density, 1f)
    }

    @Test fun captureLoginR11ReflowGeometryForEvidence() {
        composeRule.setContent { AppTheme { LoginScreen(LoginFormState(), {}) } }
        composeRule.waitForIdle()
        val password = composeRule.onNodeWithTag("login-password").fetchSemanticsNode().boundsInRoot
        val remember = composeRule.onNodeWithTag("login-remember").fetchSemanticsNode().boundsInRoot
        val register = composeRule.onNodeWithTag("login-register").fetchSemanticsNode().boundsInRoot
        val submit = composeRule.onNodeWithTag("login-submit").fetchSemanticsNode().boundsInRoot
        val density = composeRule.density.density
        val output = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "login-r11-reflow-geometry.json")
        output.writeText(
            """{
              "device": "Medium_Phone(AVD) - 16",
              "api": 36,
              "density": $density,
              "root": {"widthPx": 1080, "heightPx": 2400},
              "boundsPx": {
                "password": {"top": ${password.top}, "bottom": ${password.bottom}},
                "remember": {"top": ${remember.top}, "bottom": ${remember.bottom}},
                "register": {"top": ${register.top}, "bottom": ${register.bottom}},
                "submit": {"top": ${submit.top}, "bottom": ${submit.bottom}}
              },
              "derivedDp": {
                "submitTopMinusRememberBottom": ${(submit.top - remember.bottom) / density},
                "rememberTopMinusPasswordBottom": ${(remember.top - password.bottom) / density}
              },
              "assertions": [
                "remember.top >= password.bottom",
                "register.top >= remember.top",
                "submit.top >= remember.bottom",
                "submit.top - remember.bottom == 16dp"
              ]
            }""".trimIndent(),
        )
        exportComposeCapture(output, "login-r11-reflow-geometry.json")
        check(output.length() > 0)
    }

    @Test fun loginAuthErrorKeepsValuesAndShowsAccessibleGlobalMessage() {
        composeRule.setContent {
            AppTheme {
                LoginScreen(
                    LoginFormState(account = "sunrise000", password = "********", rememberMe = true, requestError = "帳號或密碼錯誤"),
                    {},
                )
            }
        }
        composeRule.onNodeWithText("sunrise000").assertIsDisplayed()
        composeRule.onNodeWithText("帳號或密碼錯誤").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("login-request-error").performScrollTo().assertIsDisplayed()
    }

    @Test fun loginSubmittingStateDisablesSubmit() {
        composeRule.setContent { AppTheme { LoginScreen(LoginFormState(submitting = true), {}) } }
        composeRule.onNodeWithText("登入").assertIsNotEnabled()
    }

    @Test fun loginPasswordVisibilityActionTogglesMaskedState() {
        var state by mutableStateOf(LoginFormState(password = "password"))
        composeRule.setContent {
            AppTheme {
                LoginScreen(state, onEvent = { event ->
                    if (event is AuthEvent.LoginPasswordVisibilityChanged) {
                        state = state.copy(passwordVisible = event.value)
                    }
                })
            }
        }
        composeRule.onNodeWithText("顯示密碼").performClick()
        composeRule.onNodeWithText("隱藏密碼").assertIsDisplayed()
        composeRule.onNodeWithText("隱藏密碼").performClick()
        composeRule.onNodeWithText("顯示密碼").assertIsDisplayed()
    }

    @Test fun registerRendersSixFieldsAndUnselectedWorkType() {
        composeRule.setContent { AppTheme { RegisterScreen(RegisterFormState(), listOf(VendorOption("1", "廠商")), {}) } }
        composeRule.onNodeWithText("帳號").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("密碼").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("確認密碼").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("廠商名稱").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("作業性質").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("姓名(請輸入真實姓名)").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("外業人員").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("內業人員").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("register-back").assertWidthIsEqualTo(48.dp).assertHeightIsEqualTo(48.dp)
        composeRule.onNodeWithContentDescription("返回登入頁").performScrollTo().assertIsDisplayed()
        composeRule.onAllNodesWithContentDescription("註冊插圖").assertCountEquals(0)
    }

    @Test fun registerNarrowWidthKeepsBothWorkTypeLabelsAndTargets() {
        composeRule.setContent {
            AppTheme {
                Box(Modifier.width(226.dp)) {
                    RegisterScreen(RegisterFormState(), listOf(VendorOption("1", "廠商")), {})
                }
            }
        }
        composeRule.onNodeWithText("外業人員").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("內業人員").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("外業人員").assertHasClickAction()
        composeRule.onNodeWithText("內業人員").assertHasClickAction()
    }

    @Test fun registerCancelDispatchesBackToLogin() {
        var event: AuthEvent? = null
        composeRule.setContent { AppTheme { RegisterScreen(RegisterFormState(), emptyList(), { event = it }) } }
        composeRule.onNodeWithText("取消").performScrollTo().performClick()
        assert(event == AuthEvent.CancelRegister)
    }

    @Test fun registerShowsWorkTypeValidationMessage() {
        composeRule.setContent {
            AppTheme { RegisterScreen(RegisterFormState(fieldErrors = mapOf(RegisterField.WORK_TYPE to "請選擇作業性質")), emptyList(), {}) }
        }
        composeRule.onNodeWithText("請選擇作業性質").performScrollTo().assertIsDisplayed()
    }

    @Test fun registerSubmittingStateDisablesSubmit() {
        composeRule.setContent { AppTheme { RegisterScreen(RegisterFormState(submitting = true), emptyList(), {}) } }
        composeRule.onNodeWithText("完成").assertIsNotEnabled()
        composeRule.onNodeWithText("取消").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("返回登入頁").assertIsNotEnabled()
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
        composeRule.onAllNodesWithText("顯示密碼").assertCountEquals(0)
        composeRule.onNodeWithText("外業人員").performScrollTo().assertIsDisplayed()
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
        exportComposeCapture(output, "register-filled-compose-root.png")
        check(output.length() > 0)
    }

    @Test fun captureRegisterEmptyStateForVisualEvidence() {
        composeRule.setContent {
            AppTheme { RegisterScreen(RegisterFormState(), listOf(VendorOption("1", "廠商")), {}) }
        }
        composeRule.waitForIdle()
        val output = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "register-empty-compose.png")
        FileOutputStream(output).use { stream ->
            composeRule.onRoot().captureToImage().asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        }
        exportComposeCapture(output, "register-empty-compose-root.png")
        check(output.length() > 0)
    }

    @Test fun captureLoginFilledStateForVisualEvidence() {
        composeRule.setContent {
            AppTheme {
                LoginScreen(
                    LoginFormState(account = "sunrise000", password = "password", rememberMe = true),
                    {},
                )
            }
        }
        composeRule.waitForIdle()
        val output = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "login-filled-compose.png")
        FileOutputStream(output).use { stream ->
            composeRule.onRoot().captureToImage().asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        }
        exportComposeCapture(output, "login-filled-compose-root.png")
        check(output.length() > 0)
    }

    @Test fun captureLoginEmptyStateForVisualEvidence() {
        composeRule.setContent {
            AppTheme {
                LoginScreen(
                    LoginFormState(),
                    {},
                )
            }
        }
        composeRule.waitForIdle()
        val output = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "login-empty-compose.png")
        FileOutputStream(output).use { stream ->
            composeRule.onRoot().captureToImage().asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        }
        exportComposeCapture(output, "login-empty-compose-root.png")
        check(output.length() > 0)
    }

    @Test fun captureLoginAuthErrorStateForVisualEvidence() {
        composeRule.setContent {
            AppTheme {
                LoginScreen(
                    LoginFormState(account = "sunrise000", password = "password", rememberMe = true, requestError = "帳號或密碼錯誤"),
                    {},
                )
            }
        }
        composeRule.waitForIdle()
        val output = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "login-error-compose.png")
        FileOutputStream(output).use { stream ->
            composeRule.onRoot().captureToImage().asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        }
        exportComposeCapture(output, "login-error-compose-root.png")
        check(output.length() > 0)
    }

    @Test fun captureLoginImeStateForVisualEvidence() {
        composeRule.setContent {
            AppTheme {
                LoginScreen(
                    LoginFormState(),
                    {},
                )
            }
        }
        composeRule.onNodeWithTag("login-account").performClick()
        composeRule.waitForIdle()
        val output = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "login-ime-compose.png")
        FileOutputStream(output).use { stream ->
            composeRule.onRoot().captureToImage().asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream)
        }
        exportComposeCapture(output, "login-ime-compose-root.png")
        check(output.length() > 0)
    }
}
