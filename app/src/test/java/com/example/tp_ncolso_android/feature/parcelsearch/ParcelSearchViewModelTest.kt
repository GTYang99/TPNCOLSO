package com.example.tp_ncolso_android.feature.parcelsearch

import com.example.tp_ncolso_android.feature.parcelsearch.data.FakeParcelSearchDataSource
import com.example.tp_ncolso_android.feature.parcelsearch.data.ParcelSearchDataSource
import com.example.tp_ncolso_android.feature.parcelsearch.data.ParcelSearchParcel
import com.example.tp_ncolso_android.feature.mapshell.Basemap
import com.example.tp_ncolso_android.feature.mapshell.LocationState
import com.example.tp_ncolso_android.feature.mapshell.MapShellEvent
import com.example.tp_ncolso_android.feature.mapshell.MapShellViewModel
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ParcelSearchViewModelTest {
    @Test
    fun fakeSourceMatchesKeyLandNumberAndLocationCaseInsensitively() {
        val source = FakeParcelSearchDataSource()

        assertEquals("KS-10001", source.search("ks-10001").single().keyNo)
        assertEquals("KS-10001", source.search("中正段 0012").single().keyNo)
        assertEquals("KS-10001", source.search("臺北市中正區").single().keyNo)
    }

    @Test
    fun queryRemainsVisibleAfterNoMatchAndDismissal() {
        val viewModel = ParcelSearchViewModel()
        val query = "沒有這筆土地"

        viewModel.onEvent(ParcelSearchEvent.QueryChanged(query))
        viewModel.onEvent(ParcelSearchEvent.SearchSubmitted)

        assertEquals(query, viewModel.state.value.query)
        assertTrue(viewModel.state.value.searchSubmitted)
        assertTrue(viewModel.state.value.noResults)

        viewModel.onEvent(ParcelSearchEvent.SummaryDismissed)

        assertEquals(query, viewModel.state.value.query)
        assertFalse(viewModel.state.value.noResults)
    }

    @Test
    fun matchingQuerySelectsParcelAndDismissesOnlySummary() {
        val viewModel = ParcelSearchViewModel()

        viewModel.onEvent(ParcelSearchEvent.QueryChanged("KS-10001"))
        viewModel.onEvent(ParcelSearchEvent.SearchSubmitted)

        assertEquals("KS-10001", viewModel.state.value.selectedParcel?.keyNo)
        assertFalse(viewModel.state.value.noResults)

        viewModel.onEvent(ParcelSearchEvent.SummaryDismissed)

        assertNull(viewModel.state.value.selectedParcel)
        assertEquals("KS-10001", viewModel.state.value.query)
        assertTrue(viewModel.state.value.searchSubmitted)
    }

    @Test
    fun featureAcceptsReplaceableDataSource() {
        val replacement = ParcelSearchParcel(
            keyNo = "TEST-1",
            landNo = "測試段 1-1",
            location = "測試市測試區",
            status = "測試狀態",
            siteCondition = "測試現況",
            selectedTarget = "測試地塊",
        )
        val source = ParcelSearchDataSource { keyword -> if (keyword == "replacement") listOf(replacement) else emptyList() }
        val viewModel = ParcelSearchViewModel(source)

        viewModel.onEvent(ParcelSearchEvent.QueryChanged("replacement"))
        viewModel.onEvent(ParcelSearchEvent.SearchSubmitted)

        assertEquals(replacement, viewModel.state.value.selectedParcel)
    }

    @Test
    fun mapSearchContextUpdatesOnlyQueryAndSelectedTarget() {
        val identity = AppIdentity("測試人員", AppRole.INVESTIGATOR)
        val viewModel = MapShellViewModel(identity)
        viewModel.onEvent(MapShellEvent.SelectBasemap(Basemap.TERRAIN))
        viewModel.onEvent(MapShellEvent.SetLocationState(LocationState.PERMISSION_DENIED))
        viewModel.onEvent(MapShellEvent.OpenDrawer)
        val before = viewModel.state.value

        viewModel.onEvent(MapShellEvent.UpdateSearchContext("中正段", "地塊 A-001"))

        val after = viewModel.state.value
        assertEquals("中正段", after.overlay.activeQuery)
        assertEquals("地塊 A-001", after.overlay.selectedTarget)
        assertEquals(before.overlay.label, after.overlay.label)
        assertEquals(before.overlay.userLocationMarker, after.overlay.userLocationMarker)
        assertEquals(before.selectedBasemap, after.selectedBasemap)
        assertEquals(before.locationState, after.locationState)
        assertEquals(before.drawerOpen, after.drawerOpen)
        assertEquals(before.identity, after.identity)
    }
}
