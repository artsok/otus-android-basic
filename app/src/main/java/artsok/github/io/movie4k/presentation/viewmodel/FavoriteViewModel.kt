package artsok.github.io.movie4k.presentation.viewmodel

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import artsok.github.io.movie4k.domain.model.MovieDomainModel
//import artsok.github.io.movie4k.domain.usecase.GetFavoritesMoviesUseCase
//import artsok.github.io.movie4k.domain.usecase.GetFavoritesMoviesUseCase.Result.Error
//import artsok.github.io.movie4k.domain.usecase.GetFavoritesMoviesUseCase.Result.Success
//import artsok.github.io.movie4k.domain.usecase.GetMoviesUseCase
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch

//class FavoriteViewModel : ViewModel() {
//
//    private val favoriteLiveData = MutableLiveData<List<MovieDomainModel>>()
//    private val useCase = GetMoviesUseCase
//
//    val favorites: LiveData<List<MovieDomainModel>>
//        get() = favoriteLiveData
//
//    fun resetLiveDataValue() {
//        favoriteLiveData.postValue(listOf())
//    }
//
//    fun getFavoriteMovies() {
//        viewModelScope.launch(Dispatchers.IO) {
//            useCase.fetchFavoritesMovies().also { result ->
//                when (result) {
//                    is Success ->
//                        favoriteLiveData.postValue(result.data)
//                    is Error ->
//                        favoriteLiveData.postValue(listOf())
//                }
//            }
//        }
//    }
//}