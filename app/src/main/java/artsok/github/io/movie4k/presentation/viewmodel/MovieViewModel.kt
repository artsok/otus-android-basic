package artsok.github.io.movie4k.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import artsok.github.io.movie4k.data.model.Schedule
import artsok.github.io.movie4k.data.model.toMovieDomainModel
import artsok.github.io.movie4k.data.repository.MovieRepositoryImpl
import artsok.github.io.movie4k.data.retrofit.MovieApi
import artsok.github.io.movie4k.data.room.AppDatabase
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.usecase.GetMoviesUseCase
import artsok.github.io.movie4k.extensions.get
import artsok.github.io.movie4k.extensions.put
import artsok.github.io.movie4k.lastResponseTime
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private val pageInit = 1
    private val preferName = "PrefStorage"
    private val category = FirebaseRemoteConfig.getInstance().getString("FilmCategory")
    private val sharedPref = application.getSharedPreferences(preferName, Context.MODE_PRIVATE)

    private var page = AtomicInteger(pageInit)
    private val moviesLiveData = MutableLiveData<List<MovieDomainModel>>()
    private val errorLiveData = MutableLiveData<String>()
    private val selectedMovieLiveData = MutableLiveData<MovieDomainModel>()
    private val useCase = GetMoviesUseCase(
        MovieRepositoryImpl(
            MovieApi.movieService,
            AppDatabase.getInstance(getApplication(), viewModelScope).movieDao(),
            AppDatabase.getInstance(getApplication(), viewModelScope).scheduleDao()
        )
    )

    companion object {
        val TAG = MovieViewModel::class.toString()
    }


    val error: LiveData<String>
        get() = errorLiveData

    val selectedMovie: LiveData<MovieDomainModel>
        get() = selectedMovieLiveData


    /**
     * Delete data from DB (table: movies)
     */
    fun clearAndInitData() {
        val currentTime = LocalTime.now()
        val lastResponseTime =
            LocalTime.parse(
                sharedPref.get(lastResponseTime, currentTime.toString()),
                DateTimeFormatter.ISO_LOCAL_TIME
            )

        val interval = 1L //Change to 20 minutes
        if (lastResponseTime.plusMinutes(interval).isBefore(currentTime)) {
            Log.d(TAG, "delete data from DB")
            viewModelScope.launch(Dispatchers.IO) {
                useCase.deleteMoviesFromTable()
                page.set(pageInit)
                getMoviesByPage(page.get())
                Log.d(TAG, "Total records ${useCase.getMovieRecords()}")
            }
        }
    }


    /**
     * Check if DB (Table Movies) is empty or not
     */
    fun isDbEmpty(): Boolean {
        var records = 0
        viewModelScope.launch(Dispatchers.IO) {
            records = useCase.getMovieRecords()
        }
        return records == 0
    }

    /**
     * Read https://stackoverflow.com/questions/44582019/how-to-clear-livedata-stored-value and
     * use to Event wrapper preferred or SingleLiveEvent
     */
    fun resetLiveDataValue() {
        moviesLiveData.postValue(listOf())
    }

    fun onErrorDisplayed() {
        errorLiveData.postValue(null)
    }

    /**
     * Download movies from network and save to DB
     */
    fun getMovies() {
        Log.d(TAG, "call getMovies. Page - $page, category - $category")
        useCase.fetchMovies(page.getAndIncrement(), category)
            .subscribe(object : SingleObserver<List<MovieDomainModel>> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "onSubscribe")
                }

                override fun onSuccess(list: List<MovieDomainModel>) {
                    if (list.isEmpty()) {
                        Log.d(TAG, "getMovies. Got empty data")
                    } else {
                        sharedPref.put(lastResponseTime, LocalTime.now().toString())
                        viewModelScope.launch(Dispatchers.IO) {
                            useCase.insertToDB(list)
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    errorLiveData.postValue(e.message)
                }

            })
    }

    /**
     * Download movies from network and post to LiveData
     */
    private fun getMoviesByPage(page: Int) {
        Log.d(TAG, "call getMoviesByPage. Page - $page, category - $category")
        useCase.fetchMovies(page, category)
            .subscribe(object : SingleObserver<List<MovieDomainModel>> {
                override fun onSubscribe(d: Disposable) {
                    TODO("Not yet implemented")
                }

                override fun onSuccess(list: List<MovieDomainModel>) {
                    if (list.isEmpty()) {
                        moviesLiveData.postValue(listOf())
                    } else {
                        moviesLiveData.postValue(list)
                    }
                }

                override fun onError(e: Throwable) {
                    errorLiveData.postValue(e.message)
                }
            })
    }


    /**
     * Return the LiveData of movies by using MovieDomainModel
     */
    fun getMoviesFromDB(): LiveData<List<MovieDomainModel>> {
        return Transformations.map(useCase.getMoviesFromDB()) {
            val moviesDomain = mutableListOf<MovieDomainModel>()
            it.forEach { k -> moviesDomain.add(k.toMovieDomainModel()) }
            return@map moviesDomain.toList()
        }
    }

    /**
     * Return the LiveData of favorite movies
     */
    fun getFavoriteMovies(): LiveData<List<MovieDomainModel>> {
        return Transformations.map(useCase.getFavoritesMoviesFromDB()) {
            val moviesDomain = mutableListOf<MovieDomainModel>()
            it.forEach { k -> moviesDomain.add(k.toMovieDomainModel()) }
            return@map moviesDomain.toList()
        }
    }

    /**
     * Return the LiveData of schedule movies
     */
    fun getScheduleMovies(): LiveData<List<MovieDomainModel>> {
        return Transformations.map(useCase.getScheduleMoviesFromDB()) {
            val moviesDomain = mutableListOf<MovieDomainModel>()
            it.forEach { k -> moviesDomain.add(k.toMovieDomainModel()) }
            return@map moviesDomain.toList()
        }
    }

    fun getFavoriteMoviesCount(): Int {
        var favoriteMovieRecords: Int
        runBlocking(Dispatchers.IO) {
            favoriteMovieRecords = useCase.getFavoriteMovieRecords()
        }
        return favoriteMovieRecords

    }

    fun onMovieSelected(movie: MovieDomainModel) {
        selectedMovieLiveData.postValue(movie)
    }

    fun moveToFavorite(id: Int) {
        runBlocking {
            launch(Dispatchers.IO) {
                useCase.updateFavoriteField(true, id)
            }
        }
    }

    fun unMoveToFavorite(id: Int) {
        runBlocking {
            launch(Dispatchers.IO) {
                useCase.updateFavoriteField(false, id)
            }
        }
    }

    fun updateScheduleTime(id: Int, time: String) {
        runBlocking {
            launch(Dispatchers.IO) {
                useCase.updateMovieScheduledTime(id, time)
            }
        }
    }


    /**
     * Save schedule information for Alarm canceling or other actions
     */
    fun saveScheduleInfo(title: String, requestCode: Int, time: Long) {
        val schedule = Schedule(title, requestCode, time.toString())
        runBlocking {
            launch(Dispatchers.IO) {
                useCase.saveScheduleInfoToDB(schedule)
            }
        }
    }

    /**
     * Return Request Code for Alarm Service (Pending Intent). Behavior related with stop alarming of notifications
     */
    fun getRequestCodeForAlarmService(title: String, time: String): Int {
        var requestCode: Int
        runBlocking(Dispatchers.IO) {
            val zdt = ZonedDateTime.parse(time).toInstant().toEpochMilli().toString()
            requestCode = useCase.getRequestCodeFromDB(title, zdt)
        }
        return requestCode
    }

    /**
     * Update field for schedule flag
     */
    fun updateScheduleFlag(id: Int, flag: Boolean) {
        runBlocking {
            launch(Dispatchers.IO) {
                useCase.updateScheduledField(id, flag)
            }
        }
    }

    /**
     * Delete movie from favorite list
     */
    fun deleteMovieFromFavoriteList(tile: String) {
        runBlocking {
            launch(Dispatchers.IO) {
                useCase.updateFavoriteFieldByTitle(false, tile)
            }
        }
    }

    /**
     * Add movie to favorite list
     */
    fun addMovieFromFavoriteList(tile: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.updateFavoriteFieldByTitle(true, tile)
        }
    }

//    fun getFavoriteMoviesCount(): Int {
//        var favoriteMovieRecords: Int
//        runBlocking(Dispatchers.IO) {
//            favoriteMovieRecords = useCase.getFavoriteMovieRecords()
//        }
//        return favoriteMovieRecords
//    }

}