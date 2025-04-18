package com.isteam.movieappaz.domain.model

import androidx.annotation.StringRes

data class GenreUiModel(val id: Int, @StringRes val name: Int, var isSelected: Boolean = false)
