package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.feature.parcelsearch.data.ParcelSearchParcel
import com.example.tp_ncolso_android.ui.foundation.component.AppReadOnlyField
import com.example.tp_ncolso_android.ui.foundation.component.AppStatusBadge
import com.example.tp_ncolso_android.ui.foundation.component.AppStatusTone
import com.example.tp_ncolso_android.ui.foundation.component.AppTextField
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Composable
fun ParcelSearchRoute(
    viewModel: ParcelSearchViewModel,
    onClose: () -> Unit,
    onParcelSelected: (query: String, selectedTarget: String) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    BackHandler(onBack = onClose)
    LaunchedEffect(state.searchSubmitted, state.query, state.selectedParcel?.keyNo) {
        val selected = state.selectedParcel
        if (state.searchSubmitted && selected != null) {
            onParcelSelected(state.query, selected.selectedTarget)
        }
    }
    ParcelSearchScreen(state = state, onEvent = viewModel::onEvent, onClose = onClose)
}

@Composable
fun ParcelSearchScreen(
    state: ParcelSearchUiState,
    onEvent: (ParcelSearchEvent) -> Unit,
    onClose: () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AppThemeTokens.colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("土地查詢", style = AppThemeTokens.typography.sectionTitle, modifier = Modifier.weight(1f))
                    TextButton(onClick = onClose) { Text("返回地圖") }
                }
                AppTextField(
                    value = state.query,
                    onValueChange = { onEvent(ParcelSearchEvent.QueryChanged(it)) },
                    label = "地號、地段或地號識別碼",
                    placeholder = "例如：中正段、臺北市中正區或 KS-10001",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onEvent(ParcelSearchEvent.SearchSubmitted) }),
                )
                Button(
                    onClick = { onEvent(ParcelSearchEvent.SearchSubmitted) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.query.isNotBlank(),
                ) {
                    Text("搜尋土地")
                }
            }
        }

        when {
            state.noResults -> NoResultsCard(
                query = state.query,
                modifier = Modifier.align(Alignment.BottomCenter),
                onDismiss = { onEvent(ParcelSearchEvent.SummaryDismissed) },
            )

            state.selectedParcel != null -> ParcelSummaryCard(
                parcel = state.selectedParcel,
                modifier = Modifier.align(Alignment.BottomCenter),
                onDismiss = { onEvent(ParcelSearchEvent.SummaryDismissed) },
            )
        }
    }
}

@Composable
private fun NoResultsCard(query: String, modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 12.dp, bottomEnd = 12.dp),
        colors = CardDefaults.cardColors(containerColor = AppThemeTokens.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("查無資料", style = AppThemeTokens.typography.sectionTitle, modifier = Modifier.weight(1f))
                TextButton(onClick = onDismiss) { Text("關閉") }
            }
            Text("找不到「$query」的土地資料，請確認輸入內容。", style = AppThemeTokens.typography.body)
        }
    }
}

@Composable
private fun ParcelSummaryCard(parcel: ParcelSearchParcel, modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 12.dp, bottomEnd = 12.dp),
        colors = CardDefaults.cardColors(containerColor = AppThemeTokens.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("土地摘要", style = AppThemeTokens.typography.sectionTitle, fontWeight = FontWeight.SemiBold)
                    Text(parcel.location, style = AppThemeTokens.typography.supporting, color = AppThemeTokens.colors.textSecondary)
                }
                TextButton(onClick = onDismiss, modifier = Modifier.semantics { contentDescription = "關閉土地摘要" }) {
                    Text("關閉")
                }
            }
            AppStatusBadge(label = parcel.status, tone = AppStatusTone.Neutral)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppReadOnlyField(label = "地號識別碼", value = parcel.keyNo, modifier = Modifier.weight(1f))
                AppReadOnlyField(label = "地號", value = parcel.landNo, modifier = Modifier.weight(1f))
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = AppThemeTokens.colors.surfaceMuted,
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("現場狀況", style = AppThemeTokens.typography.fieldLabel)
                    Spacer(Modifier.width(12.dp))
                    Text(parcel.siteCondition, style = AppThemeTokens.typography.body)
                }
            }
            Spacer(Modifier.height(2.dp))
        }
    }
}
