package artsok.github.io.movie4k.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import artsok.github.io.movie4k.data.model.Schedule
import artsok.github.io.movie4k.data.repository.MovieRepositoryImpl
import artsok.github.io.movie4k.data.retrofit.MovieApi
import artsok.github.io.movie4k.data.room.AppDatabase
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.usecase.GetMoviesUseCase
import artsok.github.io.movie4k.extensions.get
import artsok.github.io.movie4k.extensions.put
import artsok.github.io.movie4k.lastResponseTime
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableSubscriber
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscription
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
    private val disposableBag = CompositeDisposable()
    private val moviesLiveData = MutableLiveData<List<MovieDomainModel>>()
    private val errorLiveData = MutableLiveData<String>()

    private val moviesLiveDataDB = MutableLiveData<List<MovieDomainModel>>()
    private val searchedMoviesLiveData = MutableLiveData<List<MovieDomainModel>>()
    private val favoritesMoviesLiveDataDB = MutableLiveData<List<MovieDomainModel>>()
    private val scheduleMoviesLiveDataDB = MutableLiveData<List<MovieDomainModel>>()
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

    val searchedMovies: LiveData<List<MovieDomainModel>>
        get() = searchedMoviesLiveData

    //TODO: rename
    private val moviesFrom: LiveData<List<MovieDomainModel>>
        get() = moviesLiveDataDB

    private val favoritesMoviesFromDB: LiveData<List<MovieDomainModel>>
        get() = favoritesMoviesLiveDataDB

    private val scheduleMoviesFromDB: LiveData<List<MovieDomainModel>>
        get() = scheduleMoviesLiveDataDB


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

        val interval = 1L //1 minutes
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
        useCase.getMovieRecords()
            .subscribe({ value -> records = value }, { records = 0 })
        return records == 0
    }

    /**
     * Read https://stackoverflow.com/questions/44582019/how-to-clear-livedata-stored-value and
     * use to Event wrapper preferred or SingleLiveEvent
     */
    fun resetLiveDataValue() {
        moviesLiveData.postValue(listOf())
    }

    /**
     *
     */
    fun resetSearchedLiveData() {
        searchedMoviesLiveData.postValue(listOf())
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
                    disposableBag.add(d)
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
                    disposableBag.add(d)
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
     * Return the LiveData of MovieDomainModel's movies
     */
    fun getMoviesFromDB(): LiveData<List<MovieDomainModel>> {
        val disposable = useCase.getMoviesFromDB()
            .subscribe(
                { value -> moviesLiveDataDB.postValue(value) },
                { error -> errorLiveData.postValue(error.message) })
        disposableBag.add(disposable)
        return moviesFrom
    }

    fun searchMovies(title: String): LiveData<List<MovieDomainModel>> {
        if (title.isNotEmpty()) {
            val searchedMoviesInDB = useCase.searchMoviesInDB(title)
                .doOnNext { Log.d(TAG, "Use local search in DB + ${LocalTime.now()}") }
            val searchedMoviesInNetwork = useCase.searchMoviesInNetwork(title)
                .doOnNext { Log.d(TAG, "Use network search  ${LocalTime.now()}") }
            val disposable = Flowable
                .merge(searchedMoviesInDB, searchedMoviesInNetwork)
                .filter { it.isNotEmpty() }
                .subscribe(
                    { value -> searchedMoviesLiveData.postValue(value) },
                    { error -> errorLiveData.postValue(error.message) })
            disposableBag.add(disposable)
            return searchedMovies
        } else {
            return getMoviesFromDB()
        }
    }


    /**
     * Return the LiveData of favorite movies
     */
    fun getFavoriteMoviesFromDB(): LiveData<List<MovieDomainModel>> {
        useCase.getFavoritesMoviesFromDB()
            .subscribe({ value -> favoritesMoviesLiveDataDB.postValue(value) },
                { error ->
                    Log.d(TAG, "Error during showing favorites movies ${error.message.toString()}")
                    favoritesMoviesLiveDataDB.postValue(listOf())
                })
        return favoritesMoviesFromDB
    }

    /**
     * Return the LiveData of schedule movies
     */
    fun getScheduleMovies(): LiveData<List<MovieDomainModel>> {
        useCase.getScheduleMoviesFromDB()
            .subscribe(object : FlowableSubscriber<List<MovieDomainModel>> {
                override fun onSubscribe(s: Subscription) {
                    Log.d(TAG, "getScheduleMovies - onSubscribe")
                }

                override fun onNext(value: List<MovieDomainModel>) {
                    scheduleMoviesLiveDataDB.postValue(value)
                }

                override fun onError(t: Throwable) {
                    Log.d(TAG, "getScheduleMovies - onError")
                    scheduleMoviesLiveDataDB.postValue(listOf())
                }

                override fun onComplete() {
                    Log.d(TAG, "getScheduleMovies - onComplete")
                }

            })
        return scheduleMoviesFromDB
    }

    /**
     * Return number of favorite movies.
     * From documentation: BlockingObservable is a variety of Observable that provides blocking operators.
     * It can be useful for testing and demo purposes, but is generally inappropriate for production applications (if you think you need to use a BlockingObservable this is usually a sign that you should rethink your design).
     */
    fun getFavoriteMoviesCount(): Int {
        var favoriteMovieRecords = 0
        useCase.getFavoriteMovieRecords().toObservable().blockingForEach { value ->
            Log.d(TAG, "Get count of favorite movies '$value'")
            favoriteMovieRecords = value
        }
        return favoriteMovieRecords
    }

    fun getFavoriteMoviesCountTest(): Single<Int> {
        return useCase.getFavoriteMovieRecords()
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
     * Return Request Code for Alarm Service (Pending Intent). Behavior related with stop alarm of notifications
     */
    fun getRequestCodeForAlarmService(title: String, time: String): Int {
        var requestCode = 0
        val zdt = ZonedDateTime.parse(time).toInstant().toEpochMilli().toString()
        useCase.getRequestCodeFromDB(title, zdt).subscribe(
            { code -> requestCode = code },
            { Log.d(TAG, "Can't get requestCode") })
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

    override fun onCleared() {
        super.onCleared()
        disposableBag.clear()
    }
}
