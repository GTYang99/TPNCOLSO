package com.example.tp_ncolso_android.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val dataSource: AuthDataSource,
    private val captchaProvider: CaptchaProvider,
) : ViewModel() {
    private val mutableState = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = mutableState.asStateFlow()
    private val effects = Channel<AuthEffect>(capacity = Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    init { loadVendors() }

    fun onEvent(event: AuthEvent) {
        when (event) {
            AuthEvent.OpenRegister -> mutableState.value = mutableState.value.copy(screen = AuthScreen.Register)
            AuthEvent.BackToLogin, AuthEvent.CancelRegister -> resetToLogin()
            AuthEvent.SubmitLogin -> submitLogin()
            AuthEvent.SubmitRegister -> submitRegister()
            AuthEvent.RefreshCaptcha -> refreshCaptcha()
            AuthEvent.LogoutRequested -> { resetToLogin(); effects.trySend(AuthEffect.LogoutRequested) }
            is AuthEvent.LoginAccountChanged -> updateLogin { copy(account = event.value, requestError = null) }
            is AuthEvent.LoginPasswordChanged -> updateLogin { copy(password = event.value, requestError = null) }
            is AuthEvent.LoginCaptchaChanged -> updateLogin { copy(captcha = event.value, requestError = null) }
            is AuthEvent.RememberMeChanged -> updateLogin { copy(rememberMe = event.value) }
            is AuthEvent.RegisterAccountChanged -> updateRegister { copy(account = event.value) }
            is AuthEvent.RegisterPasswordChanged -> updateRegister { copy(password = event.value) }
            is AuthEvent.RegisterConfirmChanged -> updateRegister { copy(confirmPassword = event.value) }
            is AuthEvent.VendorSelected -> updateRegister { copy(vendor = event.value) }
            is AuthEvent.WorkTypeSelected -> updateRegister { copy(workType = event.value) }
            is AuthEvent.RegisterNameChanged -> updateRegister { copy(name = event.value) }
        }
    }

    fun resetToLogin() { mutableState.value = AuthUiState(vendors = mutableState.value.vendors) }

    private fun submitLogin() {
        val current = mutableState.value.login
        if (current.submitting) return
        val errors = validateLogin(current)
        if (errors.isNotEmpty()) { updateLogin { copy(fieldErrors = errors) }; return }
        updateLogin { copy(submitting = true, fieldErrors = emptyMap(), requestError = null) }
        viewModelScope.launch {
            val success = dataSource.login(current.account, current.password, current.captcha)
            if (success) { resetToLogin(); effects.send(AuthEffect.LoginSucceeded) }
            else updateLogin { copy(submitting = false, requestError = "帳號、密碼或驗證碼錯誤") }
        }
    }

    private fun submitRegister() {
        val current = mutableState.value.register
        if (current.submitting) return
        val errors = validateRegister(current)
        if (errors.isNotEmpty()) { updateRegister { copy(fieldErrors = errors) }; return }
        updateRegister { copy(submitting = true, fieldErrors = emptyMap()) }
        viewModelScope.launch {
            val success = dataSource.register(RegistrationInput(current.account, current.password, current.confirmPassword, current.vendor!!, current.workType!!, current.name))
            if (success) { resetToLogin(); effects.send(AuthEffect.RegistrationSucceeded) }
            else updateRegister { copy(submitting = false) }
        }
    }

    private fun refreshCaptcha() {
        viewModelScope.launch {
            runCatching { captchaProvider.refresh() }
                .onSuccess { imageKey -> updateLogin { copy(captchaImageKey = imageKey, captchaError = null) } }
                .onFailure { updateLogin { copy(captchaError = "驗證碼更新失敗，請重試") } }
        }
    }
    private fun loadVendors() { viewModelScope.launch { mutableState.value = mutableState.value.copy(vendors = dataSource.vendors()) } }
    private fun updateLogin(change: LoginFormState.() -> LoginFormState) { mutableState.value = mutableState.value.copy(login = mutableState.value.login.change()) }
    private fun updateRegister(change: RegisterFormState.() -> RegisterFormState) { mutableState.value = mutableState.value.copy(register = mutableState.value.register.change()) }
}
