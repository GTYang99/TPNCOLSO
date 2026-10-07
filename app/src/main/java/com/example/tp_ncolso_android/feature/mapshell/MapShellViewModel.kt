package com.example.tp_ncolso_android.feature.mapshell

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class MapShellViewModel(identity: com.example.tp_ncolso_android.session.AppIdentity) : ViewModel() {
    private val mutableState = MutableStateFlow(MapShellUiState(identity = identity))
    val state: StateFlow<MapShellUiState> = mutableState.asStateFlow()

    private val effects = Channel<MapShellEffect>(capacity = Channel.BUFFERED)
    val effect = effects.receiveAsFlow()
    private var logoutRequested = false

    fun onEvent(event: MapShellEvent) {
        when (event) {
            is MapShellEvent.SelectBasemap -> mutableState.value = mutableState.value.copy(selectedBasemap = event.basemap)
            MapShellEvent.OpenDrawer -> mutableState.value = mutableState.value.copy(drawerOpen = true)
            MapShellEvent.CloseDrawer -> mutableState.value = mutableState.value.copy(drawerOpen = false)
            MapShellEvent.SearchClicked,
            MapShellEvent.NotificationsClicked,
            MapShellEvent.MapNavigationClicked,
            MapShellEvent.MapPlatformClicked,
            MapShellEvent.DashboardClicked -> Unit
            is MapShellEvent.UpdateSearchContext -> mutableState.value = mutableState.value.copy(
                overlay = mutableState.value.overlay.copy(
                    activeQuery = event.query,
                    selectedTarget = event.selectedTarget,
                ),
            )
            MapShellEvent.LocationClicked -> mutableState.value = mutableState.value.copy(locationState = LocationState.LOADING)
            MapShellEvent.RetryLocation -> mutableState.value = mutableState.value.copy(locationState = LocationState.LOADING)
            is MapShellEvent.SetLocationState -> mutableState.value = mutableState.value.copy(locationState = event.state)
            MapShellEvent.LogoutRequested -> requestLogout()
        }
    }

    private fun requestLogout() {
        if (logoutRequested) return
        logoutRequested = true
        mutableState.value = mutableState.value.copy(drawerOpen = false)
        effects.trySend(MapShellEffect.LogoutRequested)
    }
}
