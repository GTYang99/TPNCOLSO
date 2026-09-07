package com.example.tp_ncolso_android

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppSessionState

@Composable
fun AppRoot(
    sessionState: AppSessionState,
    signedOutContent: @Composable () -> Unit,
    signedInContent: @Composable (AppIdentity) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (sessionState) {
            AppSessionState.SignedOut -> signedOutContent()
            is AppSessionState.SignedIn -> signedInContent(sessionState.identity)
        }
    }
}
