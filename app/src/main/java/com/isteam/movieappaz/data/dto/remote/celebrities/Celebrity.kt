package com.isteam.movieappaz.data.dto.remote.celebrities


import com.google.gson.annotations.SerializedName

data class Celebrity(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val celebrityProfile: List<CelebrityDTO>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)