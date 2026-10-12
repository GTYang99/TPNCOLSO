package com.example.tp_ncolso_android.feature.notifications

import androidx.compose.runtime.Immutable

@Immutable
data class ReturnedNotification(
    val keyNo: String,
    val landNo: String,
    val status: String,
    val reasonCategory: String,
    val reasonPreview: String,
)

@Immutable
data class NotificationsUiState(
    val items: List<ReturnedNotification> = emptyList(),
)

sealed interface NotificationsEvent {
    data object Close : NotificationsEvent
    data class SelectParcel(val keyNo: String) : NotificationsEvent
}

sealed interface NotificationsEffect {
    data object Close : NotificationsEffect
    data class ParcelSelected(val keyNo: String) : NotificationsEffect
}

data class NotificationsCallbacks(
    val onClose: () -> Unit,
    val onParcelSelected: (keyNo: String) -> Unit,
)
