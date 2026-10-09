package com.example.tp_ncolso_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme

/** Debug-only fixed-identity host for stateful UI route recreation checks. */
class Ui009StatefulFlowTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                DebugSignedInContent(
                    identity = AppIdentity("測試人員", AppRole.INVESTIGATOR),
                    onLogout = ::finish,
                )
            }
        }
    }
}
