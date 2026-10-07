package com.example.tp_ncolso_android.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme

private val previewVendors = listOf(VendorOption("preview", "日陞"))

@Preview(name = "Login empty", showBackground = true)
@Composable
private fun LoginEmptyPreview() = AppTheme { LoginScreen(LoginFormState(), {}) }

@Preview(name = "Login filled", showBackground = true)
@Composable
private fun LoginFilledPreview() = AppTheme {
    LoginScreen(LoginFormState(account = "sunrise000", password = "password", captcha = "0926", rememberMe = true), {})
}

@Preview(name = "Login auth error", showBackground = true)
@Composable
private fun LoginErrorPreview() = AppTheme {
    LoginScreen(LoginFormState(account = "sunrise000", password = "password", captcha = "0926", rememberMe = true, requestError = "帳號、密碼或驗證碼錯誤"), {})
}

@Preview(name = "Login submitting", showBackground = true)
@Composable
private fun LoginSubmittingPreview() = AppTheme { LoginScreen(LoginFormState(submitting = true), {}) }

@Preview(name = "Register empty", showBackground = true)
@Composable
private fun RegisterEmptyPreview() = AppTheme { RegisterScreen(RegisterFormState(), previewVendors, {}) }

@Preview(name = "Register filled", showBackground = true)
@Composable
private fun RegisterFilledPreview() = AppTheme {
    RegisterScreen(RegisterFormState(account = "sunrise1234", password = "sfk;wfj1~", confirmPassword = "sfk;wfj1~", vendor = previewVendors.first(), workType = WorkType.FIELD, name = "劉大君"), previewVendors, {})
}

@Preview(name = "Register validation error", showBackground = true)
@Composable
private fun RegisterValidationErrorPreview() = AppTheme {
    RegisterScreen(RegisterFormState(fieldErrors = validateRegister(RegisterFormState())), previewVendors, {})
}

@Preview(name = "Register submitting", showBackground = true)
@Composable
private fun RegisterSubmittingPreview() = AppTheme { RegisterScreen(RegisterFormState(submitting = true), previewVendors, {}) }
