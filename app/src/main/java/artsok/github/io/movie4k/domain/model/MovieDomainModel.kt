package artsok.github.io.movie4k.domain.model

import android.os.Parcelable
import artsok.github.io.movie4k.data.model.Movie
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDomainModel(
    val uniqueId: Int,
    val title: String,
    val description: String,
    val posterPath: String,
    val backdropPath: String,
    var selected: Boolean = false,
    var favorite: Boolean = false,
    var scheduled: Boolean = false,
    var scheduledTime: String = ""
) : Parcelable

internal fun MovieDomainModel.toModel() = Movie(
    title = this.title,
    description = this.description,
    posterPath = this.posterPath,
    backdropPath = this.posterPath,
    favorite = this.favorite,
    scheduled = false,
    scheduledTime = ""
)