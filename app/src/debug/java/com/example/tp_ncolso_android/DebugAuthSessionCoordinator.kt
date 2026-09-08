package com.example.tp_ncolso_android

import com.example.tp_ncolso_android.feature.auth.AuthViewModel
import com.example.tp_ncolso_android.session.AppRole

class DebugAuthSessionCoordinator(
    private val sessionOwner: DebugSessionController,
    private val authViewModel: AuthViewModel,
) {
    fun login(role: AppRole) {
        if (sessionOwner.state.value is com.example.tp_ncolso_android.session.AppSessionState.SignedOut) {
            sessionOwner.startDebugSession(role)
        }
    }

    fun logout() {
        sessionOwner.clear()
        authViewModel.resetToLogin()
    }
}
