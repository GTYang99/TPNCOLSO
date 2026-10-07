package com.example.tp_ncolso_android

import com.example.tp_ncolso_android.feature.auth.AuthDataSource
import com.example.tp_ncolso_android.feature.auth.AuthEvent
import com.example.tp_ncolso_android.feature.auth.AuthScreen
import com.example.tp_ncolso_android.feature.auth.AuthViewModel
import com.example.tp_ncolso_android.feature.auth.CaptchaProvider
import com.example.tp_ncolso_android.feature.auth.RegistrationInput
import com.example.tp_ncolso_android.feature.auth.VendorOption
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.session.AppSessionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DebugAuthSessionCoordinatorTest {
    @Before fun setUp() { Dispatchers.setMain(Dispatchers.Unconfined) }
    @After fun tearDown() { Dispatchers.resetMain() }

    @Test fun loginIsGuardedAndLogoutClearsSessionAndAuth() {
        val owner = DebugSessionOwner()
        val viewModel = AuthViewModel(NoopSource(), NoopCaptcha())
        val coordinator = DebugAuthSessionCoordinator(owner, viewModel)
        coordinator.login(AppRole.INVESTIGATOR)
        coordinator.login(AppRole.ADMINISTRATOR)
        assertEquals(AppRole.INVESTIGATOR, (owner.state.value as AppSessionState.SignedIn).identity.role)
        coordinator.logout()
        assertEquals(AppSessionState.SignedOut, owner.state.value)
        assertEquals("", viewModel.state.value.login.account)
    }

    @Test fun ui003LogoutRequestedFixtureClearsBothStatesAndIsIdempotent() {
        val owner = DebugSessionOwner()
        val viewModel = AuthViewModel(NoopSource(), NoopCaptcha())
        val coordinator = DebugAuthSessionCoordinator(owner, viewModel)

        viewModel.onEvent(AuthEvent.LoginAccountChanged("old-account"))
        viewModel.onEvent(AuthEvent.LoginPasswordChanged("old-password"))
        viewModel.onEvent(AuthEvent.LoginCaptchaChanged("1234"))
        viewModel.onEvent(AuthEvent.RememberMeChanged(true))
        viewModel.onEvent(AuthEvent.OpenRegister)
        viewModel.onEvent(AuthEvent.RegisterAccountChanged("old-register"))
        viewModel.onEvent(AuthEvent.RegisterPasswordChanged("old-register-password"))
        viewModel.onEvent(AuthEvent.RegisterConfirmChanged("old-register-password"))
        viewModel.onEvent(AuthEvent.VendorSelected(VendorOption("vendor", "測試廠商")))
        viewModel.onEvent(AuthEvent.WorkTypeSelected(com.example.tp_ncolso_android.feature.auth.WorkType.FIELD))
        viewModel.onEvent(AuthEvent.RegisterNameChanged("測試人員"))
        coordinator.login(AppRole.INVESTIGATOR)

        // Contract fixture: UI-003's single LogoutRequested callback delegates here.
        val logoutRequested: () -> Unit = coordinator::logout
        logoutRequested()
        logoutRequested()

        assertEquals(AppSessionState.SignedOut, owner.state.value)
        assertEquals(AuthScreen.Login, viewModel.state.value.screen)
        assertEquals("", viewModel.state.value.login.account)
        assertEquals("", viewModel.state.value.login.password)
        assertEquals("", viewModel.state.value.login.captcha)
        assertEquals(false, viewModel.state.value.login.rememberMe)
        assertEquals("", viewModel.state.value.register.account)
        assertEquals("", viewModel.state.value.register.password)
        assertEquals("", viewModel.state.value.register.confirmPassword)
        assertEquals(null, viewModel.state.value.register.vendor)
        assertEquals(null, viewModel.state.value.register.workType)
        assertEquals("", viewModel.state.value.register.name)
    }

    private class NoopCaptcha : CaptchaProvider { override suspend fun refresh() = "fixture" }
    private class NoopSource : AuthDataSource {
        override suspend fun login(account: String, password: String, captcha: String) = false
        override suspend fun register(input: RegistrationInput) = false
        override suspend fun vendors() = listOf(VendorOption("test", "測試廠商"))
    }
}
