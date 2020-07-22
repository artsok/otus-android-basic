package artsok.github.io.movie4k.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import artsok.github.io.movie4k.data.DataStore
import artsok.github.io.movie4k.data.repository.MovieRepositoryImpl
import artsok.github.io.movie4k.data.retrofit.MovieApi
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private var page = 1
    private val moviesLiveData = MutableLiveData<List<MovieDomainModel>>()
    private val errorLiveData = MutableLiveData<String>()
    private val selectedMovieLiveData = MutableLiveData<MovieDomainModel>()
    private val useCase = GetMoviesUseCase(MovieRepositoryImpl(MovieApi.movieService))

    //Прокидываем LiveData наружу во MovieListFragment
    val movies: LiveData<List<MovieDomainModel>>
        get() = moviesLiveData

    val error: LiveData<String>
        get() = errorLiveData

    val selectedMovie: LiveData<MovieDomainModel>
        get() = selectedMovieLiveData

    fun clearAndInitData() {
        page = 1
        DataStore.movies.clear()
    }

    fun isDataStoreEmpty() : Boolean {
        return DataStore.movies.isEmpty()
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

    fun getMoviesByPage() {
        viewModelScope.launch {
            useCase.fetchPopularMovies(page).also { result ->
                val action = when (result) {
                    is GetMoviesUseCase.Result.Success ->
                        if (result.data.isEmpty()) {
                            moviesLiveData.postValue(listOf())
                        } else {
                            moviesLiveData.postValue(result.data)
                        }
                    is GetMoviesUseCase.Result.Error ->
                        errorLiveData.postValue(result.e.message)
                }
            }
        }
        this.page = page.inc()
    }

    fun onMovieSelected(movie: MovieDomainModel) {
        selectedMovieLiveData.postValue(movie)
    }
}