package com.isteam.movieappaz.common.utils

import androidx.annotation.StringRes

data class ValidationState(
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val nameError: Int? = null,
    @StringRes val nicknameError: Int? = null,
)
