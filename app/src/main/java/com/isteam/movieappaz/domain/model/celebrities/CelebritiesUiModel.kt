package com.isteam.movieappaz.domain.model.celebrities

data class CelebritiesUiModel(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownFor: List<KnownForUiModel>,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String
)
