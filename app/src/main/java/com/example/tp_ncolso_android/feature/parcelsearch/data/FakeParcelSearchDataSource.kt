package com.example.tp_ncolso_android.feature.parcelsearch.data

class FakeParcelSearchDataSource : ParcelSearchDataSource {
    private val parcels = listOf(
        ParcelSearchParcel(
            keyNo = "KS-10001",
            landNo = "中正段 0012-0000",
            location = "臺北市中正區",
            status = "已登記",
            siteCondition = "建物使用中",
            selectedTarget = "地塊 A-001",
        ),
        ParcelSearchParcel(
            keyNo = "KS-10002",
            landNo = "大安段 0048-0000",
            location = "臺北市大安區",
            status = "待調查",
            siteCondition = "空地",
            selectedTarget = "地塊 A-002",
        ),
        ParcelSearchParcel(
            keyNo = "TC-20001",
            landNo = "西屯段 0310-0000",
            location = "臺中市西屯區",
            status = "調查中",
            siteCondition = "住宅使用",
            selectedTarget = "地塊 B-001",
        ),
    )

    override fun search(keyword: String): List<ParcelSearchParcel> {
        val normalizedKeyword = keyword.trim().lowercase()
        if (normalizedKeyword.isEmpty()) return emptyList()

        return parcels.filter { parcel ->
            parcel.keyNo.lowercase().contains(normalizedKeyword) ||
                parcel.landNo.lowercase().contains(normalizedKeyword) ||
                parcel.location.lowercase().contains(normalizedKeyword)
        }
    }
}
