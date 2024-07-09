package com.example.gallery.domain.usecases

interface DeleteImageUseCase {

    operator fun invoke(path: String)

}