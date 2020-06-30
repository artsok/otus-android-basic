package artsok.github.io.movie4k.data

import android.os.Parcelable
import artsok.github.io.movie4k.network.MovieNetwork
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val uniqueId: Int,
    val title: String,
    val description: String,
    val posterPath: String,
    val backdropPath: String,
    var selected: Boolean = false,
    var favorite: Boolean = false
) : Parcelable {
    constructor(dto: MovieNetwork) : this(
        dto.id,
        dto.title,
        dto.overview,
        posterPath = dto.posterPath ?: "",
        backdropPath = dto.posterPath ?: "" //TODO: Разобраться с постером
    )
}