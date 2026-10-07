package com.example.tp_ncolso_android.feature.parceldetail

import androidx.lifecycle.ViewModel
import com.example.tp_ncolso_android.feature.parceldetail.data.ParcelDetailDataSource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class ParcelDetailViewModel(
    private val keyNo: String,
    private val dataSource: ParcelDetailDataSource,
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
            is ParcelDetailEvent.SelectTab -> mutableState.value = mutableState.value.copy(selectedTab = event.tab)
            is ParcelDetailEvent.SelectPeriod -> {
                val periodExists = mutableState.value.detail?.surveyRecords?.any { it.periodId == event.periodId } == true
                if (periodExists) mutableState.value = mutableState.value.copy(selectedPeriodId = event.periodId)
            }
            ParcelDetailEvent.Retry -> load()
            ParcelDetailEvent.RequestEdit -> {
                mutableState.value.detail?.editActionLabel?.let {
                    effects.trySend(ParcelDetailEffect.EditRequested(keyNo))
                }
            }
        }
    }

    private fun load() {
        val detail = dataSource.findDetail(keyNo)
        mutableState.value = if (detail == null) {
            ParcelDetailUiState(
                keyNo = keyNo,
                loadState = ParcelDetailLoadState.ERROR,
                errorMessage = "找不到土地詳情資料",
            )
        } else {
            ParcelDetailUiState(
                keyNo = keyNo,
                loadState = ParcelDetailLoadState.CONTENT,
                detail = detail,
                selectedPeriodId = detail.surveyRecords.firstOrNull()?.periodId,
            )
        }
    }
}
