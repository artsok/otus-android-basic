package artsok.github.io.movie4k.data.repository

import androidx.lifecycle.LiveData
import artsok.github.io.movie4k.data.model.Movie
import artsok.github.io.movie4k.data.model.Schedule
import artsok.github.io.movie4k.data.model.toDomainModel
import artsok.github.io.movie4k.data.retrofit.MovieApiService
import artsok.github.io.movie4k.data.room.dao.MovieDao
import artsok.github.io.movie4k.data.room.dao.ScheduleDao
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.model.toModel
import artsok.github.io.movie4k.domain.repository.MovieRepository

const val INIT_PAGE = 1

class MovieRepositoryImpl(
    private val retrofitService: MovieApiService,
    private val movieDao: MovieDao,
    private val scheduleDao: ScheduleDao
) : MovieRepository {

    override suspend fun getLandingMovies(): List<MovieDomainModel> =
        retrofitService.getPopularFilmsByPage(INIT_PAGE).results.map { it.toDomainModel() }

    override suspend fun getMovies(page: Int): List<MovieDomainModel> =
        retrofitService.getPopularFilmsByPage(page).results.map { it.toDomainModel() }

    override suspend fun insertToDB(movie: MovieDomainModel) {
        movieDao.insert(movie.toModel())
    }

    override suspend fun deleteFromDB(movie: MovieDomainModel) {
        movieDao.deleteFromDB(movie.toModel())
    }

    override fun getMoviesFromDB(): LiveData<List<Movie>> {
        return movieDao.getMovies()
    }

    override suspend fun deleteMoviesFromDB() {
        return movieDao.deleteAll()
    }

    override suspend fun getTotalRecordsFromDB(): Int {
        return movieDao.getTotalRecordOfMovies()
    }

    override suspend fun getFavoriteTotalRecordsFromDB(): Int {
        return movieDao.getFavoriteTotalRecordOfMovies()
    }

    override suspend fun updateDB(favorite: Boolean, id: Int) {
        val value = if (favorite) 1 else 0
        return movieDao.update(value, id)
    }

    override suspend fun updateDB(favorite: Boolean, title: String) {
        val value = if (favorite) 1 else 0
        return movieDao.update(value, title)
    }

    override suspend fun updateScheduledFields(id: Int, scheduledTime: String) {
        return movieDao.updateScheduledFields(id, scheduledTime)
    }

    override suspend fun updateScheduledFlag(id: Int, flag: Boolean) {
        val value = if (flag) 1 else 0
        return movieDao.updateScheduledFlag(id, value)
    }

    override suspend fun saveScheduleInfoToDB(schedule: Schedule) {
        scheduleDao.insert(schedule)
    }

    override suspend fun getRequestCodeFromDB(title: String, time: String): Int {
        return scheduleDao.getRequestCodeForScheduledMovie(title, time)
    }

    override fun getFavoriteMoviesFromDB(): LiveData<List<Movie>> {
        return movieDao.getFavoriteMovies()
    }

    override fun getScheduleMoviesFromDB(): LiveData<List<Movie>> {
        return movieDao.getScheduleMovies()
    }
}
