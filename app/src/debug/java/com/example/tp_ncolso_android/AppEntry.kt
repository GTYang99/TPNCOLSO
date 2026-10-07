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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.component.AppPrimaryButton
import com.example.tp_ncolso_android.ui.foundation.component.AppRadioGroup
import com.example.tp_ncolso_android.ui.foundation.component.AppSecondaryButton
import com.example.tp_ncolso_android.ui.foundation.theme.AppThemeTokens
import com.example.tp_ncolso_android.feature.auth.AuthHost
import com.example.tp_ncolso_android.feature.auth.AuthViewModel
import com.example.tp_ncolso_android.feature.auth.DebugAuthDataSource
import com.example.tp_ncolso_android.feature.auth.DebugCaptchaProvider
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
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailRoute
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailViewModel
import com.example.tp_ncolso_android.feature.parceldetail.data.FakeParcelDetailDataSource
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraCallbacks
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraRoute
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraViewModel
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormCallbacks
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEvent
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormRoute
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormViewModel
import com.example.tp_ncolso_android.feature.surveyform.data.FakeSurveyFormDataSource

@Composable
fun AppEntry() {
    val sessionOwner = remember { DebugSessionOwner() }
    val sessionState by sessionOwner.state.collectAsState()
    val authViewModel = remember { AuthViewModel(DebugAuthDataSource(), DebugCaptchaProvider()) }
    val coordinator = remember { DebugAuthSessionCoordinator(sessionOwner, authViewModel) }

    AppRoot(
        sessionState = sessionState,
        signedOutContent = {
            DebugSignedOutHost(
                authContent = {
                    AuthHost(
                        viewModel = authViewModel,
                        onLoginSucceeded = { coordinator.login(AppRole.INVESTIGATOR) },
                        onLogoutRequested = coordinator::logout,
                        captchaVisual = { modifier -> Image(painterResource(com.example.tp_ncolso_android.R.drawable.login_captcha_fixture), contentDescription = "驗證碼圖片", modifier = modifier) },
                    )
                },
                directLoginContent = { DebugDirectLogin(onLogin = coordinator::login) },
            )
        },
        signedInContent = { identity ->
            DebugSignedInContent(identity = identity, onLogout = coordinator::logout)
        },
    )
}

@Composable
internal fun DebugSignedInContent(
    identity: AppIdentity,
    onLogout: () -> Unit,
    onEditSurvey: (String) -> Unit = {},
) {
    val mapShellViewModel = remember(identity) { MapShellViewModel(identity) }
    val parcelSearchViewModel = remember(identity) { ParcelSearchViewModel(FakeParcelSearchDataSource()) }
    var parcelSearchOpen by rememberSaveable(identity) { mutableStateOf(false) }
    var detailKeyNo by rememberSaveable(identity) { mutableStateOf<String?>(null) }
    var detailOpen by rememberSaveable(identity) { mutableStateOf(false) }
    var surveyKeyNo by rememberSaveable(identity) { mutableStateOf<String?>(null) }
    var surveyOpen by rememberSaveable(identity) { mutableStateOf(false) }
    var cameraOpen by rememberSaveable(identity) { mutableStateOf(false) }
    var nextPhotoNumber by rememberSaveable(identity) { mutableStateOf(1) }
    val detailViewModel = remember(identity, detailKeyNo) {
        detailKeyNo?.let { ParcelDetailViewModel(it, FakeParcelDetailDataSource()) }
    }
    val surveyViewModel = remember(identity, surveyKeyNo) {
        surveyKeyNo?.let { SurveyFormViewModel(it, FakeSurveyFormDataSource()) }
    }
    val photoCameraViewModel = remember(identity) { PhotoCameraViewModel() }

    Box(Modifier.fillMaxSize()) {
        MapShellRoute(
            identity = identity,
            viewModel = mapShellViewModel,
            callbacks = MapShellCallbacks(
                onSearch = { parcelSearchOpen = true },
                onLogoutRequested = onLogout,
            ),
        )
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
                        parcelSearchOpen = true
                    },
                    onEditRequested = { keyNo ->
                        onEditSurvey(keyNo)
                        surveyKeyNo = keyNo
                        surveyOpen = true
                    },
                ),
            )
        }
        if (surveyOpen && surveyViewModel != null) {
            SurveyFormRoute(
                viewModel = surveyViewModel,
                callbacks = SurveyFormCallbacks(
                    onCloseRequested = { _, _ ->
                        cameraOpen = false
                        surveyOpen = false
                    },
                    onCaptureRequested = { cameraOpen = true },
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
