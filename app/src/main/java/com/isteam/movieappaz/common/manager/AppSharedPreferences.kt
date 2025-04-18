package com.isteam.movieappaz.common.manager

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.isteam.movieappaz.common.utils.Constants.DARK_MODE
import com.isteam.movieappaz.common.utils.Constants.IS_ONBOARDING_FINISHED
import com.isteam.movieappaz.common.utils.Constants.IS_REGISTER_FINISHED
import com.isteam.movieappaz.common.utils.Constants.LANGUAGE_CODE
import com.isteam.movieappaz.common.utils.Constants.LOCAL_DB
import com.isteam.movieappaz.common.utils.Constants.REMEMBER
import com.isteam.movieappaz.common.utils.Constants.STORY_FINISHED
import com.isteam.movieappaz.common.utils.Constants.THEME_IS_SELECTED_FROM_APP
import com.isteam.movieappaz.common.utils.Constants.TOKEN
import com.isteam.movieappaz.common.utils.LanguageCode
import com.isteam.movieappaz.common.utils.ThemeEnum
import javax.inject.Inject

class AppSharedPreferences @Inject constructor(private val context: Context) {

    private val masterKeyAlias by lazy {
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    }

    private val sp by lazy {
        EncryptedSharedPreferences.create(
            context,
            LOCAL_DB,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setIsDarkMode(themeEnum: ThemeEnum) = sp.edit().putString(DARK_MODE, themeEnum.name).apply()

    fun getIsDarkMode(): ThemeEnum {
        return when (sp.getString(DARK_MODE, ThemeEnum.LIGHT_MODE.name)
            ?: ThemeEnum.LIGHT_MODE.name) {
            ThemeEnum.DARK_MODE.name -> ThemeEnum.DARK_MODE
            ThemeEnum.LIGHT_MODE.name -> ThemeEnum.LIGHT_MODE
            else -> ThemeEnum.LIGHT_MODE
        }
    }

    fun setIsThemeSelectedFromApp(isSelected: Boolean) = sp.edit().putBoolean(
        THEME_IS_SELECTED_FROM_APP, isSelected
    ).apply()

    fun getIsThemeSelectedFromApp() = sp.getBoolean(THEME_IS_SELECTED_FROM_APP, false)

    fun setToken(token: String) = sp.edit().putString(TOKEN, token).apply()

    fun getToken(): String = sp.getString(TOKEN, "").orEmpty()

    fun removeToken() = sp.edit().remove(TOKEN).apply()

    fun setIsRegisterFinished(isFinished: Boolean) =
        sp.edit().putBoolean(IS_REGISTER_FINISHED, isFinished).apply()

    fun getIsRegisterFinished(): Boolean = sp.getBoolean(IS_REGISTER_FINISHED, false)

    fun setIsOnboardingFinished(isFinished: Boolean) =
        sp.edit().putBoolean(IS_ONBOARDING_FINISHED, isFinished).apply()

    fun getIsOnboardingFinished(): Boolean = sp.getBoolean(IS_ONBOARDING_FINISHED, false)

    fun setRemember(remember: Boolean) = sp.edit().putBoolean(REMEMBER, remember).apply()

    fun getRemember(): Boolean = sp.getBoolean(REMEMBER, false)

    fun setIsStoryFinished(isFinished: Boolean) =
        sp.edit().putBoolean(STORY_FINISHED, isFinished).apply()

    fun getIsStoryFinished() = sp.getBoolean(STORY_FINISHED, false)

    fun setLanguageCode(language: LanguageCode) =
        sp.edit().putString(LANGUAGE_CODE, language.code).apply()

    fun getLanguageCode() = sp.getString(LANGUAGE_CODE, LanguageCode.AZ.code)

}