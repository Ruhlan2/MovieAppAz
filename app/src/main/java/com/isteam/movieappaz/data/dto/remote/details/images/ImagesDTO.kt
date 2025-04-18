package com.isteam.movieappaz.data.dto.remote.details.images


import com.google.gson.annotations.SerializedName

data class ImagesDTO(
    @SerializedName("backdrops")
    val backdrops: List<Backdrop?>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("logos")
    val logos: List<Logo?>?,
    @SerializedName("posters")
    val posters: List<Poster?>?
)