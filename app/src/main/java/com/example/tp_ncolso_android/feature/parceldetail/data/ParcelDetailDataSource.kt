package com.example.tp_ncolso_android.feature.parceldetail.data

import com.example.tp_ncolso_android.feature.parceldetail.ParcelDetailRecord

interface ParcelDetailDataSource {
    fun findDetail(keyNo: String): ParcelDetailRecord?
}
