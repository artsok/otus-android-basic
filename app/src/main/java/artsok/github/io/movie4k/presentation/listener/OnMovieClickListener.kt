package artsok.github.io.movie4k.presentation.listener

import artsok.github.io.movie4k.domain.model.MovieDomainModel

interface OnMovieClickListener {
    fun onMovieTextClick(item: MovieDomainModel)
}