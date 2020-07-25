package artsok.github.io.movie4k.domain.usecase

import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.repository.MovieRepository
import java.io.IOException

/**
 *
 */
class GetMoviesUseCase(private val repository: MovieRepository) {

    sealed class Result {
        data class Success(val data: List<MovieDomainModel>) : Result()
        data class Error(val e: Throwable) : Result()
    }

    suspend fun fetchPopularMovies(page: Int): Result {
        return try {
            Result.Success(repository.getMovies(page)
                .filter { it.backdropPath.isNotBlank() && it.posterPath.isNotBlank() }
            )
        } catch (e: IOException) {
            Result.Error(e)
        }
    }
}
