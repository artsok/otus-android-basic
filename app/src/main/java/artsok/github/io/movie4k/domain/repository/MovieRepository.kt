package artsok.github.io.movie4k.domain.repository

import androidx.lifecycle.LiveData
import artsok.github.io.movie4k.data.model.Movie
import artsok.github.io.movie4k.domain.model.MovieDomainModel

/*
 Repository for DB and API calls
 */
interface MovieRepository {
    suspend fun getLandingMovies(): List<MovieDomainModel>
    suspend fun getMovies(page: Int): List<MovieDomainModel>
    suspend fun insertToDB(movie: MovieDomainModel)
    suspend fun deleteFromDB(movie: MovieDomainModel)
    suspend fun deleteMoviesFromDB()
    suspend fun getTotalRecordsFromDB(): Int
    suspend fun getFavoriteTotalRecordsFromDB(): Int
    suspend fun updateDB(favorite: Boolean, id: Int)
    suspend fun updateDB(favorite: Boolean, title: String)
    suspend fun updateScheduledFields(id: Int, scheduledTime: String)
    suspend fun updateScheduledFlag(id: Int, flag: Boolean)

    fun getFavoriteMoviesFromDB(): LiveData<List<Movie>>
    fun getScheduleMoviesFromDB(): LiveData<List<Movie>>
    fun getMoviesFromDB(): LiveData<List<Movie>>
}
