package com.example.tp_ncolso_android.ui.foundation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    required: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    labelStyle: TextStyle = AppThemeTokens.typography.fieldLabel,
) {
    val displayLabel = if (required) "$label *" else label
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(displayLabel, style = labelStyle, modifier = Modifier.semantics { if (!enabled) disabled() })
        AppOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder,
            supportingText = supportingText?.let { { Text(it) } },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            textStyle = AppThemeTokens.typography.body.copy(color = AppThemeTokens.colors.textPrimary),
        )
    }
}

@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    required: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null,
    visibilityActionEnabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    labelStyle: TextStyle = AppThemeTokens.typography.fieldLabel,
) {
    val displayLabel = if (required) "$label *" else label
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(displayLabel, style = labelStyle, modifier = Modifier.semantics { if (!enabled) disabled() })
        AppOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            modifier = Modifier.fillMaxWidth().testTag("password-field"),
            placeholder = placeholder,
            supportingText = supportingText?.let { { Text(it) } },
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = if (visibilityActionEnabled) {
                {
                    TextButton(onClick = { onVisibilityChange(!visible) }) {
                        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                            Text(if (visible) "隱藏密碼" else "顯示密碼", color = androidx.compose.ui.graphics.Color.Transparent)
                            androidx.compose.foundation.Image(
                                painter = painterResource(com.example.tp_ncolso_android.R.drawable.ic_password_visibility_off),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp, 11.dp),
                            )
                        }
                    }
                }
            } else null,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            textStyle = AppThemeTokens.typography.body.copy(color = AppThemeTokens.colors.textPrimary),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeholder: String,
    enabled: Boolean,
    readOnly: Boolean,
    isError: Boolean,
    supportingText: (@Composable () -> Unit)?,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    textStyle: TextStyle,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = AppThemeTokens.colors.brandPrimary,
        unfocusedBorderColor = AppThemeTokens.colors.borderDefault,
        focusedPlaceholderColor = Color(0xFFA8ABB2),
        unfocusedPlaceholderColor = Color(0xFFA8ABB2),
        errorBorderColor = AppThemeTokens.colors.fieldErrorBorder,
        errorSupportingTextColor = AppThemeTokens.colors.errorText,
    )
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.height(48.dp).semantics {
            if (isError && supportingText != null) error("欄位輸入錯誤")
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = SolidColor(AppThemeTokens.colors.brandPrimary),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                isError = isError,
                placeholder = { if (value.isEmpty()) Text(placeholder) },
                trailingIcon = trailingIcon,
                supportingText = supportingText,
                colors = colors,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppSelectField(
    selected: T?,
    options: List<T>,
    itemLabel: (T) -> String,
    label: String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = AppThemeTokens.typography.fieldLabel)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = it },
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = selected?.let(itemLabel).orEmpty(),
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                isError = isError,
                placeholder = { Text("請選擇") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = AppThemeTokens.colors.brandPrimary,
                    unfocusedBorderColor = AppThemeTokens.colors.borderDefault,
                    focusedPlaceholderColor = Color(0xFFA8ABB2),
                    unfocusedPlaceholderColor = Color(0xFFA8ABB2),
                    errorBorderColor = AppThemeTokens.colors.fieldErrorBorder,
                    errorSupportingTextColor = AppThemeTokens.colors.errorText,
                ),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                supportingText = supportingText?.let { { Text(it) } },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled)
                    .fillMaxWidth()
                    .height(48.dp)
                    .semantics {
                        if (isError && supportingText != null) error(supportingText)
                    },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(itemLabel(item)) },
                        onClick = {
                            onSelect(item)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun <T> AppRadioGroup(
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    itemLabel: (T) -> String,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    Column(
        modifier = modifier.selectableGroup().semantics {
            contentDescription = label
            if (isError) error("$label 選項錯誤")
        },
        verticalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.sm),
    ) {
        Text(text = label, style = AppThemeTokens.typography.fieldLabel)
        options.forEach { option ->
            val selectedOption = selected == option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = AppThemeTokens.spacing.minimumTouchTarget)
                    .selectable(
                        selected = selectedOption,
                        enabled = enabled,
                        role = Role.RadioButton,
                        onClick = { onSelect(option) },
                    )
                    .padding(horizontal = AppThemeTokens.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.sm),
            ) {
                RadioButton(selected = selectedOption, enabled = enabled, onClick = null)
                Text(itemLabel(option), style = AppThemeTokens.typography.body)
            }
        }
    }
}

@Composable
fun AppCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AppThemeTokens.spacing.minimumTouchTarget)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            )
            .padding(horizontal = AppThemeTokens.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.sm),
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(14.dp)
                .background(if (checked) AppThemeTokens.colors.brandPrimary else Color.White, RoundedCornerShape(2.dp))
                .border(1.dp, if (enabled) AppThemeTokens.colors.borderDefault else AppThemeTokens.colors.textSecondary, RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) Text("✓", color = Color.White, fontSize = 11.sp, lineHeight = 11.sp)
        }
        Text(label, style = AppThemeTokens.typography.body.copy(fontWeight = FontWeight.Medium))
    }
}

@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier.heightIn(min = AppThemeTokens.spacing.minimumTouchTarget),
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp), strokeWidth = 2.dp)
        }
        Text(text)
    }
}

@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = AppThemeTokens.spacing.minimumTouchTarget),
    ) {
        Text(text)
    }
}

@Composable
fun AppIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    require(contentDescription.isNotBlank()) { "Icon-only actions require a nonblank content description." }
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = AppThemeTokens.spacing.minimumTouchTarget),
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

enum class AppStatusTone {
    Neutral,
    Success,
    Warning,
    Error,
}

@Composable
fun AppStatusBadge(
    label: String,
    tone: AppStatusTone,
    modifier: Modifier = Modifier,
) {
    Text(
        text = label,
        modifier = modifier.semantics { contentDescription = "$label $tone" },
        style = AppThemeTokens.typography.supporting,
    )
}

@Composable
fun AppLoadingContent(message: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(AppThemeTokens.spacing.md)
            .semantics {
                contentDescription = "載入中：$message"
                liveRegion = LiveRegionMode.Polite
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.sm),
    ) {
        CircularProgressIndicator()
        Text(message, style = AppThemeTokens.typography.body)
    }
}

@Composable
fun AppEmptyContent(
    title: String,
    modifier: Modifier = Modifier,
    body: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .padding(AppThemeTokens.spacing.md),
        verticalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.sm),
    ) {
        Text(title, style = AppThemeTokens.typography.sectionTitle)
        body?.let { Text(it, style = AppThemeTokens.typography.body) }
        action?.invoke()
    }
}

@Composable
fun AppErrorContent(
    message: String,
    modifier: Modifier = Modifier,
    retry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .padding(AppThemeTokens.spacing.md)
            .semantics {
                error(message)
                liveRegion = LiveRegionMode.Polite
            },
        verticalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.sm),
    ) {
        Text(message, color = AppThemeTokens.colors.error, style = AppThemeTokens.typography.body)
        retry?.let { AppSecondaryButton(text = "重試", onClick = it) }
    }
}

@Composable
fun AppReadOnlyField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.xs)) {
        Text(label, style = AppThemeTokens.typography.fieldLabel, color = AppThemeTokens.colors.textSecondary)
        Text(value, style = AppThemeTokens.typography.body)
    }
}
