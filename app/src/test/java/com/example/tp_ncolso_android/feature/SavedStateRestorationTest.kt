package com.example.tp_ncolso_android.feature

import androidx.lifecycle.SavedStateHandle
import com.example.tp_ncolso_android.feature.mapshell.Basemap
import com.example.tp_ncolso_android.feature.mapshell.MapShellEvent
import com.example.tp_ncolso_android.feature.mapshell.MapShellViewModel
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailEvent
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailTab
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailViewModel
import com.example.tp_ncolso_android.feature.parceldetail.data.FakeParcelDetailDataSource
import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchEvent
import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchOutcome
import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchViewModel
import com.example.tp_ncolso_android.feature.parcelsearch.data.FakeParcelSearchDataSource
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraEvent
import com.example.tp_ncolso_android.feature.photocamera.PhotoCameraViewModel
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormEvent
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormField
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormViewModel
import com.example.tp_ncolso_android.feature.surveyform.data.FakeSurveyFormDataSource
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SavedStateRestorationTest {
    @Test
    fun mapRestoresBasemapAndSearchContextButNotTransientLocationState() {
        val handle = SavedStateHandle()
        val identity = AppIdentity("測試人員", AppRole.INVESTIGATOR)
        MapShellViewModel(identity, handle).apply {
            onEvent(MapShellEvent.SelectBasemap(Basemap.ORTHOPHOTO))
            onEvent(MapShellEvent.UpdateSearchContext("TEST-KEY-001", "測試地塊 T-001"))
            onEvent(MapShellEvent.LocationClicked)
        }

        val restored = MapShellViewModel(identity, handle).state.value
        assertEquals(Basemap.ORTHOPHOTO, restored.selectedBasemap)
        assertEquals("TEST-KEY-001", restored.overlay.activeQuery)
        assertEquals("測試地塊 T-001", restored.overlay.selectedTarget)
        assertEquals(com.example.tp_ncolso_android.feature.mapshell.LocationState.NORMAL, restored.locationState)
    }

    @Test
    fun searchRestoresQueryAndRecomputesResultFromFakeSource() {
        val handle = SavedStateHandle()
        ParcelSearchViewModel(FakeParcelSearchDataSource(), handle).apply {
            onEvent(ParcelSearchEvent.QueryChanged("TEST-KEY-001"))
            onEvent(ParcelSearchEvent.SubmitQuery)
        }

        val restored = ParcelSearchViewModel(FakeParcelSearchDataSource(), handle).state.value
        assertEquals("TEST-KEY-001", restored.query)
        assertEquals(ParcelSearchOutcome.MATCHED, restored.outcome)
        assertEquals("TEST-KEY-001", restored.selectedParcel?.keyNo)
        assertTrue(restored.summaryVisible)
    }

    @Test
    fun detailRestoresTabAndOnlyValidPeriodSelection() {
        val handle = SavedStateHandle()
        ParcelDetailViewModel("TEST-KEY-001", FakeParcelDetailDataSource(), handle).apply {
            onEvent(ParcelDetailEvent.SelectTab(ParcelDetailTab.SURVEY))
            onEvent(ParcelDetailEvent.SelectPeriod("2025-history"))
        }

        val restored = ParcelDetailViewModel("TEST-KEY-001", FakeParcelDetailDataSource(), handle).state.value
        assertEquals(ParcelDetailTab.SURVEY, restored.selectedTab)
        assertEquals("2025-history", restored.selectedPeriodId)
    }

    @Test
    fun surveyRestoresDraftAndPhotoReferencesWithoutDurableStorage() {
        val handle = SavedStateHandle()
        val source = FakeSurveyFormDataSource()
        SurveyFormViewModel("TEST-KEY-001", source, handle).apply {
            onEvent(SurveyFormEvent.ChangeField(SurveyFormField.NOTE, "程序重建後保留"))
            onEvent(SurveyFormEvent.ToggleSiteCondition("建物"))
            onEvent(SurveyFormEvent.CapturePhoto(SurveyFormPhoto("photo-1", "content://local/photo-1", "2026-10-08")))
        }

        val restored = SurveyFormViewModel("TEST-KEY-001", source, handle).state.value
        assertEquals("程序重建後保留", restored.values.note)
        assertEquals(listOf("建物"), restored.values.siteConditions)
        assertEquals("content://local/photo-1", restored.photos.single().uri)
        assertTrue(restored.dirty)
    }

    @Test
    fun cameraResetsRuntimePermissionAndCaptureStateWhileKeepingLocalError() {
        val handle = SavedStateHandle()
        PhotoCameraViewModel(handle).apply {
            onEvent(PhotoCameraEvent.PermissionResult(true))
            onEvent(PhotoCameraEvent.CaptureStarted)
            onEvent(PhotoCameraEvent.CaptureFailed("相機暫時無法使用"))
        }

        val restored = PhotoCameraViewModel(handle).state.value
        assertEquals(com.example.tp_ncolso_android.feature.photocamera.CameraPermissionStatus.UNKNOWN, restored.permission)
        assertEquals(com.example.tp_ncolso_android.feature.photocamera.PhotoCameraStatus.READY, restored.status)
        assertEquals("相機暫時無法使用", restored.errorMessage)
    }
}
