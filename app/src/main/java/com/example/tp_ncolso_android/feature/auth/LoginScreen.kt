package com.example.tp_ncolso_android.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    captchaVisual: @Composable (Modifier) -> Unit = { modifier -> Text(state.captchaImageKey, color = AppThemeTokens.colors.textSecondary, modifier = modifier) },
) {
    Box(modifier.fillMaxSize().background(Brush.verticalGradient(listOf(AppThemeTokens.colors.surface, AppThemeTokens.colors.surfaceMuted)))) {
    val skyline = ImageBitmap.imageResource(com.example.tp_ncolso_android.R.drawable.login_city_skyline)
    Canvas(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = (-67).dp)
            .size(width = 402.dp, height = 239.dp),
    ) { drawImage(skyline, dstSize = IntSize(size.width.toInt(), size.height.toInt()), blendMode = BlendMode.Multiply) }
    Column(
        Modifier
            .width(320.dp)
            .align(Alignment.TopCenter)
            .padding(top = 161.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().semantics { heading() }) {
            Image(
                painter = painterResource(com.example.tp_ncolso_android.R.drawable.login_brand_logo),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(width = 96.dp, height = 65.dp),
            )
            Column {
                Text("新工處土地占用", style = AppThemeTokens.typography.screenTitle, maxLines = 1)
                Text(
                    "調查圖台系統",
                    style = AppThemeTokens.typography.screenTitle.copy(letterSpacing = 5.sp),
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                )
            }
        }
        androidx.compose.foundation.layout.Spacer(Modifier.height(25.dp))
        val authError = state.requestError != null
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            AppTextField(state.account, { onEvent(AuthEvent.LoginAccountChanged(it)) }, "帳號", placeholder = "請輸入", isError = authError || state.fieldErrors.containsKey(LoginField.ACCOUNT), supportingText = state.fieldErrors[LoginField.ACCOUNT], modifier = Modifier.testTag("login-account"))
            AppPasswordField(state.password, { onEvent(AuthEvent.LoginPasswordChanged(it)) }, "密碼", visible = false, onVisibilityChange = {}, placeholder = "請輸入", isError = authError || state.fieldErrors.containsKey(LoginField.PASSWORD), supportingText = state.fieldErrors[LoginField.PASSWORD], modifier = Modifier.testTag("login-password"))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                AppTextField(state.captcha, { onEvent(AuthEvent.LoginCaptchaChanged(it)) }, "驗證碼", placeholder = "請輸入", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), isError = authError || state.fieldErrors.containsKey(LoginField.CAPTCHA), supportingText = state.fieldErrors[LoginField.CAPTCHA], modifier = Modifier.width(133.dp).testTag("login-captcha"))
                Box(
                    modifier = Modifier
                        .width(139.dp)
                        .height(48.dp)
                        .semantics { contentDescription = "驗證碼圖片" }
                        .testTag("captcha-image"),
                    contentAlignment = Alignment.Center,
                ) { captchaVisual(Modifier.width(139.dp).height(48.dp)) }
                Box(
                    modifier = Modifier.width(32.dp).height(48.dp).testTag("captcha-refresh"),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .offset(x = (-8).dp)
                            .requiredSize(48.dp)
                            .clickable(role = Role.Button) { onEvent(AuthEvent.RefreshCaptcha) }
                            .semantics { contentDescription = "重新產生驗證碼" },
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(com.example.tp_ncolso_android.R.drawable.ic_reload),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
            }
            state.captchaError?.let { Text(it, color = AppThemeTokens.colors.error, modifier = Modifier.testTag("captcha-error")) }
            state.requestError?.let { Text(it, color = AppThemeTokens.colors.errorText, modifier = Modifier.testTag("login-request-error").semantics { error(it) }) }
        }
        androidx.compose.foundation.layout.Spacer(Modifier.height(21.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            AppCheckboxRow(state.rememberMe, { onEvent(AuthEvent.RememberMeChanged(it)) }, "記住我", modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clickable(role = Role.Button) { onEvent(AuthEvent.OpenRegister) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = AppThemeTokens.colors.textPrimary)) { append("沒有帳號? ") }
                        withStyle(SpanStyle(color = AppThemeTokens.colors.brandPrimary)) { append("註冊") }
                    },
                    style = AppThemeTokens.typography.body.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Medium),
                )
            }
        }
        androidx.compose.foundation.layout.Spacer(Modifier.height(19.dp))
        AppPrimaryButton("登入", { onEvent(AuthEvent.SubmitLogin) }, enabled = !state.submitting, modifier = Modifier.fillMaxWidth().testTag("login-submit"))
    }
    }
}
