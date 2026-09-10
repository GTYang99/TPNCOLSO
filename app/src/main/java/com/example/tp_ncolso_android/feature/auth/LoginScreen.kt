package com.example.tp_ncolso_android.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
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
fun LoginScreen(
    state: LoginFormState,
    onEvent: (AuthEvent) -> Unit,
    modifier: Modifier = Modifier,
    captchaVisual: @Composable () -> Unit = { Text(state.captchaImageKey, color = AppThemeTokens.colors.textSecondary) },
) {
    Box(modifier.fillMaxSize().background(Brush.verticalGradient(listOf(AppThemeTokens.colors.surface, AppThemeTokens.colors.surfaceMuted)))) {
    val skyline = ImageBitmap.imageResource(com.example.tp_ncolso_android.R.drawable.login_city_skyline)
    Canvas(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            // Figma's absolute left=-138dp maps to +29dp from the centered 736dp canvas.
            .offset(x = 29.dp, y = (-7).dp)
            .size(width = 736.dp, height = 246.dp),
    ) { drawImage(skyline, dstSize = IntSize(size.width.toInt(), size.height.toInt()), blendMode = BlendMode.Multiply) }
    Column(
        Modifier
            .width(320.dp)
            .align(Alignment.Center)
            .offset(y = (-47).dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(com.example.tp_ncolso_android.R.drawable.login_brand_logo),
                contentDescription = "品牌標誌",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(width = 96.dp, height = 65.dp),
            )
            Text(
                "新工處土地占用\n調查圖台系統",
                style = AppThemeTokens.typography.screenTitle,
                modifier = Modifier.weight(1f),
                softWrap = true,
            )
        }
        val authError = state.requestError != null
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            AppTextField(state.account, { onEvent(AuthEvent.LoginAccountChanged(it)) }, "帳號", placeholder = "請輸入", isError = authError || state.fieldErrors.containsKey(LoginField.ACCOUNT), supportingText = state.fieldErrors[LoginField.ACCOUNT], modifier = Modifier.testTag("login-account"))
            AppPasswordField(state.password, { onEvent(AuthEvent.LoginPasswordChanged(it)) }, "密碼", visible = false, onVisibilityChange = {}, placeholder = "請輸入", isError = authError || state.fieldErrors.containsKey(LoginField.PASSWORD), supportingText = state.fieldErrors[LoginField.PASSWORD], modifier = Modifier.testTag("login-password"))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                AppTextField(state.captcha, { onEvent(AuthEvent.LoginCaptchaChanged(it)) }, "驗證碼", placeholder = "請輸入", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), isError = authError || state.fieldErrors.containsKey(LoginField.CAPTCHA), supportingText = state.fieldErrors[LoginField.CAPTCHA], modifier = Modifier.weight(1f).testTag("login-captcha"))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .semantics { contentDescription = "驗證碼圖片" }
                        .testTag("captcha-image"),
                    contentAlignment = Alignment.Center,
                ) { captchaVisual() }
                TextButton(onClick = { onEvent(AuthEvent.RefreshCaptcha) }, modifier = Modifier.testTag("captcha-refresh")) { Text("重整") }
            }
            state.captchaError?.let { Text(it, color = AppThemeTokens.colors.error, modifier = Modifier.testTag("captcha-error")) }
            state.requestError?.let { Text(it, color = AppThemeTokens.colors.error, modifier = Modifier.testTag("login-request-error").semantics { error(it) }) }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            AppCheckboxRow(state.rememberMe, { onEvent(AuthEvent.RememberMeChanged(it)) }, "記住我", modifier = Modifier.weight(1f))
            TextButton(onClick = { onEvent(AuthEvent.OpenRegister) }) { Text("沒有帳號? 註冊") }
        }
        AppPrimaryButton("登入", { onEvent(AuthEvent.SubmitLogin) }, enabled = !state.submitting, modifier = Modifier.fillMaxWidth().testTag("login-submit"))
    }
    }
}
