package com.isteam.movieappaz.di

import com.isteam.movieappaz.domain.useCase.ValidationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun injectValidationUseCase() = ValidationUseCase()


}