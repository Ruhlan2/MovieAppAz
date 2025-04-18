package com.isteam.movieappaz.data.service.interceptor

import com.isteam.movieappaz.common.manager.SharedPreferencesManager
import com.isteam.movieappaz.common.utils.Constants.ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val lanCode = sharedPreferencesManager.getLanguageCode().code
        val newUrl =
            chain.request().url.newBuilder().addQueryParameter("language", lanCode)
                .build()

        val newRequest =
            chain.request().newBuilder().url(newUrl).header("Authorization", "Bearer $ACCESS_TOKEN")
                .build()

        return chain.proceed(newRequest)
    }
}