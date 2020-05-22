package artsok.github.io.movie4k.listener

import artsok.github.io.movie4k.Movie

interface OnMovieClickListener {
    fun onMovieTextClick(item: Movie)
}