package com.isteam.movieappaz.domain.model.credits

data class CreditsUiModel(
    val cast: List<CastUiModel>,
    val crew: List<CrewUiModel>,
    val id: Int
)
