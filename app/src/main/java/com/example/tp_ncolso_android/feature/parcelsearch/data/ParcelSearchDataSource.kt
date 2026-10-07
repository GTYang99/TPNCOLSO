package com.example.tp_ncolso_android.feature.parcelsearch.data

import com.example.tp_ncolso_android.feature.parcelsearch.ParcelSearchRecord

interface ParcelSearchDataSource {
    fun findParcel(keyword: String): ParcelSearchRecord?
}
