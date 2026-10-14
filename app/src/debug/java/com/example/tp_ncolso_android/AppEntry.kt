package com.example.tp_ncolso_android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.session.AppSessionState
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppRadioGroup
import com.example.tp_ncolso_android.ui.foundation.component.AppSecondaryButton
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens
import com.example.tp_ncolso_android.feature.auth.AuthHost
import com.example.tp_ncolso_android.feature.auth.AuthViewModel
import com.example.tp_ncolso_android.feature.auth.DebugAuthDataSource
import com.example.tp_ncolso_android.feature.mapshell.MapShellCallbacks
import com.example.tp_ncolso_android.feature.mapshell.MapShellEvent
import com.example.tp_ncolso_android.feature.mapshell.MapShellRoute
import com.example.tp_ncolso_android.feature.mapshell.MapShellViewModel
import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchCallbacks
import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchEvent
import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchRoute
import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchViewModel
import com.example.tp_ncolso_android.feature.parcelsearch.data.FakeParcelSearchDataSource
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailCallbacks
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailEvent
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailRoute
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailViewModel
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraCallbacks
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraRoute
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraViewModel
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormCallbacks
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEvent
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormRoute
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormViewModel
import com.example.tp_ncolso_android.feature.notifications.NotificationsCallbacks
import com.example.tp_ncolso_android.feature.notifications.NotificationsRoute
import com.example.tp_ncolso_android.feature.notifications.NotificationsViewModel
import com.example.tp_ncolso_android.feature.notifications.data.FakeReturnFlowStore

@Composable
fun AppEntry() {
    val sessionOwner = remember { DebugSessionOwner() }
    val sessionState by sessionOwner.state.collectAsState()
    val authViewModel = rememberDebugViewModel("auth") { AuthViewModel(DebugAuthDataSource()) }
    val coordinator = remember { DebugAuthSessionCoordinator(sessionOwner, authViewModel) }
    var sessionGeneration by rememberSaveable { mutableIntStateOf(0) }
    val login: (AppRole) -> Unit = { role ->
        val wasSignedOut = sessionOwner.state.value is AppSessionState.SignedOut
        coordinator.login(role)
        if (wasSignedOut && sessionOwner.state.value is AppSessionState.SignedIn) {
            sessionGeneration += 1
        }
    }
    val logout: () -> Unit = {
        coordinator.logout()
        sessionGeneration += 1
    }

    AppRoot(
        sessionState = sessionState,
        signedOutContent = {
            DebugSignedOutHost(
                authContent = {
                    AuthHost(
                        viewModel = authViewModel,
                        onLoginSucceeded = { login(AppRole.INVESTIGATOR) },
                        onLogoutRequested = logout,
                    )
                },
                directLoginContent = { DebugDirectLogin(onLogin = login) },
            )
        },
        signedInContent = { identity ->
            DebugSignedInContent(identity = identity, sessionGeneration = sessionGeneration, onLogout = logout)
        },
    )
}

@Composable
internal fun DebugSignedInContent(
    identity: AppIdentity,
    onLogout: () -> Unit,
    onEditSurvey: (String) -> Unit = {},
    sessionGeneration: Int = 0,
) {
    val mapShellViewModel = rememberDebugViewModel("map-shell-${identity.role}") { MapShellViewModel(identity, it) }
    val parcelSearchViewModel = rememberDebugViewModel("parcel-search-${identity.role}") { ParcelSearchViewModel(FakeParcelSearchDataSource(), it) }
    val returnFlowStore = remember(identity, sessionGeneration) { FakeReturnFlowStore() }
    val notificationsViewModel = rememberDebugViewModel("notifications-${identity.role}-$sessionGeneration") { NotificationsViewModel(returnFlowStore, it) }
    var notificationsOpen by rememberSaveable(identity, sessionGeneration) { mutableStateOf(false) }
    var detailFromNotifications by rememberSaveable(identity, sessionGeneration) { mutableStateOf(false) }
    var parcelSearchOpen by rememberSaveable(identity, sessionGeneration) { mutableStateOf(false) }
    var detailKeyNo by rememberSaveable(identity, sessionGeneration) { mutableStateOf<String?>(null) }
    var detailOpen by rememberSaveable(identity, sessionGeneration) { mutableStateOf(false) }
    var surveyKeyNo by rememberSaveable(identity, sessionGeneration) { mutableStateOf<String?>(null) }
    var surveyOpen by rememberSaveable(identity, sessionGeneration) { mutableStateOf(false) }
    var surveyInstance by rememberSaveable(identity, sessionGeneration) { mutableStateOf(0) }
    var cameraOpen by rememberSaveable(identity, sessionGeneration) { mutableStateOf(false) }
    var nextPhotoNumber by rememberSaveable(identity, sessionGeneration) { mutableStateOf(1) }
    val detailViewModel = detailKeyNo?.let { keyNo ->
        rememberDebugViewModel("parcel-detail-$keyNo-$sessionGeneration") { ParcelDetailViewModel(keyNo, returnFlowStore, it) }
    }
    val surveyViewModel = surveyKeyNo?.let { keyNo ->
        rememberDebugViewModel("survey-form-$keyNo-$surveyInstance-$sessionGeneration") { SurveyFormViewModel(keyNo, returnFlowStore, it) }
    }
    val photoCameraViewModel = rememberDebugViewModel("photo-camera-${identity.role}") { PhotoCameraViewModel(it) }

    Box(Modifier.fillMaxSize()) {
        MapShellRoute(
            identity = identity,
            viewModel = mapShellViewModel,
            callbacks = MapShellCallbacks(
                onSearch = { parcelSearchOpen = true },
                onNotifications = { notificationsOpen = true },
                onLogoutRequested = onLogout,
            ),
        )
        if (notificationsOpen) {
            NotificationsRoute(
                viewModel = notificationsViewModel,
                callbacks = NotificationsCallbacks(
                    onClose = { notificationsOpen = false },
                    onParcelSelected = { keyNo ->
                        notificationsOpen = false
                        detailFromNotifications = true
                        detailKeyNo = keyNo
                        detailOpen = true
                    },
                ),
            )
        }
        if (parcelSearchOpen) {
            ParcelSearchRoute(
                viewModel = parcelSearchViewModel,
                callbacks = ParcelSearchCallbacks(
                    onClose = {
                        parcelSearchViewModel.onEvent(ParcelSearchEvent.DismissSummary)
                        parcelSearchOpen = false
                    },
                    onParcelSelected = { query, selectedTarget ->
                        mapShellViewModel.onEvent(MapShellEvent.UpdateSearchContext(query, selectedTarget))
                    },
                    onOpenDetail = { keyNo ->
                        detailKeyNo = keyNo
                        detailFromNotifications = false
                        parcelSearchOpen = false
                        detailOpen = true
                    },
                ),
            )
        }
        if (detailOpen && detailViewModel != null) {
            ParcelDetailRoute(
                viewModel = detailViewModel,
                callbacks = ParcelDetailCallbacks(
                    onClose = {
                        detailOpen = false
                        if (detailFromNotifications) {
                            detailFromNotifications = false
                            notificationsOpen = true
                        } else {
                            parcelSearchOpen = true
                        }
                    },
                    onEditRequested = { keyNo ->
                        onEditSurvey(keyNo)
                        surveyKeyNo = keyNo
                        surveyInstance += 1
                        surveyOpen = true
                    },
                ),
            )
        }
        if (surveyOpen && surveyViewModel != null) {
            SurveyFormRoute(
                viewModel = surveyViewModel,
                backEnabled = !cameraOpen,
                callbacks = SurveyFormCallbacks(
                    onCloseRequested = { _, _ ->
                        cameraOpen = false
                        surveyOpen = false
                        surveyKeyNo = null
                    },
                    onCaptureRequested = { cameraOpen = true },
                    onCorrectionSaved = { savedKeyNo ->
                        cameraOpen = false
                        surveyOpen = false
                        surveyKeyNo = null
                        if (savedKeyNo == detailKeyNo) detailViewModel?.onEvent(ParcelDetailEvent.CorrectionSaved)
                    },
                ),
            )
        }
        if (cameraOpen && surveyViewModel != null) {
            PhotoCameraRoute(
                viewModel = photoCameraViewModel,
                callbacks = PhotoCameraCallbacks(
                    onClose = { cameraOpen = false },
                    onPhotoCaptured = { captured ->
                        val number = nextPhotoNumber++
                        surveyViewModel.onEvent(
                            SurveyFormEvent.CapturePhoto(
                                SurveyFormPhoto(
                                    id = "camera-$number",
                                    uri = captured.uri,
                                    capturedAt = captured.capturedAt,
                                ),
                            ),
                        )
                        cameraOpen = false
                    },
                ),
            )
        }
    }
}

@Composable
private fun DebugSignedOutHost(
    authContent: @Composable () -> Unit,
    directLoginContent: @Composable () -> Unit,
) {
    var showDirectLogin by rememberSaveable { mutableStateOf(false) }
    Box(Modifier.fillMaxSize()) {
        if (showDirectLogin) directLoginContent() else authContent()
        TextButton(
            onClick = { showDirectLogin = !showDirectLogin },
            modifier = Modifier.padding(top = 4.dp, end = 4.dp),
        ) { Text(if (showDirectLogin) "返回登入頁" else "開發模式") }
    }
}

@Composable
private fun DebugDirectLogin(
    onLogin: (AppRole) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedRole by rememberSaveable { mutableStateOf(AppRole.INVESTIGATOR) }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(AppThemeTokens.spacing.lg)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.md),
        ) {
            Text("開發模式", style = AppThemeTokens.typography.screenTitle)
            Text("API 尚未完成時，可使用假角色進入後續畫面。", style = AppThemeTokens.typography.body)
            AppRadioGroup(
                options = AppRole.entries,
                selected = selectedRole,
                onSelect = { selectedRole = it },
                itemLabel = ::roleLabel,
                label = "選擇開發角色",
                modifier = Modifier.testTag("debug-role-options"),
            )
            Spacer(Modifier.height(AppThemeTokens.spacing.sm))
            AppPrimaryButton(
                text = "直接進入",
                onClick = { onLogin(selectedRole) },
                modifier = Modifier.fillMaxWidth().testTag("debug-direct-login"),
            )
        }
    }
}

@Composable
private fun DebugAuthenticatedContent(
    identity: AppIdentity,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(AppThemeTokens.spacing.lg)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppThemeTokens.spacing.md),
        ) {
            Text("已進入開發受保護內容", style = AppThemeTokens.typography.screenTitle)
            Text("使用者：${identity.displayName}", style = AppThemeTokens.typography.body)
            Text("角色：${roleLabel(identity.role)}", style = AppThemeTokens.typography.body)
            AppSecondaryButton(
                text = "登出開發模式",
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().testTag("debug-logout"),
            )
        }
    }
}

fun roleLabel(role: AppRole): String = when (role) {
    AppRole.INVESTIGATOR -> "調查人員"
    AppRole.INTERNAL_STAFF -> "內業人員"
    AppRole.ADMINISTRATOR -> "管理者"
}
