package artsok.github.io.movie4k.fragment

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.data.DataStore
import artsok.github.io.movie4k.data.Movie
import artsok.github.io.movie4k.listener.OnMovieClickListener
import artsok.github.io.movie4k.network.MovieApi
import artsok.github.io.movie4k.network.PopularFilms
import artsok.github.io.movie4k.recycler.MovieAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var loadProgress: ProgressBar

    private var page = 0
    private var listener: OnMovieClickListener? = null

    companion object {
        const val PAGE = "pageNumber"
        const val TAG = "MovieListFragment"
        const val visibleThreshold = 10
        const val defaultPage = 1
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        outState.putInt(PAGE, this.page)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        if (savedInstanceState?.getInt(PAGE) != null) {
            this.page = savedInstanceState.getInt(PAGE)
        }
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
        loadProgress = view.findViewById(R.id.progressBar)
        setGridByOrientation(resources.configuration.orientation)
        fetchPopularMoviesByPage(defaultPage)

        recyclerView.adapter =
            MovieAdapter(
                requireContext(),
                DataStore.movies
            ) {
                listener?.onMovieTextClick(it)
            }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = gridLayoutManager.itemCount
                val lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition()
                if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    Log.d(
                        TAG,
                        "Подгрузить еще данные ${gridLayoutManager.findLastVisibleItemPosition()}"
                    )
                    fetchPopularMoviesByPage(this@MovieListFragment.page, false)
                }
            }
        })
        initSwipeRefreshListener()
    }

    private fun fetchPopularMoviesByPage(page: Int, isLoadProgress: Boolean = true) {
        if (DataStore.movies.isEmpty() && isLoadProgress) {
            loadProgress.visibility = View.VISIBLE
        }
        MovieApi.movieService.getPopularFilmsByPage(page)
            .enqueue(object : Callback<PopularFilms> {
                override fun onFailure(call: Call<PopularFilms>, t: Throwable) {
                    Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<PopularFilms>,
                    response: Response<PopularFilms>
                ) {
                    if (response.isSuccessful) {
                        val dtos = response.body()!!.results
                        val storeSize = DataStore.movies.size
                        DataStore.movies.addAll(dtos.map(::Movie))
                        recyclerView.adapter?.notifyItemRangeInserted(
                            storeSize,
                            storeSize + dtos.size
                        )
                        loadProgress.visibility = GONE
                    }
                }
            })

        this.page = page.inc()
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
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_purple
        )
        swipeRefreshLayout.setOnRefreshListener {
            updateMovies()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun updateMovies() {
        val handle = Handler()
        val task = Runnable {
            DataStore.movies.clear()
            recyclerView.adapter?.notifyDataSetChanged()
            fetchPopularMoviesByPage(defaultPage, false)
        }
        handle.post(task)
    }
}