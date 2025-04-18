package com.isteam.movieappaz.domain.model.details.reviews

data class DetailsResultUiModel(
    val author: String,
    val authorDetails: AuthorDetailsUiModel,
    val content: String,
    val createdAt: String,
    val id: String,
    val updatedAt: String,
    val url: String
)
