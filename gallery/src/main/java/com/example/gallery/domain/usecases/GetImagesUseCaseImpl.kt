package com.example.gallery.domain.usecases

import com.example.common.models.Image
import com.example.common.repositories.ImagesRepository
import javax.inject.Inject

class GetImagesUseCaseImpl @Inject constructor(
    private val imagesRepository: ImagesRepository
) : GetImagesUseCase {

    override operator fun invoke(): List<Image> {
        return imagesRepository.getImages()
    }

}