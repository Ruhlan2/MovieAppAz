package com.isteam.movieappaz.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieUiModel(
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val image: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val rate: Double,
    val voteCount: Int
):Parcelable
