package com.example.tp_ncolso_android.feature.auth

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    @Before fun setUp() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test fun invalidLoginDoesNotCallSource() = runTest {
        val source = RecordingAuthSource()
        val viewModel = AuthViewModel(source, FixedCaptchaProvider())
        viewModel.onEvent(AuthEvent.SubmitLogin)
        advanceUntilIdle()
        assertEquals(0, source.loginCalls)
        assertTrue(viewModel.state.value.login.fieldErrors.isNotEmpty())
    }

    @Test fun successfulLoginEmitsOneEffectAndClearsSensitiveState() = runTest(StandardTestDispatcher()) {
        val source = RecordingAuthSource(loginResult = true)
        val viewModel = AuthViewModel(source, FixedCaptchaProvider())
        viewModel.onEvent(AuthEvent.LoginAccountChanged("sunrise000"))
        viewModel.onEvent(AuthEvent.LoginPasswordChanged("password"))
        viewModel.onEvent(AuthEvent.LoginCaptchaChanged("0926"))
        viewModel.onEvent(AuthEvent.SubmitLogin)
        advanceUntilIdle()
        assertEquals(1, source.loginCalls)
        assertEquals(AuthEffect.LoginSucceeded, viewModel.effect.first())
        assertEquals("", viewModel.state.value.login.account)
        assertEquals("", viewModel.state.value.login.password)
        assertEquals("", viewModel.state.value.login.captcha)
    }

    @Test fun failedLoginRetainsAccountAndRememberMe() = runTest {
        val source = RecordingAuthSource(loginResult = false)
        val viewModel = AuthViewModel(source, FixedCaptchaProvider())
        viewModel.onEvent(AuthEvent.LoginAccountChanged("sunrise000"))
        viewModel.onEvent(AuthEvent.LoginPasswordChanged("password"))
        viewModel.onEvent(AuthEvent.LoginCaptchaChanged("0926"))
        viewModel.onEvent(AuthEvent.RememberMeChanged(true))
        viewModel.onEvent(AuthEvent.SubmitLogin)
        advanceUntilIdle()
        assertEquals("sunrise000", viewModel.state.value.login.account)
        assertTrue(viewModel.state.value.login.rememberMe)
        assertEquals("帳號、密碼或驗證碼錯誤", viewModel.state.value.login.requestError)
    }

    @Test fun logoutResetsAuthStateAndEmitsEffect() = runTest {
        val viewModel = AuthViewModel(RecordingAuthSource(), FixedCaptchaProvider())
        viewModel.onEvent(AuthEvent.LoginAccountChanged("account"))
        viewModel.onEvent(AuthEvent.RememberMeChanged(true))
        viewModel.onEvent(AuthEvent.LogoutRequested)
        assertEquals(AuthEffect.LogoutRequested, viewModel.effect.first())
        assertEquals(AuthScreen.Login, viewModel.state.value.screen)
        assertEquals("", viewModel.state.value.login.account)
        assertTrue(!viewModel.state.value.login.rememberMe)
    }

    @Test fun captchaRefreshFailureRetainsFormAndExposesRetryError() = runTest {
        val viewModel = AuthViewModel(RecordingAuthSource(), FailingCaptchaProvider())
        viewModel.onEvent(AuthEvent.LoginAccountChanged("account"))
        viewModel.onEvent(AuthEvent.RememberMeChanged(true))
        viewModel.onEvent(AuthEvent.RefreshCaptcha)
        advanceUntilIdle()
        assertEquals("account", viewModel.state.value.login.account)
        assertTrue(viewModel.state.value.login.rememberMe)
        assertEquals("驗證碼更新失敗，請重試", viewModel.state.value.login.captchaError)
    }

    private class FixedCaptchaProvider : CaptchaProvider { override suspend fun refresh() = "next" }
    private class FailingCaptchaProvider : CaptchaProvider { override suspend fun refresh(): String = error("fixture failure") }

    private class RecordingAuthSource(private val loginResult: Boolean = true) : AuthDataSource {
        var loginCalls = 0
        override suspend fun login(account: String, password: String, captcha: String): Boolean { loginCalls++; return loginResult }
        override suspend fun register(input: RegistrationInput) = true
        override suspend fun vendors() = emptyList<VendorOption>()
    }
}
