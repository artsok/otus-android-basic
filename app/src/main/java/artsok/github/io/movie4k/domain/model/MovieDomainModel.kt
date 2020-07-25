package artsok.github.io.movie4k.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDomainModel(
    val uniqueId: Int,
    val title: String,
    val description: String,
    val posterPath: String,
    val backdropPath: String,
    var selected: Boolean = false,
    var favorite: Boolean = false
) : Parcelable
