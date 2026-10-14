package com.example.tp_ncolso_android.feature.auth

class DebugAuthDataSource : AuthDataSource {
    override suspend fun login(account: String, password: String): Boolean =
        account == "sunrise000" && password.isNotBlank()

    override suspend fun register(input: RegistrationInput): Boolean = true

    override suspend fun vendors(): List<VendorOption> = listOf(VendorOption("debug", "日陞"))
}
