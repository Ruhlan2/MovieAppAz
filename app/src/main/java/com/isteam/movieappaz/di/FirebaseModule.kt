package com.isteam.movieappaz.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.isteam.movieappaz.data.repository.FirebaseAuthRepository
import com.isteam.movieappaz.data.repository.FirebaseRepository
import com.isteam.movieappaz.data.service.google.GoogleClient
import com.isteam.movieappaz.data.source.remote.FirebaseAuthDataSource
import com.isteam.movieappaz.data.source.remote.FirebaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Singleton
    @Provides
    fun injectFireStore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun injectAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun injectStorage() = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun injectFirebaseMessaging() = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun injectFirebaseAuthSource(auth: FirebaseAuth, fireStore: FirebaseFirestore) =
        FirebaseAuthDataSource(auth, fireStore)

    @Singleton
    @Provides
    fun injectFirebaseAuthRepository(source: FirebaseAuthDataSource) =
        FirebaseAuthRepository(source)

    @Singleton
    @Provides
    fun injectFirebaseDataSource(
        fireStore: FirebaseFirestore,
        storage: FirebaseStorage
    ) = FirebaseDataSource(fireStore, storage)

    @Singleton
    @Provides
    fun injectFirebase(
        source: FirebaseDataSource
    ) = FirebaseRepository(source)


    @Singleton
    @Provides
    fun injectGoogleAuthService(@ApplicationContext context:Context,auth: FirebaseAuth)=
        GoogleClient(auth = auth, context = context)
}