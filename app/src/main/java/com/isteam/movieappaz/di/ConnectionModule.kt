package com.isteam.movieappaz.di

import android.content.Context
import com.isteam.movieappaz.data.repository.ConnectionRepository
import com.isteam.movieappaz.data.service.connection.CheckConnectivity
import com.isteam.movieappaz.data.source.local.CheckNetworkSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {

    @Provides
    @Singleton
    fun injectConnection(@ApplicationContext context: Context)=CheckConnectivity(context)

    @Provides
    @Singleton
    fun injectSource(connectivity: CheckConnectivity,@ApplicationContext context: Context)=CheckNetworkSource(connectivity,context)

    @Provides
    @Singleton
    fun injectRepository(source: CheckNetworkSource)=ConnectionRepository(source)
}