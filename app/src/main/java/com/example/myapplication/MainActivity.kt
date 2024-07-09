package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.camera.presentation.CameraScreen
import com.example.common.Screen
import com.example.gallery.presentation.ImagesViewModel
import com.example.gallery.presentation.images.ImagesScreen
import com.example.gallery.presentation.preview.ImagePreview
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val storageActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Storage Permissions Granted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Storage Permissions Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isCameraGranted = it
    }

    private val imagesPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isImagesGranted = it
    }

    private var isCameraGranted by mutableStateOf(false)
    private var isImagesGranted by mutableStateOf(false)

    private val imagesViewModel: ImagesViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val isGranted = Environment.isExternalStorageManager()
            if (isGranted.not()) {
                requestManageExternalStorage()
            }
        }
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination =  Screen.CAMERA_ROUTE
            ) {
                composable(route = Screen.CAMERA_ROUTE){
                    if (isCameraGranted) {
                        CameraScreen(
                            onGalleryClicked = {
                                navController.navigate(Screen.IMAGES_ROUTE)
                            }
                        )
                    } else {
                        cameraPermissionRequest.launch(android.Manifest.permission.CAMERA)
                    }
                }
                composable(route = Screen.IMAGES_ROUTE) {
                    if (isImagesGranted) {
                        ImagesScreen(
                            openPreview = {
                                navController.navigate(Screen.IMAGES_PREVIEW_ROUTE)
                            },
                            viewModel = imagesViewModel,
                            openCamera = {
                                navController.popBackStack()
                            }
                        )
                    } else {
                        imagesPermissionRequest.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    }
                }
                composable(route = Screen.IMAGES_PREVIEW_ROUTE) {
                    ImagePreview(
                        viewModel = imagesViewModel,
                        openGallery = {
                            navController.popBackStack()
                        },

                    )
                }
            }
        }
    }

    private fun requestManageExternalStorage() {
        val intent = Intent()
        intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        storageActivityResultLauncher.launch(intent)
    }

}
