package com.isteam.movieappaz.data.dto.remote


import com.google.gson.annotations.SerializedName

data class NowPlayingResponseDTO(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<MovieDTO>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)