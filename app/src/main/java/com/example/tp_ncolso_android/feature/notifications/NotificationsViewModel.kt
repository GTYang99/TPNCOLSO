package com.example.tp_ncolso_android.feature.notifications

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp_ncolso_android.feature.notifications.data.NotificationsDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotificationsViewModel(
    dataSource: NotificationsDataSource,
    @Suppress("UNUSED_PARAMETER") savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {
    private val mutableState = MutableStateFlow(NotificationsUiState(items = dataSource.returnedNotifications.value))
    val state: StateFlow<NotificationsUiState> = mutableState.asStateFlow()

    private val effects = Channel<NotificationsEffect>(capacity = Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            dataSource.returnedNotifications.collect { items ->
                mutableState.value = NotificationsUiState(items)
            }
        }
    }

    fun onEvent(event: NotificationsEvent) {
        when (event) {
            NotificationsEvent.Close -> effects.trySend(NotificationsEffect.Close)
            is NotificationsEvent.SelectParcel -> effects.trySend(NotificationsEffect.ParcelSelected(event.keyNo))
        }
    }
}
