package com.isteam.movieappaz.domain.model.details.reviews

data class ReviewDetailsUiModel(
    val id: Int,
    val page: Int,
    val detailsResults: List<DetailsResultUiModel>,
    val totalPages: Int,
    val totalResults: Int
)
