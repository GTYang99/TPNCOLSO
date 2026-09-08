package com.example.tp_ncolso_android.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.ui.foundation.component.AppCheckboxRow
import com.example.tp_ncolso_android.ui.foundation.component.AppPasswordField
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Composable
fun LoginScreen(state: LoginFormState, onEvent: (AuthEvent) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color.White, AppThemeTokens.colors.surfaceMuted)))) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 32.dp).verticalScroll(rememberScrollState()).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("新工處土地占用\n調查圖台系統", style = AppThemeTokens.typography.screenTitle)
        AppTextField(state.account, { onEvent(AuthEvent.LoginAccountChanged(it)) }, "帳號", placeholder = "請輸入", isError = state.fieldErrors.containsKey(LoginField.ACCOUNT), supportingText = state.fieldErrors[LoginField.ACCOUNT], modifier = Modifier.testTag("login-account"))
        AppPasswordField(state.password, { onEvent(AuthEvent.LoginPasswordChanged(it)) }, "密碼", visible = false, onVisibilityChange = {}, placeholder = "請輸入", isError = state.fieldErrors.containsKey(LoginField.PASSWORD), supportingText = state.fieldErrors[LoginField.PASSWORD], modifier = Modifier.testTag("login-password"))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            AppTextField(state.captcha, { onEvent(AuthEvent.LoginCaptchaChanged(it)) }, "驗證碼", placeholder = "請輸入", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), isError = state.fieldErrors.containsKey(LoginField.CAPTCHA), supportingText = state.fieldErrors[LoginField.CAPTCHA], modifier = Modifier.weight(1f).testTag("login-captcha"))
            TextButton(onClick = { onEvent(AuthEvent.RefreshCaptcha) }, modifier = Modifier.testTag("captcha-refresh")) { Text("重整") }
        }
        state.captchaError?.let { Text(it, color = AppThemeTokens.colors.error, modifier = Modifier.testTag("captcha-error")) }
        state.requestError?.let { Text(it, color = AppThemeTokens.colors.error, modifier = Modifier.testTag("login-request-error")) }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            AppCheckboxRow(state.rememberMe, { onEvent(AuthEvent.RememberMeChanged(it)) }, "記住我")
            TextButton(onClick = { onEvent(AuthEvent.OpenRegister) }) { Text("沒有帳號? 註冊") }
        }
        AppPrimaryButton("登入", { onEvent(AuthEvent.SubmitLogin) }, enabled = !state.submitting, modifier = Modifier.fillMaxWidth().testTag("login-submit"))
    }
    }
}
