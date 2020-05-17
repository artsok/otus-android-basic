package artsok.github.io.movie4k

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val uniqueId: Int,
    val imageId: Int,
    val title: String,
    val description: String,
    var selected: Boolean = false,
    var favorite: Boolean = false
) : Parcelable