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
import com.example.tp_ncolso_android.feature.mapshell.Basemap
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
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailTab
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailRoute
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailViewModel
import com.example.tp_ncolso_android.feature.parceldetail.data.FakeParcelDetailDataSource
import com.example.tp_ncolso_android.session.AppIdentity
import com.example.tp_ncolso_android.session.AppRole
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme

/** Debug-only entry point for restoring map, search and detail UI state from Recents. */
class Ui009StatefulRouteRestoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val identity = AppIdentity("測試人員", AppRole.INVESTIGATOR)
                val mapViewModel = rememberDebugViewModel("ui009-route-map") { MapShellViewModel(identity, it) }
                val searchViewModel = rememberDebugViewModel("ui009-route-search") {
                    ParcelSearchViewModel(FakeParcelSearchDataSource(), it)
                }
                var searchOpen by rememberSaveable { mutableStateOf(false) }
                var detailKeyNo by rememberSaveable { mutableStateOf<String?>(null) }
                var detailOpen by rememberSaveable { mutableStateOf(false) }
                val detailViewModel = detailKeyNo?.let { keyNo ->
                    rememberDebugViewModel("ui009-route-detail-$keyNo") {
                        ParcelDetailViewModel(keyNo, FakeParcelDetailDataSource(), it)
                    }
                }

                LaunchedEffect(Unit) {
                    if (savedInstanceState == null && intent.getBooleanExtra(EXTRA_SEED_STATE, false)) {
                        mapViewModel.onEvent(MapShellEvent.SelectBasemap(Basemap.ORTHOPHOTO))
                        mapViewModel.onEvent(MapShellEvent.UpdateSearchContext(SEARCH_QUERY, SELECTED_TARGET))
                        searchViewModel.onEvent(ParcelSearchEvent.QueryChanged(SEARCH_QUERY))
                        searchViewModel.onEvent(ParcelSearchEvent.SubmitQuery)
                        detailKeyNo = SEARCH_QUERY
                        detailOpen = true
                    }
                }

                LaunchedEffect(detailViewModel) {
                    if (savedInstanceState == null && detailViewModel != null) {
                        detailViewModel.onEvent(ParcelDetailEvent.SelectTab(ParcelDetailTab.SURVEY))
                        detailViewModel.onEvent(ParcelDetailEvent.SelectPeriod(HISTORY_PERIOD_ID))
                    }
                }

                Box(Modifier.fillMaxSize()) {
                    MapShellRoute(
                        identity = identity,
                        viewModel = mapViewModel,
                        callbacks = MapShellCallbacks(onSearch = { searchOpen = true }),
                    )
                    if (searchOpen) {
                        ParcelSearchRoute(
                            viewModel = searchViewModel,
                            callbacks = ParcelSearchCallbacks(
                                onClose = {
                                    searchViewModel.onEvent(ParcelSearchEvent.DismissSummary)
                                    searchOpen = false
                                },
                                onParcelSelected = { query, selectedTarget ->
                                    mapViewModel.onEvent(MapShellEvent.UpdateSearchContext(query, selectedTarget))
                                },
                                onOpenDetail = { keyNo ->
                                    detailKeyNo = keyNo
                                    searchOpen = false
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
                                    searchOpen = true
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
        const val SEARCH_QUERY = "TEST-KEY-001"
        const val SELECTED_TARGET = "測試地塊 T-001"
        const val HISTORY_PERIOD_ID = "2025-history"
    }
}
