package com.isteam.movieappaz.di

import android.content.Context
import android.net.ConnectivityManager
import com.isteam.movieappaz.common.network.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkObserverModule {

    @Provides
    @Singleton
    fun injectConnectivityManager(@ApplicationContext context: Context)=context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    @Provides
    @Singleton
    fun injectNetworkConnectivityObserver(connectivityManager: ConnectivityManager)=NetworkConnectivityObserver(connectivityManager = connectivityManager)
}