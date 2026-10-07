package com.example.tp_ncolso_android.feature.photocamera

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class PhotoCameraViewModelTest {
    @Test
    fun deniedPermissionIsRetryableAndGrantClearsMessage() {
        val viewModel = PhotoCameraViewModel()

        viewModel.onEvent(PhotoCameraEvent.PermissionResult(granted = false))
        assertEquals(CameraPermissionStatus.DENIED, viewModel.state.value.permission)
        assertEquals("需要相機權限才能拍攝現況照片", viewModel.state.value.errorMessage)

        viewModel.onEvent(PhotoCameraEvent.PermissionResult(granted = true))
        assertEquals(CameraPermissionStatus.GRANTED, viewModel.state.value.permission)
        assertEquals(null, viewModel.state.value.errorMessage)
    }

    @Test
    fun captureEmitsOnePhotoAndIgnoresReentrantEvents() = runBlocking {
        val viewModel = PhotoCameraViewModel()
        val photo = CapturedPhoto("file:///cache/photo.jpg", "民國 115 年 10 月 7 日 10:10:10")

        viewModel.onEvent(PhotoCameraEvent.PermissionResult(granted = true))
        viewModel.onEvent(PhotoCameraEvent.CaptureStarted)
        viewModel.onEvent(PhotoCameraEvent.CaptureStarted)
        viewModel.onEvent(PhotoCameraEvent.CaptureSucceeded(photo))
        viewModel.onEvent(PhotoCameraEvent.CaptureSucceeded(photo.copy(uri = "file:///cache/duplicate.jpg")))

        val effect = viewModel.effect.first() as PhotoCameraEffect.PhotoCaptured
        assertEquals(photo, effect.photo)
        assertEquals(PhotoCameraStatus.READY, viewModel.state.value.status)
    }

    @Test
    fun captureFailureFromReadyStateIsVisibleAndRecoverable() {
        val viewModel = PhotoCameraViewModel()

        viewModel.onEvent(PhotoCameraEvent.CaptureFailed("相機尚未準備完成，請稍後重試"))

        assertEquals(PhotoCameraStatus.READY, viewModel.state.value.status)
        assertEquals("相機尚未準備完成，請稍後重試", viewModel.state.value.errorMessage)
        viewModel.onEvent(PhotoCameraEvent.ClearError)
        assertEquals(null, viewModel.state.value.errorMessage)
    }

    @Test
    fun capturedAtUsesTaiwanCalendarAndTwoDigitTime() {
        assertEquals(
            "民國 115 年 10 月 7 日 09:05:03",
            formatCapturedAt(LocalDateTime.of(2026, 10, 7, 9, 5, 3)),
        )
        assertTrue(formatCapturedAt(LocalDateTime.now()).startsWith("民國 "))
    }
}
