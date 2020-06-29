package artsok.github.io.movie4k.listener

import artsok.github.io.movie4k.data.Movie

interface OnMovieClickListener {
    fun onMovieTextClick(item: Movie)
}