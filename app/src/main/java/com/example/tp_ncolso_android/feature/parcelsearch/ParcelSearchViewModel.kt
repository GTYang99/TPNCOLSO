package com.example.tp_ncolso_android.feature.parcelsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.tp_ncolso_android.feature.parcelsearch.data.ParcelSearchDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ParcelSearchViewModel(
    private val dataSource: ParcelSearchDataSource,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {
    private val mutableState = MutableStateFlow(restoreState())
    val state: StateFlow<ParcelSearchUiState> = mutableState.asStateFlow()

    private val effects = Channel<ParcelSearchEffect>(capacity = Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    fun onEvent(event: ParcelSearchEvent) {
        when (event) {
            is ParcelSearchEvent.QueryChanged -> update(mutableState.value.copy(
                query = event.value,
                outcome = ParcelSearchOutcome.IDLE,
                selectedParcel = null,
                summaryVisible = false,
            ))
            ParcelSearchEvent.SubmitQuery -> submitQuery()
            ParcelSearchEvent.DismissSummary -> update(mutableState.value.copy(summaryVisible = false))
        }
    }

    private fun submitQuery() {
        val query = mutableState.value.query
        if (query.isBlank()) {
            update(mutableState.value.copy(
                outcome = ParcelSearchOutcome.IDLE,
                selectedParcel = null,
                summaryVisible = false,
            ))
            return
        }

        val parcel = dataSource.findParcel(query)
        if (parcel == null) {
            update(mutableState.value.copy(
                outcome = ParcelSearchOutcome.NO_MATCH,
                selectedParcel = null,
                summaryVisible = false,
            ))
            return
        }

        update(mutableState.value.copy(
            outcome = ParcelSearchOutcome.MATCHED,
            selectedParcel = parcel,
            summaryVisible = true,
        ))
        effects.trySend(ParcelSearchEffect.ParcelSelected(query, parcel.selectedTarget))
    }

    private fun restoreState(): ParcelSearchUiState {
        val query = savedStateHandle.get<String>(KEY_QUERY).orEmpty()
        val outcome = savedStateHandle.get<String>(KEY_OUTCOME)
            ?.let { runCatching { ParcelSearchOutcome.valueOf(it) }.getOrNull() }
            ?: ParcelSearchOutcome.IDLE
        val parcel = if (outcome == ParcelSearchOutcome.MATCHED) dataSource.findParcel(query) else null
        return ParcelSearchUiState(
            query = query,
            outcome = if (outcome == ParcelSearchOutcome.MATCHED && parcel == null) ParcelSearchOutcome.NO_MATCH else outcome,
            selectedParcel = parcel,
            summaryVisible = savedStateHandle.get<Boolean>(KEY_SUMMARY) == true && parcel != null,
        )
    }

    private fun update(state: ParcelSearchUiState) {
        mutableState.value = state
        savedStateHandle[KEY_QUERY] = state.query
        savedStateHandle[KEY_OUTCOME] = state.outcome.name
        savedStateHandle[KEY_SUMMARY] = state.summaryVisible
    }

    private companion object {
        const val KEY_QUERY = "parcel-search.query"
        const val KEY_OUTCOME = "parcel-search.outcome"
        const val KEY_SUMMARY = "parcel-search.summary-visible"
    }
}
