package com.isteam.movieappaz.domain.model.credits

data class CrewUiModel(
    val adult: Boolean,
    val creditId: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String
)
