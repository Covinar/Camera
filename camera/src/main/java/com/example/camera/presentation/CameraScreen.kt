package com.example.camera.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.example.camera.R

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    onGalleryClicked: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraView = remember {
        PreviewView(context)
    }
    val lifecycleCameraController = remember {
        LifecycleCameraController(context)
    }

    LaunchedEffect(key1 = state.isFlashMode) {
        lifecycleCameraController.imageCaptureFlashMode = if (state.isFlashMode)
            ImageCapture.FLASH_MODE_ON else FLASH_MODE_OFF
    }

    LaunchedEffect(key1 = "camera_screen") {
        cameraView.controller = lifecycleCameraController
        lifecycleCameraController.bindToLifecycle(lifecycleOwner)
    }

    Column {
        val bitmap = state.bitmap
        if (bitmap == null) {
            Camera(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5F),
                cameraView = cameraView)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5F)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    bitmap = bitmap.asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                    ,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = AbsoluteRoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        text = stringResource(id = R.string.photo_preview),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
        Divider(
            thickness = 2.dp,
            color = Color.Black
        )
        CameraMenu(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .weight(1F),
            viewModel = viewModel,
            context = context,
            lifecycleCameraController = lifecycleCameraController,
            isBitmapNull = state.bitmap == null,
            onGalleryClicked = {
                onGalleryClicked()
            }
        )
    }
}

@Composable
fun CameraMenu(
    viewModel: CameraViewModel,
    modifier: Modifier,
    context: Context,
    lifecycleCameraController: LifecycleCameraController,
    isBitmapNull: Boolean,
    onGalleryClicked: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            ButtonGallery(
                onGalleryClicked = {
                    onGalleryClicked()
                }
            )
            ButtonTakeImage(
                context = context,
                cameraController = lifecycleCameraController,
                onTakeImageClicked = { bitmap ->
                    viewModel.updateBitmap(bitmap = bitmap)
                },
                isBitmapNull = isBitmapNull,
                onCheckClicked = {
                    viewModel.saveImage()
                }
            )
            ButtonFlash(
                onFlashClicked = {
                    viewModel.changeFlashMode()
                },
                isBitmapNull = isBitmapNull,
                onTrashClicked = {
                    viewModel.resetBitmap()
                }
            )
        }
    }
}

@Composable
fun Camera(
    modifier: Modifier,
    cameraView: PreviewView
) {
    AndroidView(
        factory = { cameraView },
        modifier = modifier
    )
}

@Composable
fun ButtonGallery(
    onGalleryClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50))
            .clickable {
                onGalleryClicked()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.ic_gallery),
            contentDescription = null
        )
    }
}

@Composable
fun ButtonFlash(
    isBitmapNull: Boolean,
    onFlashClicked: () -> Unit,
    onTrashClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(50))
            .clickable {
                if (!isBitmapNull) {
                    onTrashClicked()
                } else {
                    onFlashClicked()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (!isBitmapNull) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_trash),
                contentDescription = null
            )
        } else {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = painterResource(id = R.drawable.ic_flash),
                contentDescription = null
            )
        }
    }
}

@Composable
fun ButtonTakeImage(
    context: Context,
    cameraController: LifecycleCameraController,
    isBitmapNull: Boolean,
    onTakeImageClicked: (bitmap: Bitmap) -> Unit,
    onCheckClicked: () -> Unit
) {
    if (!isBitmapNull) {
        Box(
            modifier = Modifier
                .size(82.dp)
                .border(BorderStroke(4.dp, Color.Black), RoundedCornerShape(50))
                .clickable {
                    onCheckClicked()
                },
            contentAlignment= Alignment.Center,

            ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Black,
                        shape = CircleShape
                    )
            ){
                Icon(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = null,
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color.White, CircleShape)
                        .padding(8.dp)
                )
            }
        }

    } else {
        Box(
            modifier = Modifier
                .size(82.dp)
                .border(BorderStroke(4.dp, Color.Black), RoundedCornerShape(50))
                .clickable {
                    cameraController.takePicture(
                        ContextCompat.getMainExecutor(context),
                        object : OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                super.onCaptureSuccess(image)
                                val bitmap = image.toBitmap()
                                onTakeImageClicked(bitmap)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            MyCircle()
        }
    }


}

@Composable
fun MyCircle(){
    Canvas(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp),
        onDraw = {
        drawCircle(color = Color.Black)
    })
}
