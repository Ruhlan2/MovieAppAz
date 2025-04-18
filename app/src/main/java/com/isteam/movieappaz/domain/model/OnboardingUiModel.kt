package com.isteam.movieappaz.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingUiModel(
    val id: Int,
    @DrawableRes val imageRes: Int?,
    @StringRes val title: Int,
    @StringRes val description: Int,
)
