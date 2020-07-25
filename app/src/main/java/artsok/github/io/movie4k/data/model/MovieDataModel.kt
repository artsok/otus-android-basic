package artsok.github.io.movie4k.data.model

import artsok.github.io.movie4k.domain.model.MovieDomainModel
import com.google.gson.annotations.SerializedName

data class MovieDataModel(
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("title") val title: String,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double
)

internal fun MovieDataModel.toDomainModel() = MovieDomainModel(
    uniqueId = this.id,
    title = this.title,
    description = this.overview,
    posterPath = this.posterPath ?: "",
    backdropPath = this.posterPath ?: ""
)