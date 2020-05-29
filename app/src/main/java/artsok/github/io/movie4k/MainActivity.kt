package artsok.github.io.movie4k

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import artsok.github.io.movie4k.DataStore.Companion.movies

class MainActivity : AppCompatActivity() {

    companion object {
        const val MARKER = "movies"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var themeButton: Button
    private lateinit var favoriteButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initClickListeners()
        setGridByOrientation(resources.configuration.orientation)
        recyclerView.adapter = MovieAdapter(this, movies, this@MainActivity::personItemClicked)
        swipeRefreshLayout.setOnRefreshListener {
            fetchMovies()
        }
    }

    override fun onRestart() {
        super.onRestart()
        recyclerView.adapter = MovieAdapter(this, movies, this@MainActivity::personItemClicked)
    }

    override fun onBackPressed() {
        val dialog = CustomDialog(this@MainActivity)
        dialog.show()
    }

    private fun initViews() {
        themeButton = findViewById(R.id.change_theme)
        favoriteButton = findViewById(R.id.favorite_list)
        recyclerView = findViewById(R.id.recyclerView)
        swipeRefreshLayout = findViewById((R.id.swipeRefreshLayout))
    }

    private fun initClickListeners() {
        themeButton.setOnClickListener { changeTheme() }
        favoriteButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
        }
    }

    /*
        Test stub
     */
    private fun fetchMovies() {
        val handle = Handler()
        handle.postDelayed({
            if (swipeRefreshLayout.isRefreshing) {
                swipeRefreshLayout.isRefreshing = false
            }
        }, 1000)
    }

    private fun setGridByOrientation(orientation: Int) {
        val landscapeTableSpan = 3
        val portraitTableSpan = 2
        when (orientation) {
            ORIENTATION_LANDSCAPE -> {
                recyclerView.layoutManager =
                    GridLayoutManager(this, landscapeTableSpan, GridLayoutManager.VERTICAL, false)

            }
            ORIENTATION_PORTRAIT -> {
                recyclerView.layoutManager =
                    GridLayoutManager(this, portraitTableSpan, GridLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun changeTheme() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> setDefaultNightMode(MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO -> setDefaultNightMode(MODE_NIGHT_YES)
        }
    }

    private fun personItemClicked(movie: Movie) {
        movie.selected = true
        val intent = Intent(this@MainActivity, MovieActivity::class.java)
        with(intent) {
            putExtra(MARKER, movie)
        }
        startActivity(intent)
    }
}
