package artsok.github.io.movie4k.domain.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import artsok.github.io.movie4k.data.model.Movie
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.repository.MovieRepository
import java.io.IOException

/**
 *
 */
class GetMoviesUseCase(private val repository: MovieRepository) {

    companion object {
        const val TAG = "GetMoviesUseCase"
    }

    sealed class Result {
        data class Success(val data: List<MovieDomainModel>) : Result()
        data class Error(val e: Throwable) : Result()
    }

    fun getMoviesFromDB(): LiveData<List<Movie>> {
        return repository.getMoviesFromDB();
    }

    fun getFavoritesMoviesFromDB(): LiveData<List<Movie>> {
        return repository.getFavoriteMoviesFromDB()
    }

    suspend fun fetchPopularMovies(page: Int): Result {
        Log.d(TAG, "page $page")
        return try {
            Result.Success(repository.getMovies(page)
                .filter { it.backdropPath.isNotBlank() && it.posterPath.isNotBlank() }
            )
        } catch (e: IOException) {
            Result.Error(e)
        }
    }

    suspend fun insertToDB(movies: List<MovieDomainModel>) {
        movies.forEach { repository.insertToDB(it) }
    }

    suspend fun saveRowResponse(page: Int, movies: List<MovieDomainModel>) {

    }

    suspend fun deleteFromDB(movie: MovieDomainModel) {
        repository.deleteFromDB(movie)
    }

    suspend fun deleteMoviesFromTable() {
        repository.deleteMoviesFromDB()
    }

    suspend fun getMovieRecords(): Int {
        return repository.getTotalRecordsFromDB()
    }

    suspend fun getFavoriteMovieRecords(): Int {
        return repository.getFavoriteTotalRecordsFromDB()
    }

    suspend fun updateFavoriteField(favorite: Boolean, id: Int) {
        return repository.updateDB(favorite, id)
    }

    suspend fun updateMovieScheduledTime(id: Int, scheduledTime: String) {
        return repository.updateScheduledFields(id, scheduledTime)
    }

    suspend fun updateFavoriteFieldByTitle(favorite: Boolean, title: String) {
        return repository.updateDB(favorite, title)
    }
}
