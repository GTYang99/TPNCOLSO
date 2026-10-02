package com.example.tp_ncolso_android.feature.parcelsearch.data

import androidx.compose.runtime.Immutable

@Immutable
data class ParcelSearchParcel(
    val keyNo: String,
    val landNo: String,
    val location: String,
    val status: String,
    val siteCondition: String,
    val selectedTarget: String,
)

fun interface ParcelSearchDataSource {
    fun search(keyword: String): List<ParcelSearchParcel>
}
