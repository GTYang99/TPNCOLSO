package com.example.tp_ncolso_android.feature.parceldetail

import androidx.compose.runtime.Immutable

enum class ParcelDetailTab(val label: String) {
    LAND_DATA("土地資料"),
    SURVEY("實地勘查土地現況"),
}

@Immutable
data class ParcelDetailField(
    val label: String,
    val value: String,
)

@Immutable
data class ParcelSurveyRecord(
    val periodId: String,
    val periodLabel: String,
    val surveyDate: String,
    val surveyUser: String,
    val batchNo: String,
    val landLocation: String,
    val siteCondition: String,
    val occupyForm: String,
    val householdCount: String,
    val occupyAddress: String,
    val note: String,
    val photoSummary: String,
)

@Immutable
data class ParcelReturnReason(
    val category: String,
    val explanation: String,
)

@Immutable
data class ParcelDetailRecord(
    val status: String,
    val keyNo: String,
    val landNo: String,
    val siteCondition: String,
    val landMarkingFields: List<ParcelDetailField>,
    val gisFields: List<ParcelDetailField>,
    val surveyRecords: List<ParcelSurveyRecord>,
    val returnReason: ParcelReturnReason? = null,
    val editActionLabel: String? = null,
)

enum class ParcelDetailLoadState {
    LOADING,
    CONTENT,
    ERROR,
}

@Immutable
data class ParcelDetailUiState(
    val keyNo: String,
    val loadState: ParcelDetailLoadState = ParcelDetailLoadState.LOADING,
    val detail: ParcelDetailRecord? = null,
    val selectedTab: ParcelDetailTab = ParcelDetailTab.LAND_DATA,
    val selectedPeriodId: String? = null,
    val errorMessage: String? = null,
) {
    val selectedSurvey: ParcelSurveyRecord?
        get() = detail?.surveyRecords?.firstOrNull { it.periodId == selectedPeriodId }

    val isHistoryReadOnly: Boolean
        get() = detail?.surveyRecords?.firstOrNull()?.periodId != selectedPeriodId
}

sealed interface ParcelDetailEvent {
    data class SelectTab(val tab: ParcelDetailTab) : ParcelDetailEvent
    data class SelectPeriod(val periodId: String) : ParcelDetailEvent
    data object Retry : ParcelDetailEvent
    data object RequestEdit : ParcelDetailEvent
}

sealed interface ParcelDetailEffect {
    data object Close : ParcelDetailEffect
    data class EditRequested(val keyNo: String) : ParcelDetailEffect
}
