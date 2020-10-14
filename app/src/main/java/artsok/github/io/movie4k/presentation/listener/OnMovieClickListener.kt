package artsok.github.io.movie4k.presentation.listener

import artsok.github.io.movie4k.domain.model.MovieDomainModel

interface OnMovieClickListener {
    fun onMovieTextClick()
}

interface OnMovieSelectedListener {
    fun onMovieSelected(movie: MovieDomainModel)
}

interface OnScheduleListener {
    fun onEditButtonClick()
}