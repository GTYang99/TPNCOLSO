package com.example.tp_ncolso_android.feature.parcelsearch

import com.example.tp_ncolso_android.feature.parcelsearch.data.FakeParcelSearchDataSource
import com.example.tp_ncolso_android.feature.parcelsearch.data.ParcelSearchDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParcelSearchViewModelTest {
    @Test fun matchingSearchRetainsQueryAndEmitsOnlyGenericMapHandoff() = runTest {
        val viewModel = ParcelSearchViewModel(FakeParcelSearchDataSource())
        val selected = async { viewModel.effect.first() }

        viewModel.onEvent(ParcelSearchEvent.QueryChanged("TEST-KEY-001"))
        viewModel.onEvent(ParcelSearchEvent.SubmitQuery)

        assertEquals("TEST-KEY-001", viewModel.state.value.query)
        assertEquals(ParcelSearchOutcome.MATCHED, viewModel.state.value.outcome)
        assertEquals("TEST-KEY-001", viewModel.state.value.selectedParcel?.keyNo)
        assertTrue(viewModel.state.value.summaryVisible)
        assertEquals(
            ParcelSearchEffect.ParcelSelected("TEST-KEY-001", "測試地塊 T-001"),
            selected.await(),
        )
    }

    @Test fun noMatchRetainsQueryAndDoesNotExposeAParcel() {
        val viewModel = ParcelSearchViewModel(FakeParcelSearchDataSource())
        viewModel.onEvent(ParcelSearchEvent.QueryChanged("未知地號 88"))

        viewModel.onEvent(ParcelSearchEvent.SubmitQuery)

        assertEquals("未知地號 88", viewModel.state.value.query)
        assertEquals(ParcelSearchOutcome.NO_MATCH, viewModel.state.value.outcome)
        assertNull(viewModel.state.value.selectedParcel)
        assertFalse(viewModel.state.value.summaryVisible)
    }

    @Test fun summaryCanBeDismissedWithoutClearingQueryOrSelectedParcel() {
        val viewModel = ParcelSearchViewModel(FakeParcelSearchDataSource())
        viewModel.onEvent(ParcelSearchEvent.QueryChanged("TEST-KEY-001"))
        viewModel.onEvent(ParcelSearchEvent.SubmitQuery)

        viewModel.onEvent(ParcelSearchEvent.DismissSummary)

        assertEquals("TEST-KEY-001", viewModel.state.value.query)
        assertEquals("TEST-KEY-001", viewModel.state.value.selectedParcel?.keyNo)
        assertFalse(viewModel.state.value.summaryVisible)
    }

    @Test fun viewModelAcceptsAnAlternateDataSourceImplementation() {
        val expected = ParcelSearchRecord(
            selectedTarget = "自訂測試目標",
            keyNo = "CUSTOM-KEY",
            landNo = "自訂地號",
            landLocation = "自訂坐落",
            status = "未調查",
            siteCondition = "無占用",
        )
        val alternateSource = object : ParcelSearchDataSource {
            override fun findParcel(keyword: String): ParcelSearchRecord? = expected.takeIf { keyword == "custom" }
        }
        val viewModel = ParcelSearchViewModel(alternateSource)

        viewModel.onEvent(ParcelSearchEvent.QueryChanged("custom"))
        viewModel.onEvent(ParcelSearchEvent.SubmitQuery)

        assertEquals(expected, viewModel.state.value.selectedParcel)
        assertEquals(ParcelSearchOutcome.MATCHED, viewModel.state.value.outcome)
    }
}
