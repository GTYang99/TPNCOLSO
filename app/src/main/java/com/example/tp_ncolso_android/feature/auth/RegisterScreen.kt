package com.example.tp_ncolso_android.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppRadioGroup
import com.example.tp_ncolso_android.ui.foundation.component.AppSecondaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppSelectField
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Composable
fun RegisterScreen(state: RegisterFormState, vendors: List<VendorOption>, onEvent: (AuthEvent) -> Unit, modifier: Modifier = Modifier, illustration: @Composable () -> Unit = { Image(painterResource(com.example.tp_ncolso_android.R.drawable.register_user_illustration), contentDescription = "註冊插圖", modifier = Modifier.size(80.dp)) }) {
    val registerLabelStyle = AppThemeTokens.typography.fieldLabel.copy(fontWeight = FontWeight.Medium)
    Column(modifier.fillMaxWidth().background(AppThemeTokens.colors.surface).padding(24.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(36.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(48.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            TextButton(onClick = { onEvent(AuthEvent.BackToLogin) }, modifier = Modifier.size(48.dp)) {
                val arrowColor = AppThemeTokens.colors.brandPrimary
                Canvas(Modifier.size(24.dp).semantics { contentDescription = "返回登入頁" }) {
                    val stroke = 2.5.dp.toPx()
                    drawLine(arrowColor, androidx.compose.ui.geometry.Offset(16.dp.toPx(), 4.dp.toPx()), androidx.compose.ui.geometry.Offset(8.dp.toPx(), 12.dp.toPx()), stroke, cap = StrokeCap.Round)
                    drawLine(arrowColor, androidx.compose.ui.geometry.Offset(8.dp.toPx(), 12.dp.toPx()), androidx.compose.ui.geometry.Offset(16.dp.toPx(), 20.dp.toPx()), stroke, cap = StrokeCap.Round)
                }
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("註冊", style = AppThemeTokens.typography.sectionTitle.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 28.sp, color = AppThemeTokens.colors.textPrimary))
            }
            Spacer(modifier = Modifier.size(48.dp))
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) { illustration() }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        AppTextField(state.account, { onEvent(AuthEvent.RegisterAccountChanged(it)) }, "帳號", placeholder = "請輸入", isError = state.fieldErrors.containsKey(RegisterField.ACCOUNT), supportingText = state.fieldErrors[RegisterField.ACCOUNT], labelStyle = registerLabelStyle, modifier = Modifier.testTag("register-account"))
        AppTextField(state.password, { onEvent(AuthEvent.RegisterPasswordChanged(it)) }, "密碼", placeholder = "請輸入", isError = state.fieldErrors.containsKey(RegisterField.PASSWORD), supportingText = state.fieldErrors[RegisterField.PASSWORD], labelStyle = registerLabelStyle)
        AppTextField(state.confirmPassword, { onEvent(AuthEvent.RegisterConfirmChanged(it)) }, "確認密碼", placeholder = "請輸入", isError = state.fieldErrors.containsKey(RegisterField.CONFIRM_PASSWORD), supportingText = state.fieldErrors[RegisterField.CONFIRM_PASSWORD], labelStyle = registerLabelStyle)
        AppSelectField(state.vendor, vendors, VendorOption::displayName, "廠商名稱", { onEvent(AuthEvent.VendorSelected(it)) }, isError = state.fieldErrors.containsKey(RegisterField.VENDOR), supportingText = state.fieldErrors[RegisterField.VENDOR], labelStyle = registerLabelStyle)
        AppRadioGroup(WorkType.entries, state.workType, { onEvent(AuthEvent.WorkTypeSelected(it)) }, { if (it == WorkType.FIELD) "外業人員" else "內業人員" }, "作業性質")
        state.fieldErrors[RegisterField.WORK_TYPE]?.let { Text(it, color = AppThemeTokens.colors.error, modifier = Modifier.testTag("register-work-type-error")) }
        AppTextField(state.name, { onEvent(AuthEvent.RegisterNameChanged(it)) }, "姓名(請輸入真實姓名)", placeholder = "請輸入", isError = state.fieldErrors.containsKey(RegisterField.NAME), supportingText = state.fieldErrors[RegisterField.NAME], labelStyle = registerLabelStyle)
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            AppSecondaryButton("取消", { onEvent(AuthEvent.CancelRegister) }, modifier = Modifier.weight(1f))
            AppPrimaryButton("完成", { onEvent(AuthEvent.SubmitRegister) }, enabled = !state.submitting, modifier = Modifier.weight(1f).testTag("register-submit"))
        }
        }
    }
}
