package artsok.github.io.google.android.movie4k

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(val imageId:Int, val title:String, val description:String, var selected : Boolean = false) : Parcelable