package com.example.tp_ncolso_android.feature.auth

import org.junit.Assert.assertTrue
import org.junit.Test

class AuthValidationTest {
    @Test fun loginRequiresFourDigitCaptcha() {
        val errors = validateLogin(LoginFormState(account = "a", password = "password", captcha = "12"))
        assertTrue(errors.containsKey(LoginField.CAPTCHA))
    }

    @Test fun registerRequiresChineseNameAndWorkType() {
        val errors = validateRegister(RegisterFormState(account = "abc", password = "password", confirmPassword = "password", vendor = VendorOption("v", "廠商"), name = "Alice"))
        assertTrue(errors.containsKey(RegisterField.WORK_TYPE))
        assertTrue(errors.containsKey(RegisterField.NAME))
    }

    @Test fun registerAcceptsValidBoundary() {
        val errors = validateRegister(RegisterFormState(account = "A1", password = "password", confirmPassword = "password", vendor = VendorOption("v", "廠商"), workType = WorkType.FIELD, name = "王小明"))
        assertTrue(errors.isEmpty())
    }
}
