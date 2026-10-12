package com.example.tp_ncolso_android.feature.notifications

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.feature.notifications.NotificationsEffect.Close
import com.example.tp_ncolso_android.feature.notifications.NotificationsEffect.ParcelSelected
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

@Composable
fun NotificationsRoute(
    viewModel: NotificationsViewModel,
    callbacks: NotificationsCallbacks,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val currentCallbacks by rememberUpdatedState(callbacks)

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                Close -> currentCallbacks.onClose()
                is ParcelSelected -> currentCallbacks.onParcelSelected(effect.keyNo)
            }
        }
    }

    BackHandler { viewModel.onEvent(NotificationsEvent.Close) }
    NotificationsScreen(state = state, onEvent = viewModel::onEvent, modifier = modifier)
}

@Composable
fun NotificationsScreen(
    state: NotificationsUiState,
    onEvent: (NotificationsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var cardBounds by remember { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }
    val closeAction = rememberUpdatedState { onEvent(NotificationsEvent.Close) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(cardBounds) {
                detectTapGestures { tap ->
                    if (cardBounds?.contains(tap) != true) onEvent(NotificationsEvent.Close)
                }
            }
            .semantics {
                contentDescription = "退回通知清單，共 ${state.items.size} 則"
                customActions = listOf(
                    CustomAccessibilityAction("關閉通知清單") {
                        closeAction.value()
                        true
                    },
                )
            }
            .testTag("notifications-overlay"),
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 76.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .heightIn(max = maxHeight * 0.7f)
                .onGloballyPositioned { cardBounds = it.boundsInParent() }
                .semantics { contentDescription = "退回通知項目" },
            color = AppThemeTokens.colors.surface,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 12.dp,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(AppThemeTokens.colors.brandPrimary)
                        .heightIn(min = 40.dp)
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "共 ${state.items.size} 則",
                        color = AppThemeTokens.colors.onBrandPrimary,
                        style = AppThemeTokens.typography.supporting,
                    )
                }

                if (state.items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp)
                            .padding(20.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "目前沒有待處理的退回通知",
                            color = AppThemeTokens.colors.textSecondary,
                            style = AppThemeTokens.typography.body,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().testTag("notifications-returned-list"),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 8.dp),
                    ) {
                        itemsIndexed(state.items, key = { _, item -> item.keyNo }) { index, item ->
                            ReturnedNotificationRow(
                                item = item,
                                onClick = { onEvent(NotificationsEvent.SelectParcel(item.keyNo)) },
                            )
                            if (index < state.items.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                                    color = AppThemeTokens.colors.borderDefault,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReturnedNotificationRow(
    item: ReturnedNotification,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = "${item.status}，土地編號 ${item.landNo}，${item.reasonCategory}，${item.reasonPreview}"
                role = Role.Button
            }
            .clickable(onClick = onClick)
            .testTag("notification-returned-${item.keyNo}"),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReturnedStatusChip(
            modifier = Modifier.padding(start = 16.dp),
            label = item.status,
        )
        Column(
            modifier = Modifier.weight(1f).padding(end = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                "土地編號｜${item.landNo}",
                color = AppThemeTokens.colors.textPrimary,
                style = AppThemeTokens.typography.supporting.copy(fontWeight = FontWeight.Bold),
            )
            Text(
                "意見：${item.reasonPreview}",
                color = AppThemeTokens.colors.textSecondary,
                style = AppThemeTokens.typography.supporting,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ReturnedStatusChip(
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = AppThemeTokens.colors.surface,
        border = BorderStroke(1.dp, AppThemeTokens.colors.error),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = AppThemeTokens.colors.error,
            style = AppThemeTokens.typography.supporting.copy(fontWeight = FontWeight.Bold),
        )
    }
}
