package com.example.tp_ncolso_android.ui.foundation.state

sealed interface ContentState<out T> {
    data object Idle : ContentState<Nothing>
    data object Loading : ContentState<Nothing>
    data class Content<T>(val value: T) : ContentState<T>
    data object Empty : ContentState<Nothing>
    data class Error(val message: String, val retryable: Boolean) : ContentState<Nothing>
}
