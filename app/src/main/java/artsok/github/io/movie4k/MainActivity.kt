package artsok.github.io.movie4k

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import artsok.github.io.movie4k.dialog.CustomDialog
import artsok.github.io.movie4k.fragment.FavoriteListFragment
import artsok.github.io.movie4k.fragment.MovieListFragment
import artsok.github.io.movie4k.listener.OnMovieClickListener

class MainActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var themeButton: Button
    private lateinit var favoriteButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initClickListeners()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MovieListFragment(), MovieListFragment.TAG)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            val dialog = CustomDialog(this@MainActivity)
            dialog.show()
        }
    }

    override fun onMovieTextClick(item: Movie) {
        openMovie(item)
    }

    private fun initViews() {
        themeButton = findViewById(R.id.change_theme)
        favoriteButton = findViewById(R.id.favorite_list)
    }

    private fun openMovie(item: Movie) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, MovieFragment.newInstance(item), MovieFragment.TAG)
            .addToBackStack(null)
            .commit()
        item.selected = true
    }

    private fun openFavoriteList() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentByTag(FavoriteListFragment.TAG)
        if (fragment != null) {
            beginTransaction
                .replace(R.id.fragmentContainer, FavoriteListFragment(), FavoriteListFragment.TAG)
                .commit()
        } else {
            beginTransaction
                .replace(R.id.fragmentContainer, FavoriteListFragment(), FavoriteListFragment.TAG)
                .addToBackStack("FavoriteList")
                .commit()
        }
    }


    private fun initClickListeners() {
        themeButton.setOnClickListener { changeTheme() }
        favoriteButton.setOnClickListener {
            openFavoriteList()
        }
    }


    private fun changeTheme() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> setDefaultNightMode(MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO -> setDefaultNightMode(MODE_NIGHT_YES)
        }
    }

}