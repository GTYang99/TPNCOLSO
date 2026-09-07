package com.example.tp_ncolso_android.session

import kotlinx.coroutines.flow.StateFlow

enum class AppRole {
    INVESTIGATOR,
    INTERNAL_STAFF,
    ADMINISTRATOR,
}

data class AppIdentity(
    val displayName: String,
    val role: AppRole,
)

sealed interface AppSessionState {
    data object SignedOut : AppSessionState
    data class SignedIn(val identity: AppIdentity) : AppSessionState
}

interface AppSessionOwner {
    val state: StateFlow<AppSessionState>
    fun clear()
}
