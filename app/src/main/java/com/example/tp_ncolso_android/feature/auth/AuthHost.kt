package com.example.tp_ncolso_android.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AuthHost(
    viewModel: AuthViewModel,
    onLoginSucceeded: () -> Unit,
    onRegistrationSucceeded: () -> Unit = {},
    onLogoutRequested: () -> Unit = {},
    captchaVisual: @Composable (Modifier) -> Unit = {},
) {
    AuthRoute(
        viewModel = viewModel,
        onLoginSucceeded = onLoginSucceeded,
        onRegistrationSucceeded = onRegistrationSucceeded,
        onLogoutRequested = onLogoutRequested,
        captchaVisual = captchaVisual,
    )
}
