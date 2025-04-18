package com.isteam.movieappaz.common.manager

import com.isteam.movieappaz.common.utils.LanguageCode
import com.isteam.movieappaz.common.utils.ThemeEnum
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(private val sp: AppSharedPreferences) {

    fun getToken(): String = sp.getToken()

    fun setToken(token: String, remember: Boolean) {
        sp.setToken(token)
        sp.setRemember(remember)
    }

    fun setDarkMode(themeEnum: ThemeEnum) = sp.setIsDarkMode(themeEnum)

    fun getDarkMode() = sp.getIsDarkMode()

    fun setIsThemeSelectedFromApp(isSelected: Boolean) = sp.setIsThemeSelectedFromApp(isSelected)

    fun getIsThemeSelectedFromApp() = sp.getIsThemeSelectedFromApp()

    fun setRemember(isRemember: Boolean) = sp.setRemember(isRemember)

    fun getRemember() = sp.getRemember()

    fun removeToken() = sp.removeToken()

    fun getIsRegisterFinished() = sp.getIsRegisterFinished()

    fun setIsRegisterFinished(isFinished: Boolean) = sp.setIsRegisterFinished(isFinished)

    fun getIsOnboardingFinished() = sp.getIsOnboardingFinished()

    fun setIsOnboardingFinished(isFinished: Boolean) = sp.setIsOnboardingFinished(isFinished)

    fun setIsStoryFinished(isFinished: Boolean) = sp.setIsStoryFinished(isFinished)

    fun getIsStoryFinished() = sp.getIsStoryFinished()

    fun setLanguageCode(language: LanguageCode) = sp.setLanguageCode(language)

    fun getLanguageCode(): LanguageCode {
        return when (sp.getLanguageCode()) {
            LanguageCode.AZ.code -> LanguageCode.AZ
            LanguageCode.EN.code -> LanguageCode.EN
            LanguageCode.RU.code -> LanguageCode.RU
            else -> LanguageCode.AZ
        }
    }


}