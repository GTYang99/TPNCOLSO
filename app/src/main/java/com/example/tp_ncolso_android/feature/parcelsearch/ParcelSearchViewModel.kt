package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.lifecycle.ViewModel
import com.example.tp_ncolso_android.feature.parcelsearch.data.ParcelSearchDataSource
import com.example.tp_ncolso_android.feature.parcelsearch.data.FakeParcelSearchDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ParcelSearchViewModel(
    private val dataSource: ParcelSearchDataSource = FakeParcelSearchDataSource(),
) : ViewModel() {
    private val mutableState = MutableStateFlow(ParcelSearchUiState())
    val state: StateFlow<ParcelSearchUiState> = mutableState.asStateFlow()

    fun onEvent(event: ParcelSearchEvent) {
        when (event) {
            is ParcelSearchEvent.QueryChanged -> {
                mutableState.value = mutableState.value.copy(
                    query = event.value,
                    searchSubmitted = false,
                    selectedParcel = null,
                    noResults = false,
                )
            }

            ParcelSearchEvent.SearchSubmitted -> submitSearch()
            ParcelSearchEvent.SummaryDismissed -> {
                mutableState.value = mutableState.value.copy(selectedParcel = null, noResults = false)
            }
        }
    }

    private fun submitSearch() {
        val current = mutableState.value
        if (current.query.isBlank()) {
            mutableState.value = current.copy(searchSubmitted = false, selectedParcel = null, noResults = false)
            return
        }

        val match = dataSource.search(current.query).firstOrNull()
        mutableState.value = current.copy(
            searchSubmitted = true,
            selectedParcel = match,
            noResults = match == null,
        )
    }
}
