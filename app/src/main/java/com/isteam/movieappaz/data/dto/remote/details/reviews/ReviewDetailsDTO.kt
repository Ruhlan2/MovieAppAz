package com.isteam.movieappaz.data.dto.remote.details.reviews


import com.google.gson.annotations.SerializedName

data class ReviewDetailsDTO(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val detailsResults: List<DetailsResult>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)