package com.example.tp_ncolso_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormCallbacks
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEvent
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormField
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormRoute
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormViewModel
import com.example.tp_ncolso_android.feature.surveyform.data.FakeSurveyFormDataSource
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme

/** Debug-only entry point for restoring an editable survey task from Recents. */
class Ui009StateRestoreActivity : ComponentActivity() {
    internal lateinit var surveyViewModel: SurveyFormViewModel
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                surveyViewModel = rememberDebugViewModel("ui009-process-death-survey") {
                    SurveyFormViewModel("TEST-KEY-002", FakeSurveyFormDataSource(), it)
                }
                LaunchedEffect(surveyViewModel) {
                    val draftMarker = intent.getStringExtra(EXTRA_DRAFT_MARKER)
                    if (savedInstanceState == null && intent.getBooleanExtra(EXTRA_SEED_STATE, false)) {
                        if (draftMarker != null) {
                            surveyViewModel.onEvent(SurveyFormEvent.ChangeField(SurveyFormField.NOTE, draftMarker))
                        }
                        surveyViewModel.onEvent(
                            SurveyFormEvent.CapturePhoto(
                                SurveyFormPhoto(
                                    id = PHOTO_MARKER_ID,
                                    uri = "content://invalid/ui009-process-marker",
                                    capturedAt = PHOTO_MARKER_LABEL,
                                ),
                            ),
                        )
                    }
                }
                SurveyFormRoute(
                    viewModel = surveyViewModel,
                    callbacks = SurveyFormCallbacks(onCloseRequested = { _, _ -> finish() }),
                )
            }
        }
    }

    private companion object {
        const val EXTRA_SEED_STATE = "ui009.seed-state"
        const val EXTRA_DRAFT_MARKER = "ui009.draft-marker"
        const val PHOTO_MARKER_ID = "ui009-process-marker"
        const val PHOTO_MARKER_LABEL = "UI009_PROCESS_DEATH_PHOTO"
    }
}
