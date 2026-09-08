package com.example.tp_ncolso_android.feature.auth

private val accountPattern = Regex("^[A-Za-z0-9]{1,30}$")
private val chineseNamePattern = Regex("^[\\u4E00-\\u9FFF]+$")
private val captchaPattern = Regex("^\\d{4}$")

fun validateLogin(state: LoginFormState): Map<LoginField, String> = buildMap {
    if (state.account.isBlank()) put(LoginField.ACCOUNT, "請輸入帳號")
    if (state.password.isBlank()) put(LoginField.PASSWORD, "請輸入密碼")
    if (!captchaPattern.matches(state.captcha)) put(LoginField.CAPTCHA, "請輸入 4 碼數字驗證碼")
}

fun validateRegister(state: RegisterFormState): Map<RegisterField, String> = buildMap {
    if (!accountPattern.matches(state.account)) put(RegisterField.ACCOUNT, "帳號限英數且最多 30 字元")
    if (state.password.length < 8) put(RegisterField.PASSWORD, "密碼至少 8 碼")
    if (state.confirmPassword.isBlank() || state.confirmPassword != state.password) put(RegisterField.CONFIRM_PASSWORD, "確認密碼不一致")
    if (state.vendor == null) put(RegisterField.VENDOR, "請選擇廠商名稱")
    if (state.workType == null) put(RegisterField.WORK_TYPE, "請選擇作業性質")
    if (!chineseNamePattern.matches(state.name)) put(RegisterField.NAME, "姓名須為中文")
}
