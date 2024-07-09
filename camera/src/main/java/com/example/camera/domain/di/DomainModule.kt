package com.example.camera.domain.di

import com.example.camera.domain.usecases.SaveImageUseCase
import com.example.camera.domain.usecases.SaveImageUseCaseImpl
import com.example.common.repositories.ImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideSaveImageUseCase(imagesRepository: ImagesRepository): SaveImageUseCase = SaveImageUseCaseImpl(imagesRepository)

}