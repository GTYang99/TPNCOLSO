package com.example.tp_ncolso_android.feature.photocamera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class PhotoCameraViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {
    private val _state = MutableStateFlow(PhotoCameraUiState(errorMessage = savedStateHandle[KEY_ERROR]))
    val state: StateFlow<PhotoCameraUiState> = _state.asStateFlow()

    private val effects = Channel<PhotoCameraEffect>(Channel.BUFFERED)
    val effect = effects.receiveAsFlow()

    fun onEvent(event: PhotoCameraEvent) {
        val current = _state.value
        when (event) {
            is PhotoCameraEvent.PermissionResult -> {
                update(current.copy(
                    permission = if (event.granted) CameraPermissionStatus.GRANTED else CameraPermissionStatus.DENIED,
                    errorMessage = if (event.granted) null else CAMERA_PERMISSION_MESSAGE,
                ))
            }

            PhotoCameraEvent.CaptureStarted -> {
                if (current.permission == CameraPermissionStatus.GRANTED && current.status == PhotoCameraStatus.READY) {
                    update(current.copy(status = PhotoCameraStatus.CAPTURING, errorMessage = null))
                }
            }

            is PhotoCameraEvent.CaptureSucceeded -> {
                if (current.status == PhotoCameraStatus.CAPTURING) {
                    update(current.copy(status = PhotoCameraStatus.READY, errorMessage = null))
                    effects.trySend(PhotoCameraEffect.PhotoCaptured(event.photo))
                }
            }

            is PhotoCameraEvent.CaptureFailed -> {
                update(current.copy(status = PhotoCameraStatus.READY, errorMessage = event.message))
            }

            PhotoCameraEvent.ClearError -> update(current.copy(errorMessage = null))
        }
    }

    private fun update(state: PhotoCameraUiState) {
        _state.value = state
        savedStateHandle[KEY_ERROR] = state.errorMessage
    }

    private companion object {
        const val KEY_ERROR = "photo-camera.error"
        const val CAMERA_PERMISSION_MESSAGE = "需要相機權限才能拍攝現況照片"
    }
}
