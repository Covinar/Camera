package com.example.camera.domain.usecases

import android.graphics.Bitmap

interface SaveImageUseCase {

    operator fun invoke(bitmap: Bitmap)

}