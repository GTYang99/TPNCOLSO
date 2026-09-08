package com.example.tp_ncolso_android

import com.example.tp_ncolso_android.feature.auth.AuthDataSource
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

    private class NoopCaptcha : CaptchaProvider { override suspend fun refresh() = "fixture" }
    private class NoopSource : AuthDataSource {
        override suspend fun login(account: String, password: String, captcha: String) = false
        override suspend fun register(input: RegistrationInput) = false
        override suspend fun vendors() = listOf(VendorOption("test", "測試廠商"))
    }
}
