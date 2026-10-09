package com.example.tp_ncolso_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraCallbacks
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraRoute
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraViewModel
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormCallbacks
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEvent
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormField
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormRoute
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormViewModel
import com.example.tp_ncolso_android.feature.surveyform.data.FakeSurveyFormDataSource
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme

/** Debug-only entry point for restoring the camera route and its owning survey from Recents. */
class Ui009CameraStateRestoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val surveyViewModel = rememberDebugViewModel("ui009-camera-process-survey") {
                    SurveyFormViewModel("TEST-KEY-002", FakeSurveyFormDataSource(), it)
                }
                val cameraViewModel = rememberDebugViewModel("ui009-camera-process-camera") {
                    PhotoCameraViewModel(it)
                }
                var cameraOpen by rememberSaveable { mutableStateOf(true) }

                LaunchedEffect(surveyViewModel) {
                    if (savedInstanceState == null && intent.getBooleanExtra(EXTRA_SEED_STATE, false)) {
                        surveyViewModel.onEvent(
                            SurveyFormEvent.ChangeField(SurveyFormField.NOTE, intent.getStringExtra(EXTRA_DRAFT_MARKER).orEmpty()),
                        )
                        surveyViewModel.onEvent(
                            SurveyFormEvent.CapturePhoto(
                                SurveyFormPhoto(
                                    id = PHOTO_MARKER_ID,
                                    uri = "content://invalid/ui009-camera-process-marker",
                                    capturedAt = PHOTO_MARKER_LABEL,
                                ),
                            ),
                        )
                    }
                }

                Box(Modifier.fillMaxSize()) {
                    SurveyFormRoute(
                        viewModel = surveyViewModel,
                        callbacks = SurveyFormCallbacks(onCloseRequested = { _, _ -> finish() }),
                    )
                    if (cameraOpen) {
                        PhotoCameraRoute(
                            viewModel = cameraViewModel,
                            callbacks = PhotoCameraCallbacks(
                                onClose = { cameraOpen = false },
                                onPhotoCaptured = { photo ->
                                    surveyViewModel.onEvent(
                                        SurveyFormEvent.CapturePhoto(
                                            SurveyFormPhoto(
                                                id = "ui009-camera-captured",
                                                uri = photo.uri,
                                                capturedAt = photo.capturedAt,
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
        }
    }

    private companion object {
        const val EXTRA_SEED_STATE = "ui009.seed-state"
        const val EXTRA_DRAFT_MARKER = "ui009.draft-marker"
        const val PHOTO_MARKER_ID = "ui009-camera-process-photo"
        const val PHOTO_MARKER_LABEL = "UI009_CAMERA_PROCESS_PHOTO"
    }
}
