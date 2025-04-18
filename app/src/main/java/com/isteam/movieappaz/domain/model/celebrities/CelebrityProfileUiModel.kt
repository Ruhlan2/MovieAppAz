package com.isteam.movieappaz.domain.model.celebrities

data class CelebrityProfileUiModel(
    val page: Int,
    val celebrityProfile: List<CelebritiesUiModel>,
    val totalPages: Int,
    val totalResults: Int
)
