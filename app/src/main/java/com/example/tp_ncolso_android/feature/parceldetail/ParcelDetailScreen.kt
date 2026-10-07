package com.example.tp_ncolso_android.feature.parceldetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.PrimaryTabRow
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
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailEffect.EditRequested
import com.example.tp_ncolso_android.ui.foundation.component.AppErrorContent
import com.example.tp_ncolso_android.ui.foundation.component.AppLoadingContent
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppReadOnlyField
import com.example.tp_ncolso_android.ui.foundation.component.AppSelectField
import com.example.tp_ncolso_android.ui.foundation.component.AppStatusBadge
import com.example.tp_ncolso_android.ui.foundation.component.AppStatusTone
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

data class ParcelDetailCallbacks(
    val onClose: () -> Unit,
    val onEditRequested: (keyNo: String) -> Unit = {},
)

@Composable
fun ParcelDetailRoute(
    viewModel: ParcelDetailViewModel,
    callbacks: ParcelDetailCallbacks,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val currentCallbacks by rememberUpdatedState(callbacks)

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ParcelDetailEffect.Close -> currentCallbacks.onClose()
                is EditRequested -> currentCallbacks.onEditRequested(effect.keyNo)
            }
        }
    }

    ParcelDetailScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onClose = currentCallbacks.onClose,
        modifier = modifier,
    )
}

@Composable
fun ParcelDetailScreen(
    state: ParcelDetailUiState,
    onEvent: (ParcelDetailEvent) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AppThemeTokens.colors.scrim)
            .testTag("parcel-detail-overlay"),
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(top = 28.dp),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            color = AppThemeTokens.colors.surface,
            tonalElevation = 8.dp,
            border = BorderStroke(1.dp, AppThemeTokens.colors.borderDefault),
        ) {
            when (state.loadState) {
                ParcelDetailLoadState.LOADING -> AppLoadingContent("載入土地詳情", Modifier.testTag("parcel-detail-loading"))
                ParcelDetailLoadState.ERROR -> AppErrorContent(
                    message = state.errorMessage ?: "土地詳情無法載入",
                    retry = { onEvent(ParcelDetailEvent.Retry) },
                    modifier = Modifier.testTag("parcel-detail-error"),
                )
                ParcelDetailLoadState.CONTENT -> state.detail?.let { detail ->
                    DetailContent(
                        state = state,
                        detail = detail,
                        onEvent = onEvent,
                        onClose = onClose,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailContent(
    state: ParcelDetailUiState,
    detail: ParcelDetailRecord,
    onEvent: (ParcelDetailEvent) -> Unit,
    onClose: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("土地詳情", style = AppThemeTokens.typography.sectionTitle)
            TextButton(onClick = onClose) { Text("關閉詳情") }
        }

        FixedSummary(detail = detail)
        if (detail.returnReason != null) ReturnReasonBanner(detail.returnReason)

        PrimaryTabRow(
            selectedTabIndex = state.selectedTab.ordinal,
            modifier = Modifier.testTag("parcel-detail-tabs"),
        ) {
            ParcelDetailTab.entries.forEach { tab ->
                Tab(
                    selected = tab == state.selectedTab,
                    onClick = { onEvent(ParcelDetailEvent.SelectTab(tab)) },
                    text = { Text(tab.label) },
                    modifier = Modifier.testTag("parcel-detail-tab-${tab.ordinal}"),
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (state.selectedTab) {
                ParcelDetailTab.LAND_DATA -> LandDataContent(detail)
                ParcelDetailTab.SURVEY -> SurveyContent(state, detail, onEvent)
            }
        }

        detail.editActionLabel?.let { actionLabel ->
            AppPrimaryButton(
                text = actionLabel,
                onClick = { onEvent(ParcelDetailEvent.RequestEdit) },
                modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("parcel-detail-edit"),
            )
        }
    }
}

@Composable
private fun FixedSummary(detail: ParcelDetailRecord) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("parcel-detail-fixed-summary"),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("調查狀態", style = AppThemeTokens.typography.fieldLabel)
            AppStatusBadge(
                label = detail.status,
                tone = AppStatusTone.Neutral,
                modifier = Modifier.testTag("parcel-detail-status"),
            )
        }
        AppReadOnlyField(label = "土地編號", value = detail.keyNo)
        AppReadOnlyField(label = "地號", value = detail.landNo)
        AppReadOnlyField(label = "現場勘查土地情形", value = detail.siteCondition)
    }
}

@Composable
private fun ReturnReasonBanner(reason: ParcelReturnReason) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).testTag("parcel-detail-return-reason"),
        color = AppThemeTokens.colors.surfaceMuted,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, AppThemeTokens.colors.borderDefault),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("退回原因", style = AppThemeTokens.typography.fieldLabel)
                AppStatusBadge(label = reason.category, tone = AppStatusTone.Warning, modifier = Modifier.testTag("parcel-detail-return-tag"))
            }
            Text(reason.explanation, style = AppThemeTokens.typography.body)
        }
    }
}

@Composable
private fun LandDataContent(detail: ParcelDetailRecord) {
    Text("土地標示", style = AppThemeTokens.typography.sectionTitle)
    detail.landMarkingFields.forEach { field ->
        AppReadOnlyField(label = field.label, value = field.value, modifier = Modifier.testTag("parcel-detail-land-field-${field.label}"))
    }
    Text("套匯圖資", style = AppThemeTokens.typography.sectionTitle)
    detail.gisFields.forEach { field ->
        AppReadOnlyField(label = field.label, value = field.value, modifier = Modifier.testTag("parcel-detail-gis-field-${field.label}"))
    }
}

@Composable
private fun SurveyContent(
    state: ParcelDetailUiState,
    detail: ParcelDetailRecord,
    onEvent: (ParcelDetailEvent) -> Unit,
) {
    val survey = state.selectedSurvey ?: return
    AppSelectField(
        selected = survey,
        options = detail.surveyRecords,
        itemLabel = ParcelSurveyRecord::periodLabel,
        label = "期別",
        onSelect = { onEvent(ParcelDetailEvent.SelectPeriod(it.periodId)) },
        modifier = Modifier.testTag("parcel-detail-period-selector"),
    )
    if (state.isHistoryReadOnly) {
        Text(
            "歷史期別（唯讀）",
            modifier = Modifier.testTag("parcel-detail-history-readonly").semantics { contentDescription = "歷史期別唯讀" },
            style = AppThemeTokens.typography.fieldLabel,
        )
    } else {
        Text("最新一期（唯讀檢視）", modifier = Modifier.testTag("parcel-detail-latest"), style = AppThemeTokens.typography.fieldLabel)
    }
    Text("實地勘查土地現況", style = AppThemeTokens.typography.sectionTitle)
    listOf(
        ParcelDetailField("勘查時間", survey.surveyDate),
        ParcelDetailField("勘查人員", survey.surveyUser),
        ParcelDetailField("提交批次", survey.batchNo),
        ParcelDetailField("土地坐落(路名或適當敘明位置)", survey.landLocation),
        ParcelDetailField("現場勘查土地情形", survey.siteCondition),
        ParcelDetailField("占用型態", survey.occupyForm),
        ParcelDetailField("占用戶數", survey.householdCount),
        ParcelDetailField("占用門牌號", survey.occupyAddress),
        ParcelDetailField("備註", survey.note),
        ParcelDetailField("現況照片", survey.photoSummary),
    ).forEach { field ->
        AppReadOnlyField(label = field.label, value = field.value, modifier = Modifier.testTag("parcel-detail-survey-field-${field.label}"))
    }
}
