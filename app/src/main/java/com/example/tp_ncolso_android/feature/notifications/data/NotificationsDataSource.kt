package com.example.tp_ncolso_android.feature.notifications.data

import com.example.tp_ncolso_android.feature.notifications.ReturnedNotification
import kotlinx.coroutines.flow.StateFlow

interface NotificationsDataSource {
    val returnedNotifications: StateFlow<List<ReturnedNotification>>
}
