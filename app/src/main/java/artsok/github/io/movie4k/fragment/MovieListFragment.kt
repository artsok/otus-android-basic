package artsok.github.io.movie4k.fragment

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.data.DataStore
import artsok.github.io.movie4k.listener.OnMovieClickListener
import artsok.github.io.movie4k.recycler.MovieAdapter

class MovieListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var listener: OnMovieClickListener? = null

    companion object {
        const val TAG = "MovieListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewFragment)
        swipeRefreshLayout = view.findViewById((R.id.swipeRefreshLayout))
        setGridByOrientation(resources.configuration.orientation)
        recyclerView.adapter =
            MovieAdapter(
                requireContext(),
                DataStore.movies
            ) {
                listener?.onMovieTextClick(it)
            }
        initSwipeRefreshListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is OnMovieClickListener) {
            listener = activity as OnMovieClickListener
        }
    }

    private fun setGridByOrientation(orientation: Int) {
        val landscapeTableSpan = 3
        val portraitTableSpan = 2
        when (orientation) {
            ORIENTATION_LANDSCAPE -> {
                recyclerView.layoutManager =
                    GridLayoutManager(
                        requireContext(),
                        landscapeTableSpan,
                        GridLayoutManager.VERTICAL,
                        false
                    )

            }
            ORIENTATION_PORTRAIT -> {
                recyclerView.layoutManager =
                    GridLayoutManager(
                        requireContext(),
                        portraitTableSpan,
                        GridLayoutManager.VERTICAL,
                        false
                    )
            }
        }
    }

    private fun initSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener {
            fetchMovies()
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
}