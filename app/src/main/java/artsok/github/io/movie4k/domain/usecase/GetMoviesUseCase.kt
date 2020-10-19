package artsok.github.io.movie4k.domain.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import artsok.github.io.movie4k.data.model.Movie
import artsok.github.io.movie4k.data.model.Schedule
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.repository.MovieRepository
import java.io.IOException

/**
 *
 */
class GetMoviesUseCase(private val repository: MovieRepository) {

    companion object {
        val TAG = GetMoviesUseCase::class.toString()
    }

    sealed class Result {
        data class Success(val data: List<MovieDomainModel>) : Result()
        data class Error(val e: Throwable) : Result()
    }

    fun getMoviesFromDB(): LiveData<List<Movie>> {
        return repository.getMoviesFromDB()
    }

    fun getFavoritesMoviesFromDB(): LiveData<List<Movie>> {
        return repository.getFavoriteMoviesFromDB()
    }

    fun getScheduleMoviesFromDB(): LiveData<List<Movie>> {
        return repository.getScheduleMoviesFromDB()
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

    suspend fun saveScheduleInfoToDB(schedule: Schedule) {
        repository.saveScheduleInfoToDB(schedule)
    }

    suspend fun getRequestCodeFromDB(title: String, time: String): Int =
        repository.getRequestCodeFromDB(title, time)

    suspend fun deleteMoviesFromTable() = repository.deleteMoviesFromDB()

    suspend fun getMovieRecords(): Int = repository.getTotalRecordsFromDB()


    suspend fun getFavoriteMovieRecords(): Int {
        return repository.getFavoriteTotalRecordsFromDB()
    }

    suspend fun updateFavoriteField(favorite: Boolean, id: Int) {
        return repository.updateDB(favorite, id)
    }

    suspend fun updateMovieScheduledTime(id: Int, scheduledTime: String) {
        return repository.updateScheduledFields(id, scheduledTime)
    }

    suspend fun updateScheduledField(id: Int, flag: Boolean) {
        return repository.updateScheduledFlag(id, flag)
    }

    suspend fun updateFavoriteFieldByTitle(favorite: Boolean, title: String) {
        return repository.updateDB(favorite, title)
    }
}
