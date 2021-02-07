package artsok.github.io.movie4k.data.repository

import artsok.github.io.movie4k.data.model.Schedule
import artsok.github.io.movie4k.data.model.toDomainModel
import artsok.github.io.movie4k.data.model.toMovieDomainModel
import artsok.github.io.movie4k.data.retrofit.MovieApiService
import artsok.github.io.movie4k.data.room.dao.MovieDao
import artsok.github.io.movie4k.data.room.dao.ScheduleDao
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.model.toModel
import artsok.github.io.movie4k.domain.repository.MovieRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class MovieRepositoryImpl(
    private val retrofitService: MovieApiService,
    private val movieDao: MovieDao,
    private val scheduleDao: ScheduleDao
) : MovieRepository {

    override fun getMovie(id: Int): Single<MovieDomainModel> {
        return retrofitService.getMovie(id)
            .subscribeOn(Schedulers.io())
            .map { it.toDomainModel() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun searchMovies(query: String): Flowable<List<MovieDomainModel>> {
        return retrofitService.searchMovies(query)
            .map { it.results.map { item -> item.toDomainModel() } }
    }

    override fun getMovies(page: Int): Single<List<MovieDomainModel>> {
        return retrofitService.popularFilmsByPage(page)
            .subscribeOn(Schedulers.io())
            .map { it.results.map { item -> item.toDomainModel() } }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUpcomingMovies(page: Int): Single<List<MovieDomainModel>> {
        return retrofitService.upcomingFilmsByPage(page)
            .subscribeOn(Schedulers.io())
            .map { it.results.map { item -> item.toDomainModel() } }
            .observeOn(AndroidSchedulers.mainThread())
    }


    override suspend fun insertToDB(movie: MovieDomainModel) {
        movieDao.insert(movie.toModel())
    }

    override suspend fun deleteFromDB(movie: MovieDomainModel) {
        movieDao.deleteFromDB(movie.toModel())
    }

    override fun getMoviesFromDB(): Flowable<List<MovieDomainModel>> {
        return movieDao.getMovies()
            .map { it.map { item -> item.toMovieDomainModel() } }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFavoriteMoviesFromDB(): Flowable<List<MovieDomainModel>> {
        return movieDao.getFavoriteMovies()
            .map { it.map { item -> item.toMovieDomainModel() } }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getScheduleMoviesFromDB(): Flowable<List<MovieDomainModel>> {
        return movieDao.getScheduleMovies()
            .map { it.map { item -> item.toMovieDomainModel() } }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun searchMoviesInDB(title: String): Flowable<List<MovieDomainModel>> {
        return movieDao.searchMovies(title)
            .map { it.map { item -> item.toMovieDomainModel() } }
    }

    override suspend fun deleteMoviesFromDB() {
        return movieDao.deleteAll()
    }

    override fun getTotalRecordsFromDB(): Single<Int> {
        return movieDao.getTotalRecordOfMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFavoriteTotalRecordsFromDB(): Single<Int> {
        return movieDao.getFavoriteTotalRecordOfMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override suspend fun updateDB(favorite: Boolean, id: Int) {
        val value = if (favorite) 1 else 0
        return movieDao.update(value, id)
    }

    override suspend fun updateDB(favorite: Boolean, title: String) {
        val value = if (favorite) 1 else 0
        return movieDao.update(value, title)
    }

    override suspend fun updateScheduledFieldsInDB(id: Int, scheduledTime: String) {
        return movieDao.updateScheduledFields(id, scheduledTime)
    }

    override suspend fun updateScheduledFlag(id: Int, flag: Boolean) {
        val value = if (flag) 1 else 0
        return movieDao.updateScheduledFlag(id, value)
    }

    override suspend fun saveScheduleInfoToDB(schedule: Schedule) {
        scheduleDao.insert(schedule)
    }

    override fun getRequestCodeFromDB(title: String, time: String): Single<Int> {
        return scheduleDao
            .getRequestCodeForScheduledMovie(title, time)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
