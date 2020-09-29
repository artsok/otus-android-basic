package artsok.github.io.movie4k.data.model

import artsok.github.io.movie4k.domain.model.MovieDomainModel
import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double
)

data class MovieListDto(
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResult: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<MovieDto>
)

internal fun MovieDto.toDomainModel() = MovieDomainModel(
    uniqueId = this.id,
    title = this.title,
    description = this.overview,
    posterPath = this.posterPath ?: "",
    backdropPath = this.posterPath ?: ""
)