package com.example.tp_ncolso_android.feature.photocamera

import androidx.compose.runtime.Immutable

enum class CameraPermissionStatus {
    UNKNOWN,
    GRANTED,
    DENIED,
}

enum class PhotoCameraStatus {
    READY,
    CAPTURING,
}

@Immutable
data class CapturedPhoto(
    val uri: String,
    val capturedAt: String,
)

@Immutable
data class PhotoCameraUiState(
    val permission: CameraPermissionStatus = CameraPermissionStatus.UNKNOWN,
    val status: PhotoCameraStatus = PhotoCameraStatus.READY,
    val errorMessage: String? = null,
)

sealed interface PhotoCameraEvent {
    data class PermissionResult(val granted: Boolean) : PhotoCameraEvent
    data object CaptureStarted : PhotoCameraEvent
    data class CaptureSucceeded(val photo: CapturedPhoto) : PhotoCameraEvent
    data class CaptureFailed(val message: String) : PhotoCameraEvent
    data object ClearError : PhotoCameraEvent
}

sealed interface PhotoCameraEffect {
    data class PhotoCaptured(val photo: CapturedPhoto) : PhotoCameraEffect
}

data class PhotoCameraCallbacks(
    val onClose: () -> Unit,
    val onPhotoCaptured: (CapturedPhoto) -> Unit,
)
