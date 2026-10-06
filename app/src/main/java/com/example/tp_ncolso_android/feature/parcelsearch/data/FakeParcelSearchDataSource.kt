package com.example.tp_ncolso_android.feature.parcelsearch.data

import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchRecord

class FakeParcelSearchDataSource(
    records: List<ParcelSearchRecord> = defaultRecords,
) : ParcelSearchDataSource {
    private val fixtures = records.toList()

    override fun findParcel(keyword: String): ParcelSearchRecord? {
        val query = keyword.trim()
        if (query.isEmpty()) return null

        return fixtures.firstOrNull { parcel ->
            listOf(parcel.keyNo, parcel.landNo, parcel.landLocation)
                .any { field -> field.contains(query, ignoreCase = true) }
        }
    }

    private companion object {
        val defaultRecords = listOf(
            ParcelSearchRecord(
                selectedTarget = "測試地塊 T-001",
                keyNo = "TEST-KEY-001",
                landNo = "測試段 0012-0000",
                landLocation = "測試區示例路 1 號",
                status = "未調查",
                siteCondition = "無占用",
            ),
            ParcelSearchRecord(
                selectedTarget = "測試地塊 T-002",
                keyNo = "TEST-KEY-002",
                landNo = "測試段 0024-0000",
                landLocation = "測試區示例路 2 號",
                status = "退回",
                siteCondition = "有占用",
            ),
        )
    }
}
