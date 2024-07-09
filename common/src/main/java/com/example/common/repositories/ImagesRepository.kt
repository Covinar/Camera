package com.example.common.repositories

import android.graphics.Bitmap
import com.example.common.models.Image

interface ImagesRepository {

    fun getImages(): List<Image>

    fun saveImage(bitmap: Bitmap)

    fun deleteImage(path: String)

}