package com.example.tp_ncolso_android.feature.parceldetail.data

import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailField
import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailRecord
import com.example.tp_ncolso_android.feature.parceldetail.ParcelReturnReason
import com.example.tp_ncolso_android.feature.parceldetail.ParcelSurveyRecord

class FakeParcelDetailDataSource(
    records: List<ParcelDetailRecord> = defaultRecords,
) : ParcelDetailDataSource {
    private val fixtures = records.associateBy { it.keyNo }

    override fun findDetail(keyNo: String): ParcelDetailRecord? = fixtures[keyNo]

    private companion object {
        val defaultRecords = listOf(
            ParcelDetailRecord(
                status = "未調查",
                keyNo = "TEST-KEY-001",
                landNo = "測試段 0012-0000",
                siteCondition = "無占用",
                landMarkingFields = listOf(
                    ParcelDetailField("地號", "測試段 0012-0000"),
                    ParcelDetailField("都市計劃使用分區", "第三種住宅區"),
                    ParcelDetailField("宗地面積", "1,234.56"),
                    ParcelDetailField("權利範圍-分子", "1"),
                    ParcelDetailField("權利範圍-分母", "1"),
                    ParcelDetailField("持分面積(㎡)", "1,234.56"),
                    ParcelDetailField("所有權人", "測試所有權人"),
                    ParcelDetailField("登記原因", "買賣"),
                ),
                gisFields = listOf(
                    ParcelDetailField("是否遭占", "否"),
                    ParcelDetailField("占用狀況", "無套匯占用情形"),
                ),
                surveyRecords = listOf(
                    ParcelSurveyRecord(
                        periodId = "2026-latest",
                        periodLabel = "2026 年最新一期",
                        surveyDate = "民國 115 年 10 月 7 日",
                        surveyUser = "尚未調查",
                        batchNo = "B-001",
                        landLocation = "測試區示例路 1 號",
                        siteCondition = "無占用",
                        occupyForm = "—",
                        householdCount = "—",
                        occupyAddress = "—",
                        note = "—",
                        photoSummary = "尚未拍攝",
                    ),
                    ParcelSurveyRecord(
                        periodId = "2025-history",
                        periodLabel = "2025 年歷史期別",
                        surveyDate = "民國 114 年 9 月 3 日",
                        surveyUser = "歷史調查人員",
                        batchNo = "B-000",
                        landLocation = "測試區示例路 1 號",
                        siteCondition = "無占用",
                        occupyForm = "—",
                        householdCount = "—",
                        occupyAddress = "—",
                        note = "歷史檢視資料",
                        photoSummary = "4 張（僅供檢視）",
                    ),
                ),
                editActionLabel = "進入調查",
            ),
            ParcelDetailRecord(
                status = "退回",
                keyNo = "TEST-KEY-002",
                landNo = "測試段 0024-0000",
                siteCondition = "有占用",
                landMarkingFields = listOf(
                    ParcelDetailField("地號", "測試段 0024-0000"),
                    ParcelDetailField("都市計劃使用分區", "公共設施用地"),
                    ParcelDetailField("宗地面積", "876.50"),
                    ParcelDetailField("權利範圍-分子", "1"),
                    ParcelDetailField("權利範圍-分母", "2"),
                    ParcelDetailField("持分面積(㎡)", "438.25"),
                    ParcelDetailField("所有權人", "測試所有權人乙"),
                    ParcelDetailField("登記原因", "繼承"),
                ),
                gisFields = listOf(
                    ParcelDetailField("是否遭占", "是"),
                    ParcelDetailField("占用狀況", "圖資顯示部分占用"),
                ),
                surveyRecords = listOf(
                    ParcelSurveyRecord(
                        periodId = "2026-latest",
                        periodLabel = "2026 年最新一期",
                        surveyDate = "民國 115 年 9 月 28 日",
                        surveyUser = "測試調查人員",
                        batchNo = "B-001",
                        landLocation = "測試區示例路 2 號",
                        siteCondition = "有占用",
                        occupyForm = "固定式",
                        householdCount = "2",
                        occupyAddress = "測試區示例路 2 號",
                        note = "請補充現況照片",
                        photoSummary = "2 張（僅供檢視）",
                    ),
                ),
                returnReason = ParcelReturnReason(
                    category = "照片資料不足",
                    explanation = "請補拍完整現況照片後重新送出。",
                ),
                editActionLabel = "編輯調查",
            ),
        )
    }
}
