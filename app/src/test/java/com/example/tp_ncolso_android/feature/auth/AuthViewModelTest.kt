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
        val viewModel = AuthViewModel(source)
        viewModel.onEvent(AuthEvent.SubmitLogin)
        advanceUntilIdle()
        assertEquals(0, source.loginCalls)
        assertTrue(viewModel.state.value.login.fieldErrors.isNotEmpty())
    }

    @Test fun successfulLoginEmitsOneEffectAndClearsSensitiveState() = runTest(StandardTestDispatcher()) {
        val source = RecordingAuthSource(loginResult = true)
        val viewModel = AuthViewModel(source)
        viewModel.onEvent(AuthEvent.LoginAccountChanged("sunrise000"))
        viewModel.onEvent(AuthEvent.LoginPasswordChanged("password"))
        viewModel.onEvent(AuthEvent.SubmitLogin)
        advanceUntilIdle()
        assertEquals(1, source.loginCalls)
        assertEquals(AuthEffect.LoginSucceeded, viewModel.effect.first())
        assertEquals("", viewModel.state.value.login.account)
        assertEquals("", viewModel.state.value.login.password)
    }

    @Test fun failedLoginRetainsAccountAndRememberMe() = runTest {
        val source = RecordingAuthSource(loginResult = false)
        val viewModel = AuthViewModel(source)
        viewModel.onEvent(AuthEvent.LoginAccountChanged("sunrise000"))
        viewModel.onEvent(AuthEvent.LoginPasswordChanged("password"))
        viewModel.onEvent(AuthEvent.RememberMeChanged(true))
        viewModel.onEvent(AuthEvent.SubmitLogin)
        advanceUntilIdle()
        assertEquals("sunrise000", viewModel.state.value.login.account)
        assertTrue(viewModel.state.value.login.rememberMe)
        assertEquals("帳號或密碼錯誤", viewModel.state.value.login.requestError)
    }

    @Test fun invalidRegisterDoesNotCallSource() = runTest {
        val source = RecordingAuthSource()
        val viewModel = AuthViewModel(source)
        viewModel.onEvent(AuthEvent.SubmitRegister)
        advanceUntilIdle()
        assertEquals(0, source.registerCalls)
        assertTrue(viewModel.state.value.register.fieldErrors.isNotEmpty())
    }

    @Test fun successfulRegisterEmitsOneEffectAndReturnsToEmptyLogin() = runTest(StandardTestDispatcher()) {
        val source = RecordingAuthSource()
        val viewModel = AuthViewModel(source)
        viewModel.onEvent(AuthEvent.OpenRegister)
        viewModel.onEvent(AuthEvent.RegisterAccountChanged("abc123"))
        viewModel.onEvent(AuthEvent.RegisterPasswordChanged("password"))
        viewModel.onEvent(AuthEvent.RegisterConfirmChanged("password"))
        viewModel.onEvent(AuthEvent.VendorSelected(VendorOption("v", "廠商")))
        viewModel.onEvent(AuthEvent.WorkTypeSelected(WorkType.FIELD))
        viewModel.onEvent(AuthEvent.RegisterNameChanged("王小明"))
        viewModel.onEvent(AuthEvent.SubmitRegister)
        viewModel.onEvent(AuthEvent.SubmitRegister)
        advanceUntilIdle()
        assertEquals(1, source.registerCalls)
        assertEquals(AuthEffect.RegistrationSucceeded, viewModel.effect.first())
        assertEquals(AuthScreen.Login, viewModel.state.value.screen)
        assertEquals("", viewModel.state.value.register.password)
        assertEquals("", viewModel.state.value.register.confirmPassword)
        assertEquals("", viewModel.state.value.login.account)
    }

    @Test fun logoutResetsAuthStateAndEmitsEffect() = runTest {
        val viewModel = AuthViewModel(RecordingAuthSource())
        viewModel.onEvent(AuthEvent.LoginAccountChanged("account"))
        viewModel.onEvent(AuthEvent.RememberMeChanged(true))
        viewModel.onEvent(AuthEvent.LoginPasswordVisibilityChanged(true))
        viewModel.onEvent(AuthEvent.LogoutRequested)
        assertEquals(AuthEffect.LogoutRequested, viewModel.effect.first())
        assertEquals(AuthScreen.Login, viewModel.state.value.screen)
        assertEquals("", viewModel.state.value.login.account)
        assertTrue(!viewModel.state.value.login.rememberMe)
        assertTrue(!viewModel.state.value.login.passwordVisible)
    }

    private class RecordingAuthSource(private val loginResult: Boolean = true) : AuthDataSource {
        var loginCalls = 0
        var registerCalls = 0
        override suspend fun login(account: String, password: String): Boolean { loginCalls++; return loginResult }
        override suspend fun register(input: RegistrationInput): Boolean { registerCalls++; return true }
        override suspend fun vendors() = emptyList<VendorOption>()
    }
}
