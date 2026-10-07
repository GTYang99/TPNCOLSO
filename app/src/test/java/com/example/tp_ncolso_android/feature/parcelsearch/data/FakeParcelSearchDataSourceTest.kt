package com.example.tp_ncolso_android.feature.parcelsearch.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FakeParcelSearchDataSourceTest {
    private val dataSource = FakeParcelSearchDataSource()

    @Test fun matchesKeyNumberLandNumberAndLocation() {
        assertEquals("TEST-KEY-001", dataSource.findParcel("test-key-001")?.keyNo)
        assertEquals("TEST-KEY-001", dataSource.findParcel("0012-0000")?.keyNo)
        assertEquals("TEST-KEY-002", dataSource.findParcel("示例路 2")?.keyNo)
    }

    @Test fun returnsNoMatchForUnknownOrBlankKeywords() {
        assertNull(dataSource.findParcel("不存在的資料"))
        assertNull(dataSource.findParcel("   "))
    }
}
