package com.example.tp_ncolso_android.feature.photocamera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.Surface
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.abs

private const val FourByThree = 4f / 3f

@Composable
fun PhotoCameraRoute(
    viewModel: PhotoCameraViewModel,
    callbacks: PhotoCameraCallbacks,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val currentCallbacks by rememberUpdatedState(callbacks)
    val currentEvent by rememberUpdatedState(viewModel::onEvent)
    val context = LocalContext.current
    val lifecycleOwner = LocalView.current.findViewTreeLifecycleOwner()
    val previewView = remember(context) {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var permissionRequested by rememberSaveable { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        currentEvent(PhotoCameraEvent.PermissionResult(granted))
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        currentEvent(PhotoCameraEvent.PermissionResult(granted))
        if (!granted && !permissionRequested) {
            permissionRequested = true
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (state.permission == CameraPermissionStatus.GRANTED && lifecycleOwner != null) {
        DisposableEffect(lifecycleOwner, previewView) {
            var cameraProvider: ProcessCameraProvider? = null
            val providerFuture = ProcessCameraProvider.getInstance(context)
            val executor = ContextCompat.getMainExecutor(context)
            val listener = Runnable {
                runCatching {
                    val provider = providerFuture.get()
                    cameraProvider = provider
                    val rotation = previewView.display?.rotation ?: Surface.ROTATION_0
                    val preview = Preview.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                        .setTargetRotation(rotation)
                        .build()
                    val capture = ImageCapture.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                        .setTargetRotation(rotation)
                        .build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    provider.unbindAll()
                    provider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        capture,
                    )
                    imageCapture = capture
                }.onFailure { error ->
                    imageCapture = null
                    currentEvent(PhotoCameraEvent.CaptureFailed(cameraErrorMessage(error)))
                }
            }
            providerFuture.addListener(listener, executor)
            onDispose {
                cameraProvider?.unbindAll()
                imageCapture = null
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PhotoCameraEffect.PhotoCaptured -> currentCallbacks.onPhotoCaptured(effect.photo)
            }
        }
    }

    PhotoCameraScreen(
        state = state,
        onClose = currentCallbacks.onClose,
        onRequestPermission = {
            permissionRequested = true
            permissionLauncher.launch(Manifest.permission.CAMERA)
        },
        onCapture = {
            capturePhoto(
                context = context,
                imageCapture = imageCapture,
                currentState = state,
                onEvent = currentEvent,
            )
        },
        preview = {
            if (state.permission == CameraPermissionStatus.GRANTED) {
                androidx.compose.ui.viewinterop.AndroidView(
                    factory = { previewView },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(FourByThree)
                        .testTag("photo-camera-preview"),
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
fun PhotoCameraScreen(
    state: PhotoCameraUiState,
    onClose: () -> Unit,
    onRequestPermission: () -> Unit,
    onCapture: () -> Unit,
    preview: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxSize().testTag("photo-camera-screen"), color = Color.Black) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.permission == CameraPermissionStatus.GRANTED) {
                Box(
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                    contentAlignment = Alignment.Center,
                ) { preview() }
            } else {
                PermissionContent(
                    denied = state.permission == CameraPermissionStatus.DENIED,
                    onRequestPermission = onRequestPermission,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("拍攝現況照片", color = Color.White, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                TextButton(onClick = onClose, modifier = Modifier.semantics { contentDescription = "取消拍攝" }) {
                    Text("取消", color = Color.White)
                }
            }
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                state.errorMessage?.let {
                    Surface(color = Color.Black.copy(alpha = 0.72f), shape = CircleShape) {
                        Text(it, color = Color.White, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    }
                }
                val captureEnabled = state.permission == CameraPermissionStatus.GRANTED && state.status == PhotoCameraStatus.READY
                FloatingActionButton(
                    onClick = { if (captureEnabled) onCapture() },
                    modifier = Modifier
                        .size(64.dp)
                        .semantics {
                            contentDescription = "拍攝現況照片"
                            if (!captureEnabled) disabled()
                        }
                        .testTag("photo-camera-shutter"),
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ) { Text(if (state.status == PhotoCameraStatus.CAPTURING) "…" else "拍") }
            }
        }
    }
}

@Composable
private fun PermissionContent(
    denied: Boolean,
    onRequestPermission: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("需要相機權限才能拍攝現況照片", color = Color.White)
        if (denied) {
            Button(
                onClick = onRequestPermission,
                modifier = Modifier.padding(top = 16.dp).testTag("photo-camera-permission-retry"),
            ) { Text("重新允許相機權限") }
        }
    }
}

private fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture?,
    currentState: PhotoCameraUiState,
    onEvent: (PhotoCameraEvent) -> Unit,
) {
    if (imageCapture == null || currentState.status == PhotoCameraStatus.CAPTURING) {
        if (imageCapture == null && currentState.status == PhotoCameraStatus.READY) {
            onEvent(PhotoCameraEvent.CaptureFailed("相機尚未準備完成，請稍後重試"))
        }
        return
    }
    onEvent(PhotoCameraEvent.CaptureStarted)
    val photoFile = File(context.cacheDir, "survey-photo-${System.currentTimeMillis()}.jpg")
    val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    imageCapture.takePicture(
        output,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                photoFile.delete()
                onEvent(PhotoCameraEvent.CaptureFailed(cameraErrorMessage(exception)))
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                if (!photoFile.exists() || !isFourByThree(photoFile)) {
                    photoFile.delete()
                    onEvent(PhotoCameraEvent.CaptureFailed("照片格式不是 4:3，請重新拍攝"))
                    return
                }
                onEvent(
                    PhotoCameraEvent.CaptureSucceeded(
                        CapturedPhoto(
                            uri = Uri.fromFile(photoFile).toString(),
                            capturedAt = formatCapturedAt(LocalDateTime.now(ZoneId.of("Asia/Taipei"))),
                        ),
                    ),
                )
            }
        },
    )
}

internal fun isFourByThree(file: File): Boolean {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(file.absolutePath, options)
    if (options.outWidth <= 0 || options.outHeight <= 0) return false
    val ratio = options.outWidth.toFloat() / options.outHeight.toFloat()
    return abs(ratio - FourByThree) <= 0.03f || abs(ratio - (1f / FourByThree)) <= 0.03f
}

internal fun formatCapturedAt(dateTime: LocalDateTime): String =
    "民國 ${dateTime.year - 1911} 年 ${dateTime.monthValue} 月 ${dateTime.dayOfMonth} 日 " +
        "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}:${dateTime.second.toString().padStart(2, '0')}"

private fun cameraErrorMessage(error: Throwable): String =
    if (error.message.orEmpty().contains("permission", ignoreCase = true)) {
        "需要相機權限才能拍攝現況照片"
    } else {
        "相機暫時無法使用，請重試"
    }
