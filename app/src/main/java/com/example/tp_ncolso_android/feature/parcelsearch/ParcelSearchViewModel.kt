package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.lifecycle.ViewModel
import com.example.tp_ncolso_android.feature.parcelsearch.data.ParcelSearchDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ParcelSearchViewModel(
    private val dataSource: ParcelSearchDataSource,
) : ViewModel() {
    private val mutableState = MutableStateFlow(ParcelSearchUiState())
    val state: StateFlow<ParcelSearchUiState> = mutableState.asStateFlow()

    private val effects = Channel<ParcelSearchEffect>(capacity = Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    fun onEvent(event: ParcelSearchEvent) {
        when (event) {
            is ParcelSearchEvent.QueryChanged -> mutableState.value = mutableState.value.copy(
                query = event.value,
                outcome = ParcelSearchOutcome.IDLE,
                selectedParcel = null,
                summaryVisible = false,
            )
            ParcelSearchEvent.SubmitQuery -> submitQuery()
            ParcelSearchEvent.DismissSummary -> mutableState.value = mutableState.value.copy(summaryVisible = false)
        }
    }

    private fun submitQuery() {
        val query = mutableState.value.query
        if (query.isBlank()) {
            mutableState.value = mutableState.value.copy(
                outcome = ParcelSearchOutcome.IDLE,
                selectedParcel = null,
                summaryVisible = false,
            )
            return
        }

        val parcel = dataSource.findParcel(query)
        if (parcel == null) {
            mutableState.value = mutableState.value.copy(
                outcome = ParcelSearchOutcome.NO_MATCH,
                selectedParcel = null,
                summaryVisible = false,
            )
            return
        }

        mutableState.value = mutableState.value.copy(
            outcome = ParcelSearchOutcome.MATCHED,
            selectedParcel = parcel,
            summaryVisible = true,
        )
        effects.trySend(ParcelSearchEffect.ParcelSelected(query, parcel.selectedTarget))
    }
}
