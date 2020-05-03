package artsok.github.io.google.android.movie4k

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.google.android.movie4k.DataStore.Companion.movies

const val MARKER = "movies"
const val TAG = "MyTag"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        setGridByOrientation(resources.configuration.orientation)
        recyclerView.adapter = MovieAdapter(this, movies) { movie -> personItemClicked(movie) }
    }

    override fun onRestart() {
        super.onRestart()
        recyclerView.adapter = MovieAdapter(this, movies) { movie -> personItemClicked(movie) }
    }

    private fun setGridByOrientation(orientation: Int) {
        when (orientation) {
            ORIENTATION_LANDSCAPE -> {
                recyclerView.layoutManager =
                    GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)

            }
            ORIENTATION_PORTRAIT -> {
                recyclerView.layoutManager =
                    GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            }
        }
    }


    private fun personItemClicked(movie: Movie) {
        val intent = Intent(this@MainActivity, MovieActivity::class.java)
        movie.selected = true
        with(intent) {
            putExtra(MARKER, movie)
        }
        startActivity(intent)
    }
}
