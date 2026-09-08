package com.example.tp_ncolso_android.feature.auth

class DebugAuthDataSource : AuthDataSource {
    override suspend fun login(account: String, password: String, captcha: String): Boolean =
        account == "sunrise000" && password.isNotBlank() && captcha == "0926"

    override suspend fun register(input: RegistrationInput): Boolean = true

    override suspend fun vendors(): List<VendorOption> = listOf(VendorOption("debug", "日陞"))
}

class DebugCaptchaProvider : CaptchaProvider {
    private var sequence = 0
    override suspend fun refresh(): String { sequence += 1; return "debug-$sequence" }
}
