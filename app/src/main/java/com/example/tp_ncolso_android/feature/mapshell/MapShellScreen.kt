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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.R
import com.example.tp_ncolso_android.ui.foundation.component.AppErrorContent
import com.example.tp_ncolso_android.ui.foundation.component.AppLoadingContent
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppSecondaryButton
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens

data class MapShellCallbacks(
    val onSearch: () -> Unit = {},
    val onNotifications: () -> Unit = {},
    val onMapNavigation: () -> Unit = {},
    val onMapPlatform: () -> Unit = {},
    val onDashboard: () -> Unit = {},
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
            MapShellEvent.MapPlatformClicked -> callbacks.onMapPlatform()
            MapShellEvent.DashboardClicked -> callbacks.onDashboard()
            MapShellEvent.LocationClicked -> callbacks.onLocation()
            MapShellEvent.RetryLocation -> callbacks.onLocation()
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
                    .background(AppThemeTokens.colors.surface, RoundedCornerShape(16.dp))
                    .border(1.dp, AppThemeTokens.colors.borderDefault, RoundedCornerShape(16.dp))
                    .clickable(role = Role.Button) { onEvent(MapShellEvent.MapNavigationClicked) }
                    .semantics { contentDescription = "${state.overlay.selectedTarget}，${state.overlay.label}" },
                contentAlignment = Alignment.Center,
            ) { Text(state.overlay.selectedTarget, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(12.dp))
            Text("${state.overlay.userLocationMarker} · ${state.overlay.activeQuery.ifBlank { "尚無查詢" }}", color = AppThemeTokens.colors.textSecondary)
            when (state.locationState) {
                LocationState.NORMAL -> Unit
                LocationState.LOADING -> AppLoadingContent(
                    message = "定位中…",
                    modifier = Modifier.testTag("location-loading"),
                )
                LocationState.PERMISSION_DENIED -> {
                    AppErrorContent(
                        message = "需要定位權限才能顯示目前位置",
                        retry = { onEvent(MapShellEvent.RetryLocation) },
                        modifier = Modifier.testTag("location-denied"),
                    )
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
        ShellAction(R.drawable.ic_map_menu, "開啟選單", "map-hamburger") { onEvent(MapShellEvent.OpenDrawer) }
        ShellAction(R.drawable.ic_map_search, "關鍵字搜尋", "map-search") { onEvent(MapShellEvent.SearchClicked) }
        ShellAction(R.drawable.ic_map_notifications, "通知", "map-notifications") { onEvent(MapShellEvent.NotificationsClicked) }
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
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_map_location),
            contentDescription = null,
        )
    }
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
            .selectableGroup()
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
                    .selectable(
                        selected = selected,
                        role = Role.RadioButton,
                        onClick = { onEvent(MapShellEvent.SelectBasemap(basemap)) },
                    )
                    .semantics {
                        contentDescription = basemap.label
                    },
                contentAlignment = Alignment.Center,
            ) { Text(basemap.label, color = if (selected) AppThemeTokens.colors.onBrandPrimary else AppThemeTokens.colors.textPrimary) }
        }
    }
}

@Composable
private fun Drawer(state: MapShellUiState, onEvent: (MapShellEvent) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(AppThemeTokens.colors.scrim)
                .testTag("map-drawer-scrim"),
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(304.dp)
                .testTag("map-drawer")
                .background(AppThemeTokens.colors.brandPrimary)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(AppThemeTokens.colors.surface, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = state.identity.displayName.take(1),
                        color = AppThemeTokens.colors.brandPrimary,
                        style = AppThemeTokens.typography.sectionTitle,
                    )
                }
                Column {
                    Text(state.identity.displayName, color = AppThemeTokens.colors.onBrandPrimary, style = AppThemeTokens.typography.sectionTitle)
                    Text(state.identity.role.toString(), color = AppThemeTokens.colors.onBrandPrimary, style = AppThemeTokens.typography.supporting)
                }
            }
            DrawerAction(R.drawable.ic_map_layers, "圖台", "map-platform") { onEvent(MapShellEvent.MapPlatformClicked) }
            DrawerAction(R.drawable.ic_map_dashboard, "儀錶板", "map-dashboard") { onEvent(MapShellEvent.DashboardClicked) }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { onEvent(MapShellEvent.LogoutRequested) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("map-logout"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppThemeTokens.colors.surface,
                    contentColor = AppThemeTokens.colors.brandPrimary,
                ),
            ) {
                Icon(painterResource(R.drawable.ic_map_logout), contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("登出", style = AppThemeTokens.typography.buttonLabel)
            }
            AppSecondaryButton("關閉選單", { onEvent(MapShellEvent.CloseDrawer) }, Modifier.fillMaxWidth().testTag("map-drawer-close"))
        }
    }
}

@Composable
private fun DrawerAction(iconRes: Int, label: String, tag: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag(tag),
        colors = ButtonDefaults.textButtonColors(contentColor = AppThemeTokens.colors.onBrandPrimary),
    ) {
        Icon(painterResource(iconRes), contentDescription = null)
        Spacer(Modifier.width(12.dp))
        Text(label, style = AppThemeTokens.typography.buttonLabel)
    }
}

@Composable
private fun ShellAction(iconRes: Int, description: String, tag: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
            .background(AppThemeTokens.colors.surface, RoundedCornerShape(12.dp))
            .testTag(tag),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(onClick = onClick, modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)) {
            Icon(painter = painterResource(iconRes), contentDescription = description)
        }
    }
}
