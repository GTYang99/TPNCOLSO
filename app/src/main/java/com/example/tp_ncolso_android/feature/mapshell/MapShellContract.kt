package com.example.tp_ncolso_android.feature.mapshell

import androidx.compose.runtime.Immutable
import com.example.tp_ncolso_android.session.AppIdentity

enum class Basemap(val label: String) {
    ELECTRONIC("電子地圖"),
    ORTHOPHOTO("正射圖"),
    TERRAIN("地形圖"),
}

enum class LocationState {
    NORMAL,
    LOADING,
    PERMISSION_DENIED,
}

@Immutable
data class MapOverlayFixture(
    val label: String = "示範地塊",
    val selectedTarget: String = "地塊 A-001",
    val userLocationMarker: String = "目前位置",
    val activeQuery: String = "",
)

@Immutable
data class MapShellUiState(
    val identity: AppIdentity,
    val selectedBasemap: Basemap = Basemap.ELECTRONIC,
    val overlay: MapOverlayFixture = MapOverlayFixture(),
    val locationState: LocationState = LocationState.NORMAL,
    val drawerOpen: Boolean = false,
)

sealed interface MapShellEvent {
    data class SelectBasemap(val basemap: Basemap) : MapShellEvent
    data object OpenDrawer : MapShellEvent
    data object CloseDrawer : MapShellEvent
    data object SearchClicked : MapShellEvent
    data object NotificationsClicked : MapShellEvent
    data object MapNavigationClicked : MapShellEvent
    data object MapPlatformClicked : MapShellEvent
    data object DashboardClicked : MapShellEvent
    data object LocationClicked : MapShellEvent
    data object RetryLocation : MapShellEvent
    data class SetLocationState(val state: LocationState) : MapShellEvent
    data object LogoutRequested : MapShellEvent
}

sealed interface MapShellEffect {
    data object LogoutRequested : MapShellEffect
}
