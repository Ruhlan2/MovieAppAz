package com.isteam.movieappaz.domain.model.details.movieDetails

data class MovieDetailsUiModel(
    val adult: Boolean,
    val backdropPath: String,
    val budget: Long,
    val genres: List<GenreDetailsUiModel>,
    val homepage: String,
    val id: Int,
    val imdbId: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val productionCompanies: List<ProductionCompanyUiModel>,
    val productionCountries: List<ProductionCountryUiModel>,
    val releaseDate: String,
    val revenue: Long,
    val runtime: Int,
    val spokenLanguages: List<SpokenLanguageUiModel>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)
