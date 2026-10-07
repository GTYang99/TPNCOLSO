package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchEffect.ParcelSelected
import com.example.tp_ncolso_android.ui.foundation.component.AppEmptyContent
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppReadOnlyField
import com.example.tp_ncolso_android.ui.foundation.component.AppSecondaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppStatusBadge
import com.example.tp_ncolso_android.ui.foundation.component.AppStatusTone
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

data class ParcelSearchCallbacks(
    val onClose: () -> Unit,
    val onParcelSelected: (query: String, selectedTarget: String) -> Unit,
    val onOpenDetail: (keyNo: String) -> Unit = {},
)

@Composable
fun ParcelSearchRoute(
    viewModel: ParcelSearchViewModel,
    callbacks: ParcelSearchCallbacks,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val currentCallbacks by rememberUpdatedState(callbacks)

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ParcelSelected -> currentCallbacks.onParcelSelected(effect.query, effect.selectedTarget)
            }
        }
    }

    ParcelSearchScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onClose = { currentCallbacks.onClose() },
        onOpenDetail = { currentCallbacks.onOpenDetail(it) },
        modifier = modifier,
    )
}

@Composable
fun ParcelSearchScreen(
    state: ParcelSearchUiState,
    onEvent: (ParcelSearchEvent) -> Unit,
    onClose: () -> Unit,
    onOpenDetail: (keyNo: String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize().testTag("parcel-search-overlay")) {
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 88.dp),
            shape = RoundedCornerShape(16.dp),
            color = AppThemeTokens.colors.surface,
            tonalElevation = 4.dp,
            border = BorderStroke(1.dp, AppThemeTokens.colors.borderDefault),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("土地查詢", style = AppThemeTokens.typography.sectionTitle)
                    TextButton(onClick = onClose) { Text("關閉查詢") }
                }
                AppTextField(
                    value = state.query,
                    onValueChange = { onEvent(ParcelSearchEvent.QueryChanged(it)) },
                    label = "土地關鍵字",
                    placeholder = "輸入地號、土地坐落或 key_no",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onEvent(ParcelSearchEvent.SubmitQuery) }),
                    modifier = Modifier.testTag("parcel-search-input"),
                )
                AppPrimaryButton(
                    text = "搜尋",
                    onClick = { onEvent(ParcelSearchEvent.SubmitQuery) },
                    modifier = Modifier.fillMaxWidth().testTag("parcel-search-submit"),
                )
                if (state.outcome == ParcelSearchOutcome.NO_MATCH) {
                    AppEmptyContent(
                        title = "查無資料",
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("parcel-search-no-result")
                            .semantics { liveRegion = LiveRegionMode.Polite },
                    )
                }
            }
        }

        val parcel = state.selectedParcel
        if (state.summaryVisible && parcel != null) {
            ParcelSummaryCard(
                parcel = parcel,
                onDismiss = { onEvent(ParcelSearchEvent.DismissSummary) },
                onOpenDetail = { onOpenDetail(parcel.keyNo) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 120.dp)
                    .testTag("parcel-summary-card"),
            )
        }
    }
}

@Composable
private fun ParcelSummaryCard(
    parcel: ParcelSearchRecord,
    onDismiss: () -> Unit,
    onOpenDetail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = AppThemeTokens.colors.surface,
        tonalElevation = 8.dp,
        border = BorderStroke(1.dp, AppThemeTokens.colors.borderDefault),
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = 320.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("土地摘要", style = AppThemeTokens.typography.sectionTitle)
                TextButton(onClick = onDismiss) { Text("關閉摘要") }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("調查狀態", style = AppThemeTokens.typography.fieldLabel)
                AppStatusBadge(
                    label = parcel.status,
                    tone = AppStatusTone.Neutral,
                    modifier = Modifier.testTag("parcel-status"),
                )
            }
            AppReadOnlyField(label = "土地編號", value = parcel.keyNo)
            AppReadOnlyField(label = "地號", value = parcel.landNo)
            AppReadOnlyField(label = "現場勘查土地情形", value = parcel.siteCondition)
            AppSecondaryButton(
                text = "查看詳情",
                onClick = onOpenDetail,
                modifier = Modifier.fillMaxWidth().testTag("parcel-open-detail"),
            )
        }
    }
}
