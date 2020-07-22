package artsok.github.io.movie4k.domain.usecase

import artsok.github.io.movie4k.data.DataStore
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import java.io.IOException

class GetFavoritesMoviesUseCase {

    sealed class Result {
        data class Success(val data: List<MovieDomainModel>) : Result()
        data class Error(val e: Throwable) : Result()
    }

    fun fetchFavoritesMovies(): Result {
        return try {
            Result.Success(
                DataStore.movies.filter { it.favorite }
            )
        } catch (e: IOException) {
            Result.Error(e)
        }
    }
}