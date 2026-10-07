package com.example.tp_ncolso_android.feature.surveyform

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    backEnabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val currentCallbacks by rememberUpdatedState(callbacks)
    val currentState by rememberUpdatedState(state)
    var pendingDeletePhotoId by rememberSaveable { mutableStateOf<String?>(null) }
    var showDiscardDialog by rememberSaveable { mutableStateOf(false) }
    val requestClose by rememberUpdatedState {
        if (currentState.dirty) {
            showDiscardDialog = true
        } else {
            currentCallbacks.onCloseRequested(currentState.keyNo, false)
        }
    }
    BackHandler(enabled = backEnabled) { viewModel.onEvent(SurveyFormEvent.CloseRequested) }
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CaptureRequested -> currentCallbacks.onCaptureRequested()
                is CloseRequested -> requestClose()
            }
        }
    }
    SurveyFormScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onDeleteRequested = { pendingDeletePhotoId = it },
        modifier = modifier,
    )

    val deletePhotoId = pendingDeletePhotoId
    if (deletePhotoId != null && state.photos.any { it.id == deletePhotoId }) {
        SurveyAlertDialog(
            title = "刪除確認",
            message = "刪除後無法恢復。",
            dismissLabel = "取消",
            confirmLabel = "刪除",
            confirmColor = AppThemeTokens.colors.error,
            onDismissRequest = { pendingDeletePhotoId = null },
            onDismiss = { pendingDeletePhotoId = null },
            onConfirm = {
                viewModel.onEvent(SurveyFormEvent.DeletePhoto(deletePhotoId))
                pendingDeletePhotoId = null
            },
        )
    }

    if (showDiscardDialog) {
        SurveyAlertDialog(
            title = "是否捨棄未儲存的內容？",
            message = "如果現在離開，剛才編輯的內容將會遺失。",
            dismissLabel = "繼續編輯",
            confirmLabel = "捨棄編輯",
            confirmColor = AppThemeTokens.colors.error,
            onDismissRequest = { showDiscardDialog = false },
            onDismiss = { showDiscardDialog = false },
            onConfirm = {
                showDiscardDialog = false
                currentCallbacks.onCloseRequested(currentState.keyNo, true)
            },
        )
    }
}

@Composable
private fun SurveyAlertDialog(
    title: String,
    message: String,
    dismissLabel: String,
    confirmLabel: String,
    confirmColor: Color,
    onDismissRequest: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.widthIn(min = 280.dp, max = 312.dp).fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        containerColor = AppThemeTokens.colors.surface,
        tonalElevation = 0.dp,
        title = {
            Text(
                title,
                color = AppThemeTokens.colors.textPrimary,
                style = AppThemeTokens.typography.sectionTitle.copy(lineHeight = 28.sp),
            )
        },
        text = {
            Text(
                message,
                color = AppThemeTokens.colors.textPrimary,
                style = AppThemeTokens.typography.body,
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier.heightIn(min = AppThemeTokens.spacing.minimumTouchTarget),
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = confirmColor),
            ) { Text(confirmLabel, style = AppThemeTokens.typography.fieldLabel) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.heightIn(min = AppThemeTokens.spacing.minimumTouchTarget),
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(contentColor = AppThemeTokens.colors.brandPrimary),
            ) { Text(dismissLabel, style = AppThemeTokens.typography.fieldLabel) }
        },
    )
}

@Composable
fun SurveyFormScreen(
    state: SurveyFormUiState,
    onEvent: (SurveyFormEvent) -> Unit,
    onDeleteRequested: (photoId: String) -> Unit,
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
                PhotoSection(
                    state = state,
                    onEvent = onEvent,
                    onDeleteRequested = onDeleteRequested,
                )
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
    onDeleteRequested: (photoId: String) -> Unit,
) {
    Column(modifier = Modifier.testTag("survey-photo-section"), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("現況照片", style = AppThemeTokens.typography.fieldLabel)
        Text("*需拍攝4張照片", color = AppThemeTokens.colors.error, style = AppThemeTokens.typography.supporting)
        val tiles = buildList<SurveyFormPhoto?> {
            addAll(state.photos)
            if (state.photos.size < 4) add(null)
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            tiles.chunked(2).forEach { rowTiles ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowTiles.forEach { photo ->
                        if (photo == null) {
                            CameraTile(
                                onClick = { onEvent(SurveyFormEvent.RequestCapture) },
                                modifier = Modifier.testTag("survey-camera"),
                            )
                        } else {
                            PhotoTile(
                                photo = photo,
                                onDelete = { onDeleteRequested(photo.id) },
                            )
                        }
                    }
                    if (rowTiles.size == 1) Spacer(Modifier.width(168.dp))
                }
            }
        }
        state.photoError?.let { Text(it, color = AppThemeTokens.colors.error, style = AppThemeTokens.typography.supporting, modifier = Modifier.testTag("survey-photo-error")) }
    }
}

@Composable
private fun CameraTile(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(width = 168.dp, height = 126.dp)
            .clickable(onClick = onClick)
            .semantics { contentDescription = "拍攝現況照片" },
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(com.example.tp_ncolso_android.R.drawable.ic_photo_camera),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
            Text("拍攝照片", color = AppThemeTokens.colors.textSecondary, style = AppThemeTokens.typography.body)
        }
    }
}

@Composable
private fun PhotoTile(
    photo: SurveyFormPhoto,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bitmap by rememberPhotoBitmap(photo.uri)
    Surface(
        modifier = modifier.size(width = 168.dp, height = 126.dp).testTag("survey-photo-${photo.id}"),
        color = Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 6.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!,
                    contentDescription = "現場照片",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().background(AppThemeTokens.colors.surfaceMuted),
                    contentAlignment = Alignment.Center,
                ) { Text("現場照片", style = AppThemeTokens.typography.body) }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
            ) {
                Text(photo.capturedAt, color = Color.White, style = AppThemeTokens.typography.supporting)
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp)
                    .clickable(onClick = onDelete)
                    .semantics { contentDescription = "刪除照片 ${photo.id}" },
                contentAlignment = Alignment.Center,
            ) {
                Surface(modifier = Modifier.size(20.dp), color = Color.White, shape = CircleShape) {
                    Image(
                        painter = painterResource(com.example.tp_ncolso_android.R.drawable.ic_photo_delete),
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp).size(15.dp),
                    )
                }
            }
            if (photo.aspect == SurveyPhotoAspect.OTHER) {
                Text("非 4:3", color = AppThemeTokens.colors.error, style = AppThemeTokens.typography.supporting)
            }
        }
    }
}

@Composable
private fun rememberPhotoBitmap(uriString: String): androidx.compose.runtime.State<ImageBitmap?> {
    val context = androidx.compose.ui.platform.LocalContext.current
    return produceState<ImageBitmap?>(initialValue = null, uriString) {
        value = withContext(Dispatchers.IO) {
            runCatching {
                val uri = Uri.parse(uriString)
                val bitmap = if (uri.scheme == "file") {
                    BitmapFactory.decodeFile(uri.path)
                } else {
                    context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream)
                }
                bitmap?.asImageBitmap()
            }.getOrNull()
        }
    }
}
