package com.isteam.movieappaz.data.dto.remote.favorites

data class FavoritesBody(
    val movieId: Long,
    val title: String,
    val description: String,
    val imageUrl: String
)
