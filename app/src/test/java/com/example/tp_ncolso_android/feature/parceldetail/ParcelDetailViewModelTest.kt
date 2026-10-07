package com.example.tp_ncolso_android.feature.parceldetail

import com.example.tp_ncolso_android.feature.parceldetail.data.FakeParcelDetailDataSource
import com.example.tp_ncolso_android.feature.parceldetail.data.ParcelDetailDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParcelDetailViewModelTest {
    @Test
    fun detailLoadsLandTabAndLatestSurveyByDefault() {
        val viewModel = ParcelDetailViewModel("TEST-KEY-001", FakeParcelDetailDataSource())

        assertEquals(ParcelDetailLoadState.CONTENT, viewModel.state.value.loadState)
        assertEquals(ParcelDetailTab.LAND_DATA, viewModel.state.value.selectedTab)
        assertEquals("2026-latest", viewModel.state.value.selectedPeriodId)
        assertFalse(viewModel.state.value.isHistoryReadOnly)
        assertEquals(8, viewModel.state.value.detail?.landMarkingFields?.size)
        assertEquals(2, viewModel.state.value.detail?.gisFields?.size)
        assertEquals(10, viewModel.state.value.selectedSurvey?.let { 10 })
    }

    @Test
    fun selectingHistoryKeepsRecordReadOnlyAndCanReturnToLatest() {
        val viewModel = ParcelDetailViewModel("TEST-KEY-001", FakeParcelDetailDataSource())

        viewModel.onEvent(ParcelDetailEvent.SelectTab(ParcelDetailTab.SURVEY))
        viewModel.onEvent(ParcelDetailEvent.SelectPeriod("2025-history"))

        assertEquals(ParcelDetailTab.SURVEY, viewModel.state.value.selectedTab)
        assertEquals("2025-history", viewModel.state.value.selectedPeriodId)
        assertTrue(viewModel.state.value.isHistoryReadOnly)
        assertEquals("歷史調查人員", viewModel.state.value.selectedSurvey?.surveyUser)

        viewModel.onEvent(ParcelDetailEvent.SelectPeriod("2026-latest"))
        assertFalse(viewModel.state.value.isHistoryReadOnly)
        assertEquals("尚未調查", viewModel.state.value.selectedSurvey?.surveyUser)
    }

    @Test
    fun requestEditEmitsUi006HandoffWithoutMutatingDetail() = runTest {
        val viewModel = ParcelDetailViewModel("TEST-KEY-002", FakeParcelDetailDataSource())
        val effect = async { viewModel.effect.first() }
        val before = viewModel.state.value

        viewModel.onEvent(ParcelDetailEvent.RequestEdit)

        assertEquals(ParcelDetailEffect.EditRequested("TEST-KEY-002"), effect.await())
        assertEquals(before, viewModel.state.value)
    }

    @Test
    fun alternateSourceAndMissingKeyRemainFeatureOwned() {
        val alternate = object : ParcelDetailDataSource {
            override fun findDetail(keyNo: String): ParcelDetailRecord? = null
        }
        val viewModel = ParcelDetailViewModel("UNKNOWN", alternate)

        assertEquals(ParcelDetailLoadState.ERROR, viewModel.state.value.loadState)
        assertNull(viewModel.state.value.detail)
        assertEquals("找不到土地詳情資料", viewModel.state.value.errorMessage)
    }
}
