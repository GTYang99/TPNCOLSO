package com.example.tp_ncolso_android.feature.parceldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.tp_ncolso_android.feature.parceldetail.data.ParcelDetailDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ParcelDetailViewModel(
    private val keyNo: String,
    private val dataSource: ParcelDetailDataSource,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {
    private val mutableState = MutableStateFlow(ParcelDetailUiState(keyNo = keyNo))
    val state: StateFlow<ParcelDetailUiState> = mutableState.asStateFlow()

    private val effects = Channel<ParcelDetailEffect>(capacity = Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    init {
        load()
    }

    fun onEvent(event: ParcelDetailEvent) {
        when (event) {
            is ParcelDetailEvent.SelectTab -> {
                mutableState.value = mutableState.value.copy(selectedTab = event.tab)
                savedStateHandle[KEY_TAB] = event.tab.name
            }
            is ParcelDetailEvent.SelectPeriod -> {
                val periodExists = mutableState.value.detail?.surveyRecords?.any { it.periodId == event.periodId } == true
                if (periodExists) {
                    mutableState.value = mutableState.value.copy(selectedPeriodId = event.periodId)
                    savedStateHandle[KEY_PERIOD] = event.periodId
                }
            }
            ParcelDetailEvent.Retry, ParcelDetailEvent.Refresh -> load()
            ParcelDetailEvent.CorrectionSaved -> {
                load()
                val loaded = mutableState.value
                mutableState.value = loaded.copy(showCorrectionSaved = loaded.detail?.correctionSaved == true)
            }
            ParcelDetailEvent.RequestEdit -> {
                mutableState.value.detail?.takeIf { it.editActionLabel != null && it.status != "已修正" }?.let {
                    effects.trySend(ParcelDetailEffect.EditRequested(keyNo))
                }
            }
            ParcelDetailEvent.RequestCorrectionConfirmation -> {
                val detail = mutableState.value.detail
                if (detail?.status == "退回" && detail.correctionSaved) {
                    mutableState.value = mutableState.value.copy(showCorrectionConfirmation = true)
                }
            }
            ParcelDetailEvent.DismissCorrectionConfirmation -> {
                mutableState.value = mutableState.value.copy(showCorrectionConfirmation = false)
            }
            ParcelDetailEvent.ConfirmCorrection -> confirmCorrection()
            ParcelDetailEvent.DismissCorrectionSaved -> {
                mutableState.value = mutableState.value.copy(showCorrectionSaved = false)
            }
            ParcelDetailEvent.DismissCorrectionSuccess -> {
                mutableState.value = mutableState.value.copy(showCorrectionSuccess = false)
            }
        }
    }

    private fun confirmCorrection() {
        val current = mutableState.value
        if (!current.showCorrectionConfirmation || !dataSource.confirmCorrection(keyNo)) {
            mutableState.value = current.copy(showCorrectionConfirmation = false)
            return
        }
        val refreshed = dataSource.findDetail(keyNo)
        mutableState.value = current.copy(
            loadState = if (refreshed == null) ParcelDetailLoadState.ERROR else ParcelDetailLoadState.CONTENT,
            detail = refreshed,
            errorMessage = if (refreshed == null) "找不到土地詳情資料" else null,
            showCorrectionSaved = false,
            showCorrectionConfirmation = false,
            showCorrectionSuccess = refreshed != null,
        )
    }

    private fun load() {
        val detail = dataSource.findDetail(keyNo)
        mutableState.value = if (detail == null) {
            restoredSelection(ParcelDetailUiState(
                keyNo = keyNo,
                loadState = ParcelDetailLoadState.ERROR,
                errorMessage = "找不到土地詳情資料",
            ))
        } else {
            restoredSelection(ParcelDetailUiState(
                keyNo = keyNo,
                loadState = ParcelDetailLoadState.CONTENT,
                detail = detail,
                selectedPeriodId = detail.surveyRecords.firstOrNull()?.periodId,
            ))
        }
    }

    private fun restoredSelection(state: ParcelDetailUiState): ParcelDetailUiState {
        val tab = savedStateHandle.get<String>(KEY_TAB)
            ?.let { runCatching { ParcelDetailTab.valueOf(it) }.getOrNull() }
            ?: ParcelDetailTab.LAND_DATA
        val requestedPeriod = savedStateHandle.get<String>(KEY_PERIOD)
        val period = requestedPeriod?.takeIf { id -> state.detail?.surveyRecords?.any { it.periodId == id } == true }
            ?: state.selectedPeriodId
        return state.copy(selectedTab = tab, selectedPeriodId = period)
    }

    private companion object {
        const val KEY_TAB = "parcel-detail.tab"
        const val KEY_PERIOD = "parcel-detail.period"
    }
}
