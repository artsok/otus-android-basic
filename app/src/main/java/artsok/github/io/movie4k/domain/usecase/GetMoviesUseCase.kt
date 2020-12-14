package artsok.github.io.movie4k.domain.usecase

import android.util.Log
import artsok.github.io.movie4k.data.model.Schedule
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.repository.MovieRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

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

    fun getMoviesFromDB(): Flowable<List<MovieDomainModel>> {
        return repository.getMoviesFromDB()
    }

    fun getFavoritesMoviesFromDB(): Flowable<List<MovieDomainModel>> {
        return repository.getFavoriteMoviesFromDB()
    }

    fun getScheduleMoviesFromDB(): Flowable<List<MovieDomainModel>> {
        return repository.getScheduleMoviesFromDB()
    }

    fun getRequestCodeFromDB(title: String, time: String): Single<Int> {
        return repository.getRequestCodeFromDB(title, time)
    }

    fun getMovieRecords(): Single<Int> {
        return repository.getTotalRecordsFromDB()
    }

    fun getFavoriteMovieRecords(): Single<Int> {
        return repository.getFavoriteTotalRecordsFromDB()
    }

    fun fetchMovies(page: Int, currentCategory: String): Single<List<MovieDomainModel>> {
        Log.d(TAG, "page $page, category $currentCategory")
        return when (currentCategory) {
            "UPCOMING" ->
                repository.getUpcomingMovies(page)
            else ->
                repository.getMovies(page)
        }
    }

    suspend fun insertToDB(movies: List<MovieDomainModel>) {
        movies.forEach { repository.insertToDB(it) }
    }

    suspend fun saveScheduleInfoToDB(schedule: Schedule) {
        repository.saveScheduleInfoToDB(schedule)
    }

    suspend fun deleteMoviesFromTable() {
        repository.deleteMoviesFromDB()
    }

    suspend fun updateFavoriteField(favorite: Boolean, id: Int) {
        return repository.updateDB(favorite, id)
    }

    suspend fun updateMovieScheduledTime(id: Int, scheduledTime: String) {
        return repository.updateScheduledFieldsInDB(id, scheduledTime)
    }

    suspend fun updateScheduledField(id: Int, flag: Boolean) {
        return repository.updateScheduledFlag(id, flag)
    }

    suspend fun updateFavoriteFieldByTitle(favorite: Boolean, title: String) {
        return repository.updateDB(favorite, title)
    }
}
