package com.example.gallery.domain.usecases

import com.example.common.repositories.ImagesRepository
import javax.inject.Inject

class DeleteImageUseCaseImpl @Inject constructor(
    private val imagesRepository: ImagesRepository
): DeleteImageUseCase {

    override fun invoke(path: String) {
        imagesRepository.deleteImage(path)
    }

}