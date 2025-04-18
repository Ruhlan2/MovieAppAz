package com.isteam.movieappaz.data.dto.remote.details.movieDetails


import com.google.gson.annotations.SerializedName

data class GenreDetails(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)