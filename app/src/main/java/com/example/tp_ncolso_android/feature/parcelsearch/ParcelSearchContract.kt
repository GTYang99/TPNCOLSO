package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.compose.runtime.Immutable
import com.example.tp_ncolso_android.feature.parcelsearch.data.ParcelSearchParcel

@Immutable
data class ParcelSearchUiState(
    val query: String = "",
    val searchSubmitted: Boolean = false,
    val selectedParcel: ParcelSearchParcel? = null,
    val noResults: Boolean = false,
)

sealed interface ParcelSearchEvent {
    data class QueryChanged(val value: String) : ParcelSearchEvent
    data object SearchSubmitted : ParcelSearchEvent
    data object SummaryDismissed : ParcelSearchEvent
}
