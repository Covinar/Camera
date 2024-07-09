package com.example.gallery.domain.usecases

import com.example.common.models.Image

interface GetImagesUseCase {

    operator fun invoke(): List<Image>

}