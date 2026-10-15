package com.example.tp_ncolso_android.feature.auth

import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidationTest {
    @Test fun loginRequiresOnlyAccountAndPassword() {
        val errors = validateLogin(LoginFormState())
        assertTrue(errors.containsKey(LoginField.ACCOUNT))
        assertTrue(errors.containsKey(LoginField.PASSWORD))
        assertTrue(validateLogin(LoginFormState(account = "a", password = "password")).isEmpty())
    }

    @Test fun registerRequiresChineseNameAndWorkType() {
        val errors = validateRegister(RegisterFormState(account = "abc", password = "password", confirmPassword = "password", vendor = VendorOption("v", "廠商"), name = "Alice"))
        assertTrue(errors.containsKey(RegisterField.WORK_TYPE))
        assertTrue(errors.containsKey(RegisterField.NAME))
    }

    @Test fun registerAcceptsValidBoundary() {
        val errors = validateRegister(RegisterFormState(account = "A".repeat(30), password = "password", confirmPassword = "password", vendor = VendorOption("v", "廠商"), workType = WorkType.FIELD, name = "王小明"))
        assertTrue(errors.isEmpty())
    }

    @Test fun registerRejectsAccountAndPasswordBoundaries() {
        val errors = validateRegister(
            RegisterFormState(
                account = "A".repeat(31) + "!",
                password = "short",
                confirmPassword = "different",
                vendor = VendorOption("v", "廠商"),
                workType = WorkType.FIELD,
                name = "王小明",
            ),
        )
        assertTrue(errors.containsKey(RegisterField.ACCOUNT))
        assertTrue(errors.containsKey(RegisterField.PASSWORD))
        assertTrue(errors.containsKey(RegisterField.CONFIRM_PASSWORD))
    }
}
