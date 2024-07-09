package com.example.common.di

import android.content.Context
import com.example.common.repositories.ImagesRepository
import com.example.common.repositories.ImagesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideImagesRepository(@ApplicationContext context: Context): ImagesRepository = ImagesRepositoryImpl(context)

}