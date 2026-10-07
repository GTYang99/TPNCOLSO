package com.example.tp_ncolso_android.feature.photocamera

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class PhotoCameraViewModel : ViewModel() {
    private val _state = MutableStateFlow(PhotoCameraUiState())
    val state: StateFlow<PhotoCameraUiState> = _state.asStateFlow()

    private val effects = Channel<PhotoCameraEffect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    fun onEvent(event: PhotoCameraEvent) {
        val current = _state.value
        when (event) {
            is PhotoCameraEvent.PermissionResult -> {
                _state.value = current.copy(
                    permission = if (event.granted) CameraPermissionStatus.GRANTED else CameraPermissionStatus.DENIED,
                    errorMessage = if (event.granted) null else CAMERA_PERMISSION_MESSAGE,
                )
            }

            PhotoCameraEvent.CaptureStarted -> {
                if (current.permission == CameraPermissionStatus.GRANTED && current.status == PhotoCameraStatus.READY) {
                    _state.value = current.copy(status = PhotoCameraStatus.CAPTURING, errorMessage = null)
                }
            }

            is PhotoCameraEvent.CaptureSucceeded -> {
                if (current.status == PhotoCameraStatus.CAPTURING) {
                    _state.value = current.copy(status = PhotoCameraStatus.READY, errorMessage = null)
                    effects.trySend(PhotoCameraEffect.PhotoCaptured(event.photo))
                }
            }

            is PhotoCameraEvent.CaptureFailed -> {
                _state.value = current.copy(status = PhotoCameraStatus.READY, errorMessage = event.message)
            }

            PhotoCameraEvent.ClearError -> _state.value = current.copy(errorMessage = null)
        }
    }

    private companion object {
        const val CAMERA_PERMISSION_MESSAGE = "需要相機權限才能拍攝現況照片"
    }
}
