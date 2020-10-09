package artsok.github.io.movie4k.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import artsok.github.io.movie4k.domain.model.MovieDomainModel

@Entity(tableName = "movie_table")
class Movie(
    val title: String,
    val description: String,
    val posterPath: String,
    val backdropPath: String,
    val favorite: Boolean = false,
    val selected: Boolean = false,
    val scheduled: Boolean = false,
    val scheduledTime: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}

fun Movie.toMovieDomainModel() = MovieDomainModel(
    uniqueId = id!!,
    title = this.title,
    description = this.description,
    posterPath = this.posterPath,
    backdropPath = this.backdropPath,
    favorite = this.favorite,
    selected = this.selected
)

