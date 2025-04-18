package com.isteam.movieappaz.domain.model.favorites

data class FavoritesUiModel(
    val id: String,
    val movieId: Long,
    val title: String,
    val description: String,
    val imageUrl: String
)
