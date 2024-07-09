package com.example.camera.domain.usecases

import android.graphics.Bitmap
import com.example.common.repositories.ImagesRepository
import javax.inject.Inject

class SaveImageUseCaseImpl @Inject constructor(
    private val imagesRepository: ImagesRepository
): SaveImageUseCase {
    override fun invoke(bitmap: Bitmap) {
        imagesRepository.saveImage(bitmap)
    }
}