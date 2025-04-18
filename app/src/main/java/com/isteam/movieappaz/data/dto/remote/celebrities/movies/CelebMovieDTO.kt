package com.isteam.movieappaz.data.dto.remote.celebrities.movies


import com.google.gson.annotations.SerializedName
import com.isteam.movieappaz.data.dto.remote.MovieDTO

data class CelebMovieDTO(
    @SerializedName("cast")
    val cast: List<MovieDTO?>?,
    @SerializedName("id")
    val id: Int?
)