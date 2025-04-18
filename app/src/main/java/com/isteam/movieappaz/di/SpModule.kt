package com.isteam.movieappaz.di

import android.content.Context
import com.isteam.movieappaz.common.manager.AppSharedPreferences
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object SpModule {

    @Singleton
    @Provides
    fun injectAppSp(@ApplicationContext context: Context) = AppSharedPreferences(context)

    @Singleton
    @Provides
    fun injectSpManager(appSharedPreferences: AppSharedPreferences) =
        SharedPreferencesManager(appSharedPreferences)

}