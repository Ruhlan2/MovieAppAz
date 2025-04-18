package com.isteam.movieappaz.domain.model

data class AiUiModel(
    val id: Int,
    val aiRole: String,
    val message: String,
    val time: String,
    val isResponse: Boolean = false
)
