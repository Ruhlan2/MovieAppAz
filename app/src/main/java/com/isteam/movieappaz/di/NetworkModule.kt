package com.isteam.movieappaz.di

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.gson.Gson
import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.common.utils.ResourcesProvider
import com.isteam.movieappaz.data.repository.AiRepository
import com.isteam.movieappaz.data.service.interceptor.AuthInterceptor
import com.isteam.movieappaz.data.service.remote.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPreferencesManager: SharedPreferencesManager): AuthInterceptor =
        AuthInterceptor(sharedPreferencesManager)

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)


    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        builder: OkHttpClient.Builder,
        authInterceptor: AuthInterceptor,
        interceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addNetworkInterceptor(interceptor)
        builder.addInterceptor(authInterceptor)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): MovieService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun injectGenerativeModel() = GenerativeModel(
        modelName = Constants.GEMINI_NAME,
        apiKey = Constants.GEMINI_API,
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE)
        )
    )

    @Singleton
    @Provides
    fun injectAiRepository(generativeModel: GenerativeModel) = AiRepository(generativeModel)

    @Singleton
    @Provides
    fun injectResourceProvider(@ApplicationContext context: Context) = ResourcesProvider(context)

}