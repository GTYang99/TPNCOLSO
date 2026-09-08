package com.example.tp_ncolso_android.feature.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AuthRoute(viewModel: AuthViewModel, onLoginSucceeded: () -> Unit = {}, onRegistrationSucceeded: () -> Unit = {}, onLogoutRequested: () -> Unit = {}) {
    val state by viewModel.state.collectAsState()
    androidx.compose.runtime.LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AuthEffect.LoginSucceeded -> onLoginSucceeded()
                AuthEffect.RegistrationSucceeded -> onRegistrationSucceeded()
                AuthEffect.LogoutRequested -> onLogoutRequested()
            }
        }
    }
    when (state.screen) {
        AuthScreen.Login -> LoginScreen(state.login, viewModel::onEvent)
        AuthScreen.Register -> RegisterScreen(state.register, state.vendors, viewModel::onEvent)
    }
}
