package com.example.tp_ncolso_android

import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.session.AppSessionOwner

interface DebugSessionController : AppSessionOwner {
    fun startDebugSession(role: AppRole)
}
