package com.example.tp_ncolso_android.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppRadioGroup
import com.example.tp_ncolso_android.ui.foundation.component.AppSecondaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppSelectField
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Composable
fun RegisterScreen(state: RegisterFormState, vendors: List<VendorOption>, onEvent: (AuthEvent) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().background(AppThemeTokens.colors.surface).padding(24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            androidx.compose.material3.TextButton(onClick = { onEvent(AuthEvent.BackToLogin) }) { Text("返回登入頁") }
            Text("註冊")
        }
        AppTextField(state.account, { onEvent(AuthEvent.RegisterAccountChanged(it)) }, "帳號", placeholder = "請輸入", isError = state.fieldErrors.containsKey(RegisterField.ACCOUNT), supportingText = state.fieldErrors[RegisterField.ACCOUNT], modifier = Modifier.testTag("register-account"))
        AppTextField(state.password, { onEvent(AuthEvent.RegisterPasswordChanged(it)) }, "密碼", placeholder = "請輸入", isError = state.fieldErrors.containsKey(RegisterField.PASSWORD), supportingText = state.fieldErrors[RegisterField.PASSWORD])
        AppTextField(state.confirmPassword, { onEvent(AuthEvent.RegisterConfirmChanged(it)) }, "確認密碼", placeholder = "請輸入", isError = state.fieldErrors.containsKey(RegisterField.CONFIRM_PASSWORD), supportingText = state.fieldErrors[RegisterField.CONFIRM_PASSWORD])
        AppSelectField(state.vendor, vendors, VendorOption::displayName, "廠商名稱", { onEvent(AuthEvent.VendorSelected(it)) }, isError = state.fieldErrors.containsKey(RegisterField.VENDOR))
        AppRadioGroup(WorkType.entries, state.workType, { onEvent(AuthEvent.WorkTypeSelected(it)) }, { if (it == WorkType.FIELD) "外業人員" else "內業人員" }, "作業性質")
        state.fieldErrors[RegisterField.WORK_TYPE]?.let { Text(it, color = AppThemeTokens.colors.error, modifier = Modifier.testTag("register-work-type-error")) }
        AppTextField(state.name, { onEvent(AuthEvent.RegisterNameChanged(it)) }, "姓名(請輸入真實姓名)", placeholder = "請輸入", isError = state.fieldErrors.containsKey(RegisterField.NAME), supportingText = state.fieldErrors[RegisterField.NAME])
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            AppSecondaryButton("取消", { onEvent(AuthEvent.CancelRegister) }, modifier = Modifier.weight(1f))
            AppPrimaryButton("完成", { onEvent(AuthEvent.SubmitRegister) }, enabled = !state.submitting, modifier = Modifier.weight(1f).testTag("register-submit"))
        }
    }
}
