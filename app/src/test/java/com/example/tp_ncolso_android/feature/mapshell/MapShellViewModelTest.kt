package com.example.tp_ncolso_android.feature.mapshell

import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MapShellViewModelTest {
    private val identity = AppIdentity("測試人員", AppRole.INVESTIGATOR)

    @Test fun defaultStateSelectsElectronicMap() {
        val viewModel = MapShellViewModel(identity)

        assertEquals(Basemap.ELECTRONIC, viewModel.state.value.selectedBasemap)
        assertEquals(false, viewModel.state.value.drawerOpen)
    }

    @Test fun selectingBasemapPreservesUiOnlyOverlayContext() {
        val viewModel = MapShellViewModel(identity)
        val initialOverlay = viewModel.state.value.overlay

        viewModel.onEvent(MapShellEvent.SelectBasemap(Basemap.TERRAIN))

        assertEquals(Basemap.TERRAIN, viewModel.state.value.selectedBasemap)
        assertEquals(initialOverlay, viewModel.state.value.overlay)
    }

    @Test fun locationTransitionsAreUiOnly() {
        val viewModel = MapShellViewModel(identity)

        viewModel.onEvent(MapShellEvent.LocationClicked)
        assertEquals(LocationState.LOADING, viewModel.state.value.locationState)
        viewModel.onEvent(MapShellEvent.SetLocationState(LocationState.PERMISSION_DENIED))
        assertEquals(LocationState.PERMISSION_DENIED, viewModel.state.value.locationState)
        viewModel.onEvent(MapShellEvent.RetryLocation)
        assertEquals(LocationState.LOADING, viewModel.state.value.locationState)
    }

    @Test fun logoutEffectIsEmittedOnceForRepeatedEvents() = runTest {
        val viewModel = MapShellViewModel(identity)
        val firstEffect = async { viewModel.effect.first() }

        viewModel.onEvent(MapShellEvent.LogoutRequested)
        viewModel.onEvent(MapShellEvent.LogoutRequested)

        assertEquals(MapShellEffect.LogoutRequested, firstEffect.await())
        assertEquals(false, viewModel.state.value.drawerOpen)
        assertNull(withTimeoutOrNull(100) { viewModel.effect.first() })
    }
}
