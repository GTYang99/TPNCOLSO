package com.example.tp_ncolso_android.feature.auth

interface AuthDataSource {
    suspend fun login(account: String, password: String, captcha: String): Boolean
    suspend fun register(input: RegistrationInput): Boolean
    suspend fun vendors(): List<VendorOption>
}

data class RegistrationInput(
    val account: String,
    val password: String,
    val confirmPassword: String,
    val vendor: VendorOption,
    val workType: WorkType,
    val name: String,
)

interface CaptchaProvider { suspend fun refresh(): String }
