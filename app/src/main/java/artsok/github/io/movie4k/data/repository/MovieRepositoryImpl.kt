package artsok.github.io.movie4k.data.repository

import artsok.github.io.movie4k.data.model.toDomainModel
import artsok.github.io.movie4k.data.retrofit.MovieApiService
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.domain.repository.MovieRepository

const val INIT_PAGE = 1

class MovieRepositoryImpl(
    private val retrofitService: MovieApiService
) : MovieRepository {

    override suspend fun getLandingMovies(): List<MovieDomainModel> =
        retrofitService.getPopularFilmsByPage(INIT_PAGE).results.map { it.toDomainModel() }

    override suspend fun getMovies(page: Int): List<MovieDomainModel> =
        retrofitService.getPopularFilmsByPage(page).results.map { it.toDomainModel() }
}
