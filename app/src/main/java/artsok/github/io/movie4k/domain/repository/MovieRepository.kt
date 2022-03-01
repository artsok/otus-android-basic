package artsok.github.io.movie4k.domain.repository

import artsok.github.io.movie4k.data.model.Schedule
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

/*
 Repository for DB and API calls
 */
interface MovieRepository {
    fun getMovies(page: Int): Single<List<MovieDomainModel>>
    fun getUpcomingMovies(page: Int): Single<List<MovieDomainModel>>
    fun getMovie(id: Int): Single<MovieDomainModel>
    fun searchMovies(query: String): Flowable<List<MovieDomainModel>>

    suspend fun insertToDB(movie: MovieDomainModel)
    suspend fun deleteFromDB(movie: MovieDomainModel)
    suspend fun deleteMoviesFromDB()
    suspend fun updateDB(favorite: Boolean, id: Int)
    suspend fun updateDB(favorite: Boolean, title: String)
    suspend fun updateScheduledFieldsInDB(id: Int, scheduledTime: String)
    suspend fun updateScheduledFlag(id: Int, flag: Boolean)
    suspend fun saveScheduleInfoToDB(schedule: Schedule)

    fun searchMoviesInDB(title: String): Flowable<List<MovieDomainModel>>
    fun getTotalRecordsFromDB(): Single<Int>
    fun getFavoriteTotalRecordsFromDB(): Single<Int>
    fun getRequestCodeFromDB(title: String, time: String): Single<Int>

    fun getFavoriteMoviesFromDB(): Flowable<List<MovieDomainModel>>
    fun getScheduleMoviesFromDB(): Flowable<List<MovieDomainModel>>
    fun getMoviesFromDB(): Flowable<List<MovieDomainModel>>

}
