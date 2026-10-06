package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.compose.runtime.Immutable

@Immutable
data class ParcelSearchRecord(
    val selectedTarget: String,
    val keyNo: String,
    val landNo: String,
    val landLocation: String,
    val status: String,
    val siteCondition: String,
)

enum class ParcelSearchOutcome {
    IDLE,
    MATCHED,
    NO_MATCH,
}

@Immutable
data class ParcelSearchUiState(
    val query: String = "",
    val outcome: ParcelSearchOutcome = ParcelSearchOutcome.IDLE,
    val selectedParcel: ParcelSearchRecord? = null,
    val summaryVisible: Boolean = false,
)

sealed interface ParcelSearchEvent {
    data class QueryChanged(val value: String) : ParcelSearchEvent
    data object SubmitQuery : ParcelSearchEvent
    data object DismissSummary : ParcelSearchEvent
}

sealed interface ParcelSearchEffect {
    data class ParcelSelected(val query: String, val selectedTarget: String) : ParcelSearchEffect
}
