package com.example.tp_ncolso_android

import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.session.AppSessionOwner
import com.example.tp_ncolso_android.session.AppSessionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DebugSessionOwner : AppSessionOwner {
    private val mutableState = MutableStateFlow<AppSessionState>(AppSessionState.SignedOut)

    override val state: StateFlow<AppSessionState> = mutableState

    fun startDebugSession(role: AppRole) {
        if (mutableState.value is AppSessionState.SignedIn) return
        mutableState.value = AppSessionState.SignedIn(
            AppIdentity(
                displayName = "開發測試人員",
                role = role,
            ),
        )
    }

    override fun clear() {
        mutableState.value = AppSessionState.SignedOut
    }
}
