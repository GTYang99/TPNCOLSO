package com.example.tp_ncolso_android.session

import com.example.tp_ncolso_android.DebugSessionOwner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DebugSessionOwnerTest {
    @Test
    fun startsEveryDebugRoleWithoutTokenOrCredentials() {
        AppRole.entries.forEach { role ->
            val owner = DebugSessionOwner()

            owner.startDebugSession(role)

            val state = owner.state.value
            assertTrue(state is AppSessionState.SignedIn)
            val identity = (state as AppSessionState.SignedIn).identity
            assertEquals("開發測試人員", identity.displayName)
            assertEquals(role, identity.role)
            assertFalse(identity.toString().contains("token", ignoreCase = true))
            assertFalse(identity.toString().contains("password", ignoreCase = true))
        }
    }

    @Test
    fun ignoresRepeatedLoginWhenAlreadySignedIn() {
        val owner = DebugSessionOwner()

        owner.startDebugSession(AppRole.INVESTIGATOR)
        owner.startDebugSession(AppRole.ADMINISTRATOR)

        val identity = (owner.state.value as AppSessionState.SignedIn).identity
        assertEquals(AppRole.INVESTIGATOR, identity.role)
    }

    @Test
    fun clearReturnsToSignedOut() {
        val owner = DebugSessionOwner()

        owner.startDebugSession(AppRole.INTERNAL_STAFF)
        owner.clear()

        assertEquals(AppSessionState.SignedOut, owner.state.value)
    }
}
