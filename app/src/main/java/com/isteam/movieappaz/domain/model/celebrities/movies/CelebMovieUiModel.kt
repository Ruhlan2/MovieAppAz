package com.isteam.movieappaz.domain.model.celebrities.movies

import com.isteam.movieappaz.domain.model.MovieUiModel

data class CelebMovieUiModel(
    val cast: List<MovieUiModel>,
    val id: Int
)