package artsok.github.io.movie4k.domain.usecase

//class GetFavoritesMoviesUseCase {
//
//    sealed class Result {
//        data class Success(val data: List<MovieDomainModel>) : Result()
//        data class Error(val e: Throwable) : Result()
//    }
//
//    fun fetchFavoritesMovies(): Result {
//        return try {
//            Result.Success(
//                DataStore.movies.filter { it.favorite }
//            )
//        } catch (e: IOException) {
//            Result.Error(e)
//        }
//    }
//
//
//}