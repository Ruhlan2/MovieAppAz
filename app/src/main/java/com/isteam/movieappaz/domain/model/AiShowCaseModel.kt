package com.isteam.movieappaz.domain.model

import androidx.annotation.RawRes

data class AiShowCaseModel(
    val id: Int,
    @RawRes val lottieAnim: Int?,
    val message: String
)
