package com.example.gallery.domain.di

import com.example.common.repositories.ImagesRepository
import com.example.gallery.domain.usecases.DeleteImageUseCase
import com.example.gallery.domain.usecases.DeleteImageUseCaseImpl
import com.example.gallery.domain.usecases.GetImagesUseCase
import com.example.gallery.domain.usecases.GetImagesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun provideGetImagesUseCase(imagesRepository: ImagesRepository): GetImagesUseCase = GetImagesUseCaseImpl(imagesRepository)

    @Provides
    fun provideDeleteImageUseCase(imagesRepository: ImagesRepository): DeleteImageUseCase = DeleteImageUseCaseImpl(imagesRepository)

}