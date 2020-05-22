package artsok.github.io.movie4k

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import artsok.github.io.movie4k.fragment.MovieListFragment

class MainActivity : AppCompatActivity(), MovieListFragment.OnMovieClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MovieListFragment(), MovieListFragment.TAG)
            .commit()
    }

    override fun onMovieTextClick(item: Movie) {
        openMovie(item)
    }

    private fun openMovie(item: Movie) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MovieFragment.newInstance(item), MovieFragment.TAG)
            .addToBackStack(null)
            .commit()
        item.selected = true
    }

}