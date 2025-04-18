package com.isteam.movieappaz.data.mapper

import android.webkit.URLUtil
import com.isteam.movieappaz.common.utils.Constants
import com.isteam.movieappaz.common.utils.fullMonthTimeFormat
import com.isteam.movieappaz.data.dto.remote.MovieDTO
import com.isteam.movieappaz.data.dto.remote.celebrities.Celebrity
import com.isteam.movieappaz.data.dto.remote.celebrities.CelebrityDTO
import com.isteam.movieappaz.data.dto.remote.celebrities.KnownFor
import com.isteam.movieappaz.data.dto.remote.celebrities.detail.CelebrityDetailsDTO
import com.isteam.movieappaz.data.dto.remote.celebrities.movies.CelebMovieDTO
import com.isteam.movieappaz.data.dto.remote.credits.Cast
import com.isteam.movieappaz.data.dto.remote.credits.CreditsDTO
import com.isteam.movieappaz.data.dto.remote.credits.Crew
import com.isteam.movieappaz.data.dto.remote.details.images.Backdrop
import com.isteam.movieappaz.data.dto.remote.details.images.ImagesDTO
import com.isteam.movieappaz.data.dto.remote.details.images.Logo
import com.isteam.movieappaz.data.dto.remote.details.images.Poster
import com.isteam.movieappaz.data.dto.remote.details.movieDetails.GenreDetails
import com.isteam.movieappaz.data.dto.remote.details.movieDetails.MovieDetailsDTO
import com.isteam.movieappaz.data.dto.remote.details.movieDetails.ProductionCompany
import com.isteam.movieappaz.data.dto.remote.details.movieDetails.ProductionCountry
import com.isteam.movieappaz.data.dto.remote.details.movieDetails.SpokenLanguage
import com.isteam.movieappaz.data.dto.remote.details.reviews.AuthorDetails
import com.isteam.movieappaz.data.dto.remote.details.reviews.DetailsResult
import com.isteam.movieappaz.data.dto.remote.details.reviews.ReviewDetailsDTO
import com.isteam.movieappaz.data.dto.remote.details.videos.VideoResult
import com.isteam.movieappaz.data.dto.remote.details.videos.VideosDTO
import com.isteam.movieappaz.data.dto.remote.favorites.FavoritesDTO
import com.isteam.movieappaz.data.dto.remote.notifications.NotificationsDTO
import com.isteam.movieappaz.data.dto.remote.profile.ProfileDTO
import com.isteam.movieappaz.domain.model.MovieUiModel
import com.isteam.movieappaz.domain.model.celebrities.CelebritiesUiModel
import com.isteam.movieappaz.domain.model.celebrities.CelebrityProfileUiModel
import com.isteam.movieappaz.domain.model.celebrities.KnownForUiModel
import com.isteam.movieappaz.domain.model.celebrities.details.CelebrityDetailsUiModel
import com.isteam.movieappaz.domain.model.celebrities.movies.CelebMovieUiModel
import com.isteam.movieappaz.domain.model.credits.CastUiModel
import com.isteam.movieappaz.domain.model.credits.CreditsUiModel
import com.isteam.movieappaz.domain.model.credits.CrewUiModel
import com.isteam.movieappaz.domain.model.details.images.BackdropUiModel
import com.isteam.movieappaz.domain.model.details.images.ImagesUiModel
import com.isteam.movieappaz.domain.model.details.images.LogoUiModel
import com.isteam.movieappaz.domain.model.details.images.PosterUiModel
import com.isteam.movieappaz.domain.model.details.movieDetails.GenreDetailsUiModel
import com.isteam.movieappaz.domain.model.details.movieDetails.MovieDetailsUiModel
import com.isteam.movieappaz.domain.model.details.movieDetails.ProductionCompanyUiModel
import com.isteam.movieappaz.domain.model.details.movieDetails.ProductionCountryUiModel
import com.isteam.movieappaz.domain.model.details.movieDetails.SpokenLanguageUiModel
import com.isteam.movieappaz.domain.model.details.reviews.AuthorDetailsUiModel
import com.isteam.movieappaz.domain.model.details.reviews.DetailsResultUiModel
import com.isteam.movieappaz.domain.model.details.reviews.ReviewDetailsUiModel
import com.isteam.movieappaz.domain.model.details.video.VideoResultUiModel
import com.isteam.movieappaz.domain.model.details.video.VideoUiModel
import com.isteam.movieappaz.domain.model.favorites.FavoritesUiModel
import com.isteam.movieappaz.domain.model.notifications.NotificationUiModel
import com.isteam.movieappaz.domain.model.profile.ProfileUiModel

fun MovieDTO?.toMovieUiModel(): MovieUiModel {
    return MovieUiModel(
        id = this?.id ?: 0,
        image = Constants.BASE_URL_IMAGE + this?.posterPath.orEmpty(),
        rate = this?.voteAverage ?: 0.0,
        adult = this?.adult ?: false,
        backdropPath = this?.backdropPath.orEmpty(),
        genreIds = this?.genreIds.orEmpty(),
        originalTitle = this?.originalTitle.orEmpty(),
        originalLanguage = this?.originalLanguage.orEmpty(),
        overview = this?.overview.orEmpty(),
        popularity = this?.popularity ?: 0.0,
        releaseDate = this?.releaseDate.orEmpty(),
        title = this?.title.orEmpty(),
        video = this?.video ?: false,
        voteCount = this?.voteCount ?: 0

    )
}

fun List<MovieDTO?>?.toListMovieUiModel(): List<MovieUiModel> = this?.mapNotNull {
    it.toMovieUiModel()
}.orEmpty()

fun SpokenLanguage?.toSpokenLanguageUiModel() = SpokenLanguageUiModel(
    englishName = this?.englishName.orEmpty(),
    iso6391 = this?.iso6391.orEmpty(),
    name = this?.name.orEmpty()
)

fun ProductionCompany?.toProductionCompanyUiModel() = ProductionCompanyUiModel(
    id = this?.id ?: -1,
    logoPath = this?.logoPath.orEmpty(),
    name = this?.name.orEmpty(),
    originCountry = this?.originCountry.orEmpty()
)

fun ProductionCountry?.toProductionCountryUiModel() = ProductionCountryUiModel(
    iso31661 = this?.iso31661.orEmpty(),
    name = this?.name.orEmpty()
)

fun GenreDetails?.toGenreUiModel() = GenreDetailsUiModel(
    id = this?.id ?: -1,
    name = this?.name.orEmpty()
)

fun List<GenreDetails?>?.toListGenreUiModel() = this?.mapNotNull { it.toGenreUiModel() }.orEmpty()

fun List<ProductionCompany?>?.toListProductionCompanyUiModel() =
    this?.mapNotNull { it.toProductionCompanyUiModel() }.orEmpty()

fun List<ProductionCountry?>?.toListProductionCountryUiModel() =
    this?.mapNotNull { it.toProductionCountryUiModel() }.orEmpty()

fun List<SpokenLanguage?>?.toListSpokenLanguageUiModel() =
    this?.mapNotNull { it.toSpokenLanguageUiModel() }.orEmpty()

fun MovieDetailsDTO?.toMovieDetailsUiModel() = MovieDetailsUiModel(
    adult = this?.adult ?: false,
    backdropPath = this?.backdropPath.orEmpty(),
    budget = this?.budget ?: 0,
    genres = this?.genreDetails.toListGenreUiModel(),
    homepage = this?.homepage.orEmpty(),
    id = this?.id ?: -1,
    imdbId = this?.imdbId.orEmpty(),
    originalLanguage = this?.originalLanguage.orEmpty(),
    originalTitle = this?.originalTitle.orEmpty(),
    overview = this?.overview.orEmpty(),
    popularity = this?.popularity ?: 0.0,
    posterPath = this?.posterPath.orEmpty(),
    productionCompanies = this?.productionCompanies.toListProductionCompanyUiModel(),
    productionCountries = this?.productionCountries.toListProductionCountryUiModel(),
    releaseDate = this?.releaseDate.orEmpty(),
    revenue = this?.revenue ?: 0,
    runtime = this?.runtime ?: 0,
    spokenLanguages = this?.spokenLanguages.toListSpokenLanguageUiModel(),
    status = this?.status.orEmpty(),
    tagline = this?.tagline.orEmpty(),
    title = this?.title.orEmpty(),
    video = this?.video ?: false,
    voteAverage = this?.voteAverage ?: 0.0,
    voteCount = this?.voteCount ?: 0
)


fun Crew?.toCrewUiModel() = CrewUiModel(
    adult = this?.adult ?: false,
    creditId = this?.creditId.orEmpty(),
    department = this?.department.orEmpty(),
    gender = this?.gender ?: -1,
    id = this?.id ?: -1,
    job = this?.job.orEmpty(),
    knownForDepartment = this?.knownForDepartment.orEmpty(),
    name = this?.name.orEmpty(),
    originalName = this?.originalName.orEmpty(),
    popularity = this?.popularity ?: 0.0,
    profilePath = this?.profilePath.orEmpty()
)

fun Cast?.toCastUiModel() = CastUiModel(
    adult = this?.adult ?: false,
    creditId = this?.creditId.orEmpty(),
    character = this?.character.orEmpty(),
    gender = this?.gender ?: -1,
    id = this?.id ?: -1,
    knownForDepartment = this?.knownForDepartment.orEmpty(),
    name = this?.name.orEmpty(),
    originalName = this?.originalName.orEmpty(),
    popularity = this?.popularity ?: 0.0,
    profilePath = this?.profilePath.orEmpty(),
    castId = this?.castId ?: -1,
    order = this?.order ?: 0
)

fun List<Crew?>?.toCrewUiModel() = this?.mapNotNull {
    it.toCrewUiModel()
}.orEmpty()

fun List<Cast?>?.toCastUiModel() = this?.mapNotNull { it.toCastUiModel() }.orEmpty()

fun CreditsDTO?.toCreditsUiModel() = CreditsUiModel(
    cast = this?.cast.toCastUiModel(),
    crew = this?.crew.toCrewUiModel(),
    id = this?.id ?: -1
)


fun Backdrop?.toBackdropUiModel() = BackdropUiModel(
    aspectRatio = this?.aspectRatio ?: 0.0,
    filePath = this?.filePath.orEmpty(),
    height = this?.height ?: 0,
    iso6391 = this?.iso6391.orEmpty(),
    voteAverage = this?.voteAverage ?: 0.0,
    voteCount = this?.voteCount ?: 0,
    width = this?.width ?: 0
)

fun Logo?.toLogoUiModel() = LogoUiModel(
    aspectRatio = this?.aspectRatio ?: 0.0,
    filePath = this?.filePath.orEmpty(),
    height = this?.height ?: 0,
    iso6391 = this?.iso6391.orEmpty(),
    voteAverage = this?.voteAverage ?: 0.0,
    voteCount = this?.voteCount ?: 0,
    width = this?.width ?: 0
)

fun Poster?.toPosterUiModel() = PosterUiModel(
    aspectRatio = this?.aspectRatio ?: 0.0,
    filePath = this?.filePath.orEmpty(),
    height = this?.height ?: 0,
    iso6391 = this?.iso6391.orEmpty(),
    voteAverage = this?.voteAverage ?: 0.0,
    voteCount = this?.voteCount ?: 0,
    width = this?.width ?: 0
)

fun List<Backdrop?>?.toListBackdropUiModel() = this?.mapNotNull {
    it.toBackdropUiModel()
}.orEmpty()


fun List<Logo?>?.toListLogoUiModel() = this?.mapNotNull {
    it.toLogoUiModel()
}.orEmpty()

fun List<Poster?>?.toListPosterUiModel() = this?.mapNotNull {
    it.toPosterUiModel()
}.orEmpty()

fun ImagesDTO?.toImagesUiModel() = ImagesUiModel(
    id = this?.id ?: -1,
    backdrops = this?.backdrops.toListBackdropUiModel(),
    logos = this?.logos.toListLogoUiModel(),
    posters = this?.posters.toListPosterUiModel()
)

fun VideoResult?.toVideoResultUiModel() = VideoResultUiModel(
    id = this?.id.orEmpty(),
    iso6391 = this?.iso6391.orEmpty(),
    iso31661 = this?.iso31661.orEmpty(),
    key = this?.key.orEmpty(),
    name = this?.name.orEmpty(),
    official = this?.official ?: false,
    publishedAt = this?.publishedAt.orEmpty(),
    site = this?.site.orEmpty(),
    size = this?.size ?: -1,
    type = this?.type.orEmpty()
)

fun List<VideoResult?>?.toVideoResultUiModel() = this?.mapNotNull {
    it.toVideoResultUiModel()
}.orEmpty()

fun VideosDTO?.toVideoUiModel() = VideoUiModel(
    id = this?.id ?: -1,
    result = this?.results.toVideoResultUiModel()
)

fun AuthorDetails?.toAuthorDetailsUiModel() = AuthorDetailsUiModel(
    avatarPath = this?.avatarPath.orEmpty(),
    name = this?.name.orEmpty(),
    rating = this?.rating ?: 0.0,
    username = this?.username.orEmpty()
)

fun DetailsResult?.toDetailsResultUiModel() = DetailsResultUiModel(
    author = this?.author.orEmpty(),
    authorDetails = this?.authorDetails.toAuthorDetailsUiModel(),
    content = this?.content.orEmpty(),
    createdAt = this?.createdAt.orEmpty(),
    id = this?.id.orEmpty(),
    updatedAt = this?.updatedAt.orEmpty(),
    url = this?.url.orEmpty()
)

fun List<DetailsResult?>?.toListDetailsResultUiModel() = this?.mapNotNull {
    it.toDetailsResultUiModel()
}.orEmpty()

fun ReviewDetailsDTO?.toReviewDetailsUiModel() = ReviewDetailsUiModel(
    id = this?.id ?: -1,
    page = this?.page ?: -1,
    detailsResults = this?.detailsResults.toListDetailsResultUiModel(),
    totalPages = this?.totalPages ?: -1,
    totalResults = this?.totalResults ?: -1
)

fun FavoritesDTO?.toFavoritesUiModel() = FavoritesUiModel(
    id = this?.id.orEmpty(),
    movieId = this?.movieId ?: -1,
    title = this?.title.orEmpty(),
    description = this?.description.orEmpty(),
    imageUrl = this?.imageUrl.orEmpty()
)

fun List<FavoritesDTO?>?.toListFavoritesUiModel() = this?.mapNotNull {
    it.toFavoritesUiModel()
}.orEmpty()

fun ProfileDTO?.toProfileUiModel() = ProfileUiModel(
    email = this?.email.orEmpty(),
    fullName = this?.fullName.orEmpty(),
    nickname = this?.nickname.orEmpty(),
    imageUrl = this?.imageUrl.orEmpty(),
)
fun KnownFor?.toKnownForUiModel() =KnownForUiModel(
    adult = this?.adult?:false,
    backdropPath = this?.backdropPath.orEmpty(),
    firstAirDate = this?.firstAirDate.orEmpty(),
    genreIds = this?.genreIds.orEmpty(),
    id = this?.id?:-1,
    mediaType = this?.mediaType.orEmpty(),
    name = this?.name.orEmpty(),
    originCountry = this?.originCountry.orEmpty(),
    originalLanguage = this?.originalLanguage.orEmpty(),
    originalName = this?.originalName.orEmpty(),
    originalTitle=this?.originalTitle.orEmpty(),
    overview=this?.overview.orEmpty(),
    popularity =this?.popularity?:0.0,
    posterPath = this?.posterPath.orEmpty(),
    releaseDate=this?.releaseDate.orEmpty(),
    title = this?.title.orEmpty(),
    video=this?.video?:false,
    voteAverage=this?.voteAverage?:0.0,
    voteCount = this?.voteCount?:-1
)
fun List<KnownFor?>?.toKnownForUiModel() = this?.mapNotNull {
    it.toKnownForUiModel()
}.orEmpty()

fun CelebrityDTO?.toCelebrityProfileUiModel() =CelebritiesUiModel(
    adult=this?.adult?:false,
    gender = this?.gender?:0,
    id = this?.id?:-1,
    knownFor = this?.knownFor.toKnownForUiModel(),
    knownForDepartment=this?.knownForDepartment.orEmpty(),
    name = this?.name.orEmpty(),
    originalName = this?.originalName.orEmpty(),
    popularity = this?.popularity?:0.0,
    profilePath = this?.profilePath.orEmpty()
)
fun List<CelebrityDTO?>?.toCelebrityProfileUiModel() =this?.mapNotNull {
    it.toCelebrityProfileUiModel()
}.orEmpty()
fun Celebrity?.toCelebritiesUiModel() =CelebrityProfileUiModel(
    page = this?.page?:-1,
    celebrityProfile = this?.celebrityProfile.toCelebrityProfileUiModel(),
    totalPages = this?.totalPages?:-1,
    totalResults = this?.totalResults?:-1
)

fun CelebrityDetailsDTO?.toCelebrityDetailsUiModel() =CelebrityDetailsUiModel(
        birthday = this?.birthday?.fullMonthTimeFormat().orEmpty(),
        known_for_department = this?.knownForDepartment.orEmpty(),
        name = this?.name.orEmpty(),
        profile_path = this?.profilePath.orEmpty(),
        biography = this?.biography.orEmpty()
)
fun CelebMovieDTO?.toCelebMovieUiModel()= CelebMovieUiModel(
    id = this?.id?:-1,
    cast = this?.cast.toListMovieUiModel()
)

fun NotificationsDTO.toNotificationUiModel() = NotificationUiModel(
    id = this.id ?: -1,
    title = this.title.orEmpty(),
    description = this.description.orEmpty(),
    imageUrl = imageUrl?.let { if (URLUtil.isValidUrl(it)) it else "" } ?: this.imageUrl.orEmpty()
)

fun List<NotificationsDTO>?.toNotificationListUiModel() = this?.map {
    it.toNotificationUiModel()
}.orEmpty()