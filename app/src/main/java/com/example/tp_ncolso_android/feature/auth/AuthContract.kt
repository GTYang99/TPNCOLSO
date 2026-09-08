package com.example.tp_ncolso_android.feature.auth

data class LoginFormState(
    val account: String = "",
    val password: String = "",
    val captcha: String = "",
    val rememberMe: Boolean = false,
    val captchaImageKey: String = "default",
    val fieldErrors: Map<LoginField, String> = emptyMap(),
    val requestError: String? = null,
    val submitting: Boolean = false,
)

enum class LoginField { ACCOUNT, PASSWORD, CAPTCHA }

data class RegisterFormState(
    val account: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val vendor: VendorOption? = null,
    val workType: WorkType? = null,
    val name: String = "",
    val fieldErrors: Map<RegisterField, String> = emptyMap(),
    val submitting: Boolean = false,
)

enum class RegisterField { ACCOUNT, PASSWORD, CONFIRM_PASSWORD, VENDOR, WORK_TYPE, NAME }
enum class WorkType { FIELD, OFFICE }
data class VendorOption(val id: String, val displayName: String)

sealed interface AuthScreen { data object Login : AuthScreen; data object Register : AuthScreen }
data class AuthUiState(
    val screen: AuthScreen = AuthScreen.Login,
    val login: LoginFormState = LoginFormState(),
    val register: RegisterFormState = RegisterFormState(),
    val vendors: List<VendorOption> = emptyList(),
)

sealed interface AuthEffect {
    data object LoginSucceeded : AuthEffect
    data object RegistrationSucceeded : AuthEffect
    data object LogoutRequested : AuthEffect
}

sealed interface AuthEvent {
    data object OpenRegister : AuthEvent
    data object BackToLogin : AuthEvent
    data object CancelRegister : AuthEvent
    data object SubmitLogin : AuthEvent
    data object SubmitRegister : AuthEvent
    data object RefreshCaptcha : AuthEvent
    data object LogoutRequested : AuthEvent
    data class LoginAccountChanged(val value: String) : AuthEvent
    data class LoginPasswordChanged(val value: String) : AuthEvent
    data class LoginCaptchaChanged(val value: String) : AuthEvent
    data class RememberMeChanged(val value: Boolean) : AuthEvent
    data class RegisterAccountChanged(val value: String) : AuthEvent
    data class RegisterPasswordChanged(val value: String) : AuthEvent
    data class RegisterConfirmChanged(val value: String) : AuthEvent
    data class VendorSelected(val value: VendorOption) : AuthEvent
    data class WorkTypeSelected(val value: WorkType) : AuthEvent
    data class RegisterNameChanged(val value: String) : AuthEvent
}
