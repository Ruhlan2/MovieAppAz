package com.isteam.movieappaz.domain.model.details.images

data class ImagesUiModel(
    val backdrops: List<BackdropUiModel>,
    val id: Int,
    val logos: List<LogoUiModel>,
    val posters: List<PosterUiModel>
)
