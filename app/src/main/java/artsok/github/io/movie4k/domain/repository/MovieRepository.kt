package artsok.github.io.movie4k.domain.repository

import artsok.github.io.movie4k.domain.model.MovieDomainModel

interface MovieRepository {
    suspend fun getLandingMovies(): List<MovieDomainModel>
    suspend fun getMovies(page: Int): List<MovieDomainModel>
}
