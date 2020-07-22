package artsok.github.io.movie4k.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.fragment.app.Fragment
import artsok.github.io.movie4k.MovieFragment
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.presentation.dialog.CustomDialog
import artsok.github.io.movie4k.presentation.fragment.FavoriteListFragment
import artsok.github.io.movie4k.presentation.fragment.MovieListFragment
import artsok.github.io.movie4k.presentation.listener.OnMovieClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), OnMovieClickListener {

    private lateinit var bottomNavigation: BottomNavigationView

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MovieListFragment(), MovieListFragment.TAG)
                .commit()
        }
        initViews()
        initClickListener()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            val dialog = CustomDialog(this@MainActivity)
            dialog.show()
        }
    }

    override fun onMovieTextClick() {
        openMovie()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    private fun initViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }

    private fun openMovie() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                MovieFragment(),
                MovieFragment.TAG
            )
            .addToBackStack(null)
            .commit()
    }

    private fun openFavoriteList() {
        val favoriteList: FavoriteListFragment? =
            supportFragmentManager.findFragmentByTag(FavoriteListFragment.TAG) as FavoriteListFragment?
        if (favoriteList == null || !favoriteList.isAdded) {
            commitFragment(FavoriteListFragment(), FavoriteListFragment.TAG)
        }
    }

    private fun commitFragment(fragment: Fragment, tag: String, backStackName: String? = null) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction
            .replace(R.id.fragmentContainer, fragment, tag)
            .addToBackStack(backStackName)
            .commit()
    }

    private fun initClickListener() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottomNavigationMain -> {
                    val fragment = MovieListFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment, MovieListFragment.TAG)
                        .commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomNavigationFavorite -> {
                    openFavoriteList()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomNavigationTheme -> {
                    changeTheme()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun changeTheme() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> setDefaultNightMode(MODE_NIGHT_NO)
            Configuration.UI_MODE_NIGHT_NO -> setDefaultNightMode(MODE_NIGHT_YES)
        }
    }
}