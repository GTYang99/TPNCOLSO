package com.example.tp_ncolso_android.ui.foundation.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tp_ncolso_android.ui.foundation.component.AppEmptyContent
import com.example.tp_ncolso_android.ui.foundation.component.AppErrorContent
import com.example.tp_ncolso_android.ui.foundation.component.AppCheckboxRow
import com.example.tp_ncolso_android.ui.foundation.component.AppLoadingContent
import com.example.tp_ncolso_android.ui.foundation.component.AppPasswordField
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppRadioGroup
import com.example.tp_ncolso_android.ui.foundation.component.AppReadOnlyField
import com.example.tp_ncolso_android.ui.foundation.component.AppSecondaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Preview(showBackground = true, name = "Foundation Default")
@Composable
private fun FoundationDefaultPreview() {
    AppTheme {
        FoundationPreviewColumn {
            AppTextField(value = "測試帳號", onValueChange = {}, label = "帳號", required = true)
            AppPasswordField(value = "secret", onValueChange = {}, label = "密碼", visible = false, onVisibilityChange = {})
            AppCheckboxRow(checked = true, onCheckedChange = {}, label = "記住我")
            AppRadioGroup(
                options = listOf("調查人員", "內業人員", "管理者"),
                selected = "調查人員",
                onSelect = {},
                itemLabel = { it },
                label = "角色",
            )
            AppPrimaryButton(text = "主要動作", onClick = {}, modifier = Modifier.fillMaxWidth())
            AppSecondaryButton(text = "次要動作", onClick = {}, modifier = Modifier.fillMaxWidth())
            AppReadOnlyField(label = "唯讀欄位", value = "已核定資料")
        }
    }
}

@Preview(showBackground = true, name = "Foundation States")
@Composable
private fun FoundationStatesPreview() {
    AppTheme {
        FoundationPreviewColumn {
            AppTextField(value = "", onValueChange = {}, label = "錯誤欄位", isError = true, supportingText = "請確認輸入內容")
            AppTextField(value = "不可編輯", onValueChange = {}, label = "唯讀欄位", readOnly = true)
            AppPasswordField(
                value = "secret",
                onValueChange = {},
                label = "註冊密碼",
                visible = false,
                onVisibilityChange = {},
                visibilityActionEnabled = false,
            )
            AppCheckboxRow(checked = false, onCheckedChange = {}, label = "停用選項", enabled = false)
            AppPrimaryButton(text = "載入中", onClick = {}, loading = true, modifier = Modifier.fillMaxWidth())
            AppLoadingContent(message = "載入資料中")
            AppEmptyContent(title = "目前沒有資料", body = "完成查詢後會顯示結果。")
            AppErrorContent(message = "資料載入失敗", retry = {})
        }
    }
}

@Composable
private fun FoundationPreviewColumn(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.padding(AppThemeTokens.spacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.md),
    ) {
        content()
    }
}
