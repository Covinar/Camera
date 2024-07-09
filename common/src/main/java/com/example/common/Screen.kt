package com.example.common

sealed class Screen(
    val route: String
) {

    data object Camera: Screen(CAMERA_ROUTE)
    data object Images: Screen(IMAGES_ROUTE)
    data object ImagePreview: Screen(IMAGES_PREVIEW_ROUTE)

    companion object {
        const val CAMERA_ROUTE = "camera"
        const val IMAGES_ROUTE = "images"
        const val IMAGES_PREVIEW_ROUTE = "image_preview"
    }

}
