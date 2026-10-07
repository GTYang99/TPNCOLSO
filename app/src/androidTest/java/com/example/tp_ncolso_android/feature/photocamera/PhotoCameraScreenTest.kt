package com.example.tp_ncolso_android.feature.photocamera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.tp_ncolso_android.ui.foundation.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PhotoCameraScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun deniedPermissionShowsRetryAction() {
        var retryCount = 0

        composeRule.setContent {
            AppTheme {
                PhotoCameraScreen(
                    state = PhotoCameraUiState(permission = CameraPermissionStatus.DENIED),
                    onClose = {},
                    onRequestPermission = { retryCount += 1 },
                    onCapture = {},
                    preview = {},
                )
            }
        }

        composeRule.onNodeWithText("需要相機權限才能拍攝現況照片").assertIsDisplayed()
        composeRule.onNodeWithTag("photo-camera-permission-retry").performClick()
        assertEquals(1, retryCount)
    }

    @Test
    fun grantedCameraExposesFourByThreePreviewAndCloseAction() {
        var closeCount = 0
        var captureCount = 0

        composeRule.setContent {
            AppTheme {
                PhotoCameraScreen(
                    state = PhotoCameraUiState(permission = CameraPermissionStatus.GRANTED),
                    onClose = { closeCount += 1 },
                    onRequestPermission = {},
                    onCapture = { captureCount += 1 },
                    preview = {
                        Box(Modifier.testTag("photo-camera-preview-slot").aspectRatio(4f / 3f))
                    },
                )
            }
        }

        composeRule.onNodeWithTag("photo-camera-preview-slot").assertIsDisplayed()
        composeRule.onNodeWithTag("photo-camera-shutter").performClick()
        composeRule.onNodeWithContentDescription("取消拍攝").performClick()
        assertEquals(1, captureCount)
        assertEquals(1, closeCount)
    }

    @Test
    fun capturingDisablesShutter() {
        composeRule.setContent {
            AppTheme {
                PhotoCameraScreen(
                    state = PhotoCameraUiState(
                        permission = CameraPermissionStatus.GRANTED,
                        status = PhotoCameraStatus.CAPTURING,
                    ),
                    onClose = {},
                    onRequestPermission = {},
                    onCapture = {},
                    preview = {},
                )
            }
        }

        composeRule.onNodeWithTag("photo-camera-shutter").assertIsNotEnabled()
    }
}
