package com.example.tp_ncolso_android.feature.auth

import androidx.compose.runtime.Composable

@Composable
fun AuthHost(
    viewModel: AuthViewModel,
    onLoginSucceeded: () -> Unit,
    onRegistrationSucceeded: () -> Unit = {},
    onLogoutRequested: () -> Unit = {},
    captchaVisual: @Composable () -> Unit = {},
) {
    AuthRoute(
        viewModel = viewModel,
        onLoginSucceeded = onLoginSucceeded,
        onRegistrationSucceeded = onRegistrationSucceeded,
        onLogoutRequested = onLogoutRequested,
        captchaVisual = captchaVisual,
    )
}
