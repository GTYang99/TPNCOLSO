package com.example.tp_ncolso_android.feature.notifications.data

import com.example.tp_ncolso_android.feature.notifications.ReturnedNotification
import com.example.tp_ncolso_android.feature.parceldetail.data.ParcelDetailDataSource
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailRecord
import com.example.tp_ncolso_android.feature.parceldetail.ParcelReturnReason
import com.example.tp_ncolso_android.feature.parceldetail.data.FakeParcelDetailDataSource
import com.example.tp_ncolso_android.feature.surveyform.SurveyReturnContext
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormPhoto
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormSeed
import com.example.tp_ncolso_android.feature.surveyform.SurveyFormValues
import com.example.tp_ncolso_android.feature.surveyform.data.FakeSurveyFormDataSource
import com.example.tp_ncolso_android.feature.surveyform.data.LocalSurveySubmitResult
import com.example.tp_ncolso_android.feature.surveyform.data.SurveyFormDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** One app-session fake source shared by the returned list, parcel detail, and survey correction flow. */
class FakeReturnFlowStore : NotificationsDataSource, ParcelDetailDataSource, SurveyFormDataSource {
    private data class ParcelEntry(
        var detail: ParcelDetailRecord,
        var survey: SurveyFormSeed,
        val notification: ReturnedNotification,
    )

    private val fallbackDetails = FakeParcelDetailDataSource()
    private val fallbackSurveyForms = FakeSurveyFormDataSource()
    private val detailTemplate = requireNotNull(fallbackDetails.findDetail("TEST-KEY-002"))
    private val surveyTemplate = requireNotNull(fallbackSurveyForms.load("TEST-KEY-002"))
    private val entries = fixtureDefinitions.map { fixture ->
        val notification = ReturnedNotification(
            keyNo = fixture.landNo,
            landNo = fixture.landNo,
            status = "退回",
            reasonCategory = fixture.reason.category,
            reasonPreview = fixture.reason.explanation,
        )
        val values = surveyTemplate.values.copy(note = "補充說明：${fixture.reason.category}")
        val survey = surveyTemplate.copy(values = values, returnContext = SurveyReturnContext(fixture.reason.category, fixture.reason.explanation))
        val latestSurvey = detailTemplate.surveyRecords.first().copy(
            landLocation = values.landLocation,
            siteCondition = values.siteConditions.joinToString("、").ifBlank { "—" },
            occupyForm = values.occupyForm.ifBlank { "—" },
            householdCount = values.householdCount.ifBlank { "—" },
            occupyAddress = values.occupyAddress.ifBlank { "—" },
            note = values.note.ifBlank { "—" },
            photoSummary = "${survey.photos.size} 張（僅供檢視）",
        )
        fixture.landNo to ParcelEntry(
            detail = detailTemplate.copy(
                keyNo = fixture.landNo,
                landNo = fixture.landNo,
                surveyRecords = listOf(latestSurvey),
                returnReason = fixture.reason,
                editActionLabel = "編輯調查",
                correctionSaved = false,
            ),
            survey = survey,
            notification = notification,
        )
    }.toMap().toMutableMap()

    private val mutableReturnedNotifications = MutableStateFlow(activeNotifications())
    override val returnedNotifications: StateFlow<List<ReturnedNotification>> = mutableReturnedNotifications.asStateFlow()

    override fun findDetail(keyNo: String): ParcelDetailRecord? = entries[keyNo]?.detail ?: fallbackDetails.findDetail(keyNo)

    override fun load(keyNo: String): SurveyFormSeed? = entries[keyNo]?.survey ?: fallbackSurveyForms.load(keyNo)

    override fun submit(keyNo: String, values: SurveyFormValues, photos: List<SurveyFormPhoto>): LocalSurveySubmitResult {
        val entry = entries[keyNo] ?: return fallbackSurveyForms.submit(keyNo, values, photos)
        if (entry.detail.status != "退回") return LocalSurveySubmitResult.Failure("此資料目前無法修正")
        val updatedSurvey = entry.survey.copy(values = values, photos = photos)
        val latest = entry.detail.surveyRecords.firstOrNull() ?: detailTemplate.surveyRecords.first()
        entry.survey = updatedSurvey
        entry.detail = entry.detail.copy(
            surveyRecords = listOf(latest.copy(
                landLocation = values.landLocation,
                siteCondition = values.siteConditions.joinToString("、").ifBlank { "—" },
                occupyForm = values.occupyForm.ifBlank { "—" },
                householdCount = values.householdCount.ifBlank { "—" },
                occupyAddress = values.occupyAddress.ifBlank { "—" },
                note = values.note.ifBlank { "—" },
                photoSummary = "${photos.size} 張（僅供檢視）",
            )),
            correctionSaved = true,
        )
        return LocalSurveySubmitResult.Success
    }

    override fun confirmCorrection(keyNo: String): Boolean {
        val entry = entries[keyNo] ?: return false
        if (entry.detail.status != "退回" || !entry.detail.correctionSaved) return false
        entry.detail = entry.detail.copy(
            status = "已修正",
            returnReason = null,
            editActionLabel = null,
            correctionSaved = false,
        )
        mutableReturnedNotifications.value = activeNotifications()
        return true
    }

    private fun activeNotifications(): List<ReturnedNotification> = entries.values
        .map { it.notification }
        .filter { entries[it.keyNo]?.detail?.status == "退回" }

    private data class Fixture(val landNo: String, val reason: ParcelReturnReason)

    private companion object {
        val fixtureDefinitions = listOf(
            Fixture("A110151065400000", ParcelReturnReason("照片資料不足", "請補拍完整現況照片後重新送出。")),
            Fixture("A110151065400001", ParcelReturnReason("土地位置待確認", "請補充土地坐落位置，確認照片拍攝地點。")),
            Fixture("A110151065400002", ParcelReturnReason("占用資料不足", "請補齊占用型態與戶數資料。")),
            Fixture("A110151065400003", ParcelReturnReason("現況描述不足", "請補充現場勘查情形與說明。")),
            Fixture("A110151065400004", ParcelReturnReason("照片角度不足", "請補拍可辨識土地範圍的現況照片。")),
            Fixture("A110151065400005", ParcelReturnReason("資料需補正", "請確認調查內容與現況資料一致後重新送出。")),
        )
    }
}
