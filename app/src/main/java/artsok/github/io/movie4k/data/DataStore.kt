package artsok.github.io.movie4k.data

import artsok.github.io.movie4k.domain.model.MovieDomainModel

open class DataStore {
    companion object {
        val movies = mutableListOf<MovieDomainModel>()
    }
}