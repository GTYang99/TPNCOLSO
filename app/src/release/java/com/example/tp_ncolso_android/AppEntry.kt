package com.example.tp_ncolso_android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tp_ncolso_android.session.AppSessionState
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Composable
fun AppEntry() {
    AppRoot(
        sessionState = AppSessionState.SignedOut,
        signedOutContent = { ReleaseSignedOutContent() },
        signedInContent = {},
    )
}

@Composable
private fun ReleaseSignedOutContent(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(AppThemeTokens.spacing.lg)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.md),
        ) {
            Text("尚未登入", style = AppThemeTokens.typography.screenTitle)
            Text("正式登入功能將由認證模組接入。", style = AppThemeTokens.typography.body)
        }
    }
}
