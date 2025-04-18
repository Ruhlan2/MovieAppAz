package com.isteam.movieappaz.data.dto.remote.details.videos


import com.google.gson.annotations.SerializedName

data class VideosDTO(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("results")
    val results: List<VideoResult?>?
)