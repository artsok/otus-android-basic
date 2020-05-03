package artsok.github.io.movie4k

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.movie4k.DataStore.Companion.movies

class MainActivity : AppCompatActivity() {

    companion object {
        const val MARKER = "movies"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var themeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        themeButton = findViewById(R.id.change_theme)
        recyclerView = findViewById(R.id.recyclerView)
        setGridByOrientation(resources.configuration.orientation)
        recyclerView.adapter = MovieAdapter(this, movies) { movie -> personItemClicked(movie) }
        themeButton.setOnClickListener { changeTheme() }
    }

    override fun onRestart() {
        super.onRestart()
        recyclerView.adapter = MovieAdapter(this, movies) { movie -> personItemClicked(movie) }
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

    override fun onBackPressed() {
        val dialog = CustomDialog(this@MainActivity)
        dialog.show()
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
