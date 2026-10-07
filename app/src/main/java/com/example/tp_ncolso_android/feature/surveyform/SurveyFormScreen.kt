package com.example.tp_ncolso_android.feature.surveyform

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEffect.CaptureRequested
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEffect.CloseRequested
import com.example.tp_ncolso_android.ui.foundation.component.AppCheckboxRow
import com.example.tp_ncolso_android.ui.foundation.component.AppErrorContent
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppRadioGroup
import com.example.tp_ncolso_android.ui.foundation.component.AppReadOnlyField
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Composable
fun SurveyFormRoute(
    viewModel: SurveyFormViewModel,
    callbacks: SurveyFormCallbacks,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val currentCallbacks by rememberUpdatedState(callbacks)
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CaptureRequested -> currentCallbacks.onCaptureRequested()
                is CloseRequested -> currentCallbacks.onCloseRequested(effect.keyNo, effect.dirty)
            }
        }
    }
    SurveyFormScreen(state = state, onEvent = viewModel::onEvent, modifier = modifier)
}

@Composable
fun SurveyFormScreen(
    state: SurveyFormUiState,
    onEvent: (SurveyFormEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize().testTag("survey-form-overlay"),
        color = AppThemeTokens.colors.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 402.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("調查填報", style = AppThemeTokens.typography.sectionTitle)
                    Text("${state.status} · ${state.keyNo}", style = AppThemeTokens.typography.supporting)
                }
                TextButton(onClick = { onEvent(SurveyFormEvent.CloseRequested) }) { Text("關閉填報") }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .testTag("survey-form-content"),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SystemFields(state)
                AppTextField(
                    value = state.values.landLocation,
                    onValueChange = { onEvent(SurveyFormEvent.ChangeField(SurveyFormField.LAND_LOCATION, it)) },
                    label = "土地坐落(路名或適當敘明位置)",
                    required = true,
                    isError = SurveyFormField.LAND_LOCATION in state.fieldErrors,
                    supportingText = state.fieldErrors[SurveyFormField.LAND_LOCATION],
                    modifier = Modifier.testTag("survey-field-land-location"),
                )
                SiteConditionSection(state = state, onEvent = onEvent)
                if (!state.values.siteConditions.contains("無占用")) {
                    OccupationSection(state = state, onEvent = onEvent)
                }
                AppTextField(
                    value = state.values.note,
                    onValueChange = { onEvent(SurveyFormEvent.ChangeField(SurveyFormField.NOTE, it)) },
                    label = "備註",
                    placeholder = "住家／營業使用／工地／宮廟／無法進入／無法拍照或自行輸入",
                    singleLine = false,
                    modifier = Modifier.testTag("survey-field-note"),
                )
                PhotoSection(state = state, onEvent = onEvent)
                state.errorMessage?.let { message ->
                    AppErrorContent(message = message, modifier = Modifier.testTag("survey-form-error"))
                }
                when (state.submitState) {
                    SurveyFormSubmitState.SUCCESS -> Text(
                        "已成功送出（本機驗證）",
                        modifier = Modifier.testTag("survey-submit-success").semantics { contentDescription = "送出成功" },
                        color = AppThemeTokens.colors.brandPrimary,
                        style = AppThemeTokens.typography.body,
                    )
                    SurveyFormSubmitState.SUBMITTING -> Text("送出中…", modifier = Modifier.testTag("survey-submit-loading"), style = AppThemeTokens.typography.body)
                    SurveyFormSubmitState.IDLE -> Unit
                }
                if (state.dirty) Text("有未儲存變更", modifier = Modifier.testTag("survey-form-dirty"), style = AppThemeTokens.typography.supporting)
                Spacer(Modifier.height(8.dp))
            }

            AppPrimaryButton(
                text = "送出調查",
                onClick = { onEvent(SurveyFormEvent.Submit) },
                enabled = state.submitState != SurveyFormSubmitState.SUBMITTING,
                loading = state.submitState == SurveyFormSubmitState.SUBMITTING,
                modifier = Modifier.fillMaxWidth().padding(24.dp).testTag("survey-submit"),
            )
        }
    }
}

@Composable
private fun SystemFields(state: SurveyFormUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AppThemeTokens.colors.borderDefault, RoundedCornerShape(12.dp))
            .padding(16.dp)
            .testTag("survey-system-fields"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("系統帶入資訊", style = AppThemeTokens.typography.sectionTitle)
        AppReadOnlyField("勘查時間", state.values.surveyDate)
        AppReadOnlyField("勘查人員", state.values.surveyUser)
        AppReadOnlyField("提交批次", state.values.batchNo)
    }
}

@Composable
private fun SiteConditionSection(
    state: SurveyFormUiState,
    onEvent: (SurveyFormEvent) -> Unit,
) {
    Column(
        modifier = Modifier.testTag("survey-site-condition"),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text("現場勘查土地情形 *", style = AppThemeTokens.typography.fieldLabel)
        SurveyConditionOptions.forEach { option ->
            AppCheckboxRow(
                checked = option in state.values.siteConditions,
                onCheckedChange = { onEvent(SurveyFormEvent.ToggleSiteCondition(option)) },
                label = option,
                modifier = Modifier.testTag("survey-site-option-$option"),
            )
        }
        if (SurveyFormField.SITE_CONDITION in state.fieldErrors) {
            Text(state.fieldErrors.getValue(SurveyFormField.SITE_CONDITION), color = AppThemeTokens.colors.error, style = AppThemeTokens.typography.supporting)
        }
    }
}

@Composable
private fun OccupationSection(
    state: SurveyFormUiState,
    onEvent: (SurveyFormEvent) -> Unit,
) {
    Column(modifier = Modifier.testTag("survey-occupation-fields"), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AppRadioGroup(
            options = OccupyFormOptions,
            selected = state.values.occupyForm.takeIf { it.isNotBlank() },
            onSelect = { onEvent(SurveyFormEvent.ChangeField(SurveyFormField.OCCUPY_FORM, it)) },
            itemLabel = { it },
            label = "占用型態 *",
            isError = SurveyFormField.OCCUPY_FORM in state.fieldErrors,
            modifier = Modifier.testTag("survey-field-occupy-form"),
        )
        state.fieldErrors[SurveyFormField.OCCUPY_FORM]?.let { Text(it, color = AppThemeTokens.colors.error, style = AppThemeTokens.typography.supporting) }
        AppTextField(
            value = state.values.householdCount,
            onValueChange = { onEvent(SurveyFormEvent.ChangeField(SurveyFormField.HOUSEHOLD_COUNT, it)) },
            label = "占用戶數",
            required = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = SurveyFormField.HOUSEHOLD_COUNT in state.fieldErrors,
            supportingText = state.fieldErrors[SurveyFormField.HOUSEHOLD_COUNT],
            modifier = Modifier.testTag("survey-field-household-count"),
        )
        AppTextField(
            value = state.values.occupyAddress,
            onValueChange = { onEvent(SurveyFormEvent.ChangeField(SurveyFormField.OCCUPY_ADDRESS, it)) },
            label = "占用門牌號",
            required = true,
            isError = SurveyFormField.OCCUPY_ADDRESS in state.fieldErrors,
            supportingText = state.fieldErrors[SurveyFormField.OCCUPY_ADDRESS],
            modifier = Modifier.testTag("survey-field-occupy-address"),
        )
    }
}

@Composable
private fun PhotoSection(
    state: SurveyFormUiState,
    onEvent: (SurveyFormEvent) -> Unit,
) {
    Column(modifier = Modifier.testTag("survey-photo-section"), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("現況照片（4:3、現場拍攝） *", style = AppThemeTokens.typography.fieldLabel)
        state.photos.chunked(2).forEachIndexed { rowIndex, rowPhotos ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowPhotos.forEach { photo ->
                    PhotoTile(photo = photo, onDelete = { onEvent(SurveyFormEvent.DeletePhoto(photo.id)) }, modifier = Modifier.weight(1f))
                }
                if (rowPhotos.size == 1) Spacer(Modifier.weight(1f))
            }
            if (rowIndex < state.photos.chunked(2).lastIndex) Spacer(Modifier.height(4.dp))
        }
        if (state.photos.size < 4) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                FloatingActionButton(
                    onClick = { onEvent(SurveyFormEvent.RequestCapture) },
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "拍攝現況照片" }
                        .testTag("survey-camera"),
                    containerColor = AppThemeTokens.colors.brandPrimary,
                ) { Text("拍", color = AppThemeTokens.colors.onBrandPrimary) }
            }
        }
        state.photoError?.let { Text(it, color = AppThemeTokens.colors.error, style = AppThemeTokens.typography.supporting, modifier = Modifier.testTag("survey-photo-error")) }
    }
}

@Composable
private fun PhotoTile(
    photo: SurveyFormPhoto,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(126.dp).testTag("survey-photo-${photo.id}"),
        color = AppThemeTokens.colors.surfaceMuted,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, AppThemeTokens.colors.borderDefault),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("現場照片", style = AppThemeTokens.typography.body)
                Text(photo.capturedAt, style = AppThemeTokens.typography.supporting)
                if (photo.aspect == SurveyPhotoAspect.OTHER) Text("非 4:3", color = AppThemeTokens.colors.error, style = AppThemeTokens.typography.supporting)
            }
            TextButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd).semantics { contentDescription = "刪除照片 ${photo.id}" },
            ) { Text("刪除") }
        }
    }
}
