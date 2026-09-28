package com.example.tp_ncolso_android.feature.mapshell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppSecondaryButton
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

data class MapShellCallbacks(
    val onSearch: () -> Unit = {},
    val onNotifications: () -> Unit = {},
    val onMapNavigation: () -> Unit = {},
    val onLocation: () -> Unit = {},
    val onLogoutRequested: () -> Unit = {},
)

@Composable
fun MapShellRoute(
    identity: AppIdentity,
    callbacks: MapShellCallbacks = MapShellCallbacks(),
    viewModel: MapShellViewModel = remember(identity) { MapShellViewModel(identity) },
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                MapShellEffect.LogoutRequested -> callbacks.onLogoutRequested()
            }
        }
    }
    MapShellScreen(state = state, onEvent = { event ->
        when (event) {
            MapShellEvent.SearchClicked -> callbacks.onSearch()
            MapShellEvent.NotificationsClicked -> callbacks.onNotifications()
            MapShellEvent.MapNavigationClicked -> callbacks.onMapNavigation()
            MapShellEvent.LocationClicked -> callbacks.onLocation()
            else -> Unit
        }
        viewModel.onEvent(event)
    })
}

@Composable
fun MapShellScreen(
    state: MapShellUiState,
    onEvent: (MapShellEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize().background(AppThemeTokens.colors.surfaceMuted)) {
        MapSurface(state = state, onEvent = onEvent)
        TopControls(onEvent = onEvent)
        BasemapSwitcher(state = state, onEvent = onEvent)
        LocationAffordance(state = state, onEvent = onEvent)
        if (state.drawerOpen) Drawer(state = state, onEvent = onEvent)
    }
}

@Composable
private fun MapSurface(state: MapShellUiState, onEvent: (MapShellEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("map-surface")
            .semantics { contentDescription = "地圖區域，底圖 ${state.selectedBasemap.label}" },
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 112.dp, bottom = 142.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("${state.selectedBasemap.label}示意底圖", color = AppThemeTokens.colors.textSecondary)
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .size(176.dp)
                    .background(Color(0xFFD8E5D1), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF9CB497), RoundedCornerShape(16.dp))
                    .clickable(role = Role.Button) { onEvent(MapShellEvent.MapNavigationClicked) }
                    .semantics { contentDescription = "${state.overlay.selectedTarget}，${state.overlay.label}" },
                contentAlignment = Alignment.Center,
            ) { Text(state.overlay.selectedTarget, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(12.dp))
            Text("${state.overlay.userLocationMarker} · ${state.overlay.activeQuery.ifBlank { "尚無查詢" }}", color = AppThemeTokens.colors.textSecondary)
            when (state.locationState) {
                LocationState.NORMAL -> Unit
                LocationState.LOADING -> Text("定位中…", modifier = Modifier.testTag("location-loading"))
                LocationState.PERMISSION_DENIED -> {
                    Text("需要定位權限才能顯示目前位置", modifier = Modifier.testTag("location-denied"))
                    AppSecondaryButton("重新嘗試", { onEvent(MapShellEvent.RetryLocation) }, Modifier.testTag("location-retry"))
                }
            }
        }
    }
}

@Composable
private fun TopControls(onEvent: (MapShellEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ShellAction("☰", "開啟選單", "map-hamburger") { onEvent(MapShellEvent.OpenDrawer) }
        ShellAction("⌕", "關鍵字搜尋", "map-search") { onEvent(MapShellEvent.SearchClicked) }
        ShellAction("通知", "通知", "map-notifications") { onEvent(MapShellEvent.NotificationsClicked) }
    }
}

@Composable
private fun BoxScope.LocationAffordance(state: MapShellUiState, onEvent: (MapShellEvent) -> Unit) {
    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 152.dp)
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .background(AppThemeTokens.colors.surface, RoundedCornerShape(24.dp))
            .clickable(role = Role.Button) { onEvent(MapShellEvent.LocationClicked) }
            .semantics { contentDescription = "定位目前位置" }
            .testTag("map-location"),
        contentAlignment = Alignment.Center,
    ) { Text(if (state.locationState == LocationState.LOADING) "…" else "◎") }
}

@Composable
private fun BoxScope.BasemapSwitcher(state: MapShellUiState, onEvent: (MapShellEvent) -> Unit) {
    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .background(AppThemeTokens.colors.surface, RoundedCornerShape(16.dp))
            .padding(8.dp)
            .testTag("basemap-switcher"),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Basemap.entries.forEach { basemap ->
            val selected = basemap == state.selectedBasemap
            Box(
                modifier = Modifier
                    .weight(1f)
                    .sizeIn(minHeight = 48.dp)
                    .background(if (selected) AppThemeTokens.colors.brandPrimary else Color.Transparent, RoundedCornerShape(12.dp))
                    .clickable(role = Role.RadioButton) { onEvent(MapShellEvent.SelectBasemap(basemap)) }
                    .semantics {
                        contentDescription = basemap.label
                        role = Role.RadioButton
                    },
                contentAlignment = Alignment.Center,
            ) { Text(basemap.label, color = if (selected) AppThemeTokens.colors.onBrandPrimary else AppThemeTokens.colors.textPrimary) }
        }
    }
}

@Composable
private fun Drawer(state: MapShellUiState, onEvent: (MapShellEvent) -> Unit) {
    Box(Modifier.fillMaxSize().background(AppThemeTokens.colors.scrim)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .width(304.dp)
                .background(AppThemeTokens.colors.surface)
                .padding(24.dp)
                .testTag("map-drawer"),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("${state.identity.displayName} · ${state.identity.role}", style = AppThemeTokens.typography.sectionTitle)
            Text("圖台", modifier = Modifier.sizeIn(minHeight = 48.dp).padding(vertical = 12.dp))
            Text("儀錶板", modifier = Modifier.sizeIn(minHeight = 48.dp).padding(vertical = 12.dp))
            Spacer(Modifier.weight(1f))
            AppPrimaryButton("登出", { onEvent(MapShellEvent.LogoutRequested) }, Modifier.fillMaxWidth().testTag("map-logout"))
            AppSecondaryButton("關閉選單", { onEvent(MapShellEvent.CloseDrawer) }, Modifier.fillMaxWidth().testTag("map-drawer-close"))
        }
    }
}

@Composable
private fun ShellAction(icon: String, description: String, tag: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .background(AppThemeTokens.colors.surface, RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description }
            .testTag(tag),
        contentAlignment = Alignment.Center,
    ) { Text(icon) }
}
