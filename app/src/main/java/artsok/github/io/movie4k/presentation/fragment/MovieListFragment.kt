package artsok.github.io.movie4k.presentation.fragment

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.data.DataStore
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.listener.OnMovieClickListener
import artsok.github.io.movie4k.presentation.recycler.MovieAdapter
import artsok.github.io.movie4k.presentation.viewmodel.MovieListViewModel

class MovieListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var loadProgress: ProgressBar
    private lateinit var movieListViewModel: MovieListViewModel
    //private val movieListViewModel by lazy { ViewModelProvider(this).get(MovieListViewModel::class.java) }


    private var page = 1
    private var listener: OnMovieClickListener? = null

    companion object {
        const val PAGE = "pageNumber"
        const val TAG = "MovieListFragment"
        const val visibleThreshold = 10
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
        initSwipeRefreshLayout()
        initLoadProgress()
        initRecycler()
        setGridByOrientation(resources.configuration.orientation)

        movieListViewModel = ViewModelProvider(this).get(MovieListViewModel::class.java)
        movieListViewModel.movies.observe(
            this.viewLifecycleOwner,
            Observer<List<MovieDomainModel>> {
                val storeSize = DataStore.movies.size
                DataStore.movies.addAll(it)
                recyclerView.adapter?.notifyItemRangeInserted(
                    storeSize, storeSize + it.size
                )
            })
        movieListViewModel.error.observe(
            this.viewLifecycleOwner,
            Observer<String> { error -> Toast.makeText(context, error, Toast.LENGTH_LONG).show() })
//
//        movieListViewModel.getMoviesByPage(1)

        fetchPopularMoviesByPage()

        initSwipeRefreshListener()
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = view!!.findViewById((R.id.swipeRefreshLayout))
    }

    private fun initLoadProgress() {
        loadProgress = view!!.findViewById(R.id.progressBar)
    }

    private fun initRecycler() {
        recyclerView = view!!.findViewById(R.id.recyclerViewFragment)
        recyclerView.adapter =
            MovieAdapter(
                requireContext(), DataStore.movies
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
                    fetchPopularMoviesByPage(page)
                }
            }
        })
    }

    private fun fetchPopularMoviesByPage(page: Int = 1, isLoadProgress: Boolean = true) {
        if (DataStore.movies.isEmpty() && isLoadProgress) {
            loadProgress.visibility = View.VISIBLE
        }
        movieListViewModel.getMoviesByPage(page)
        loadProgress.visibility = GONE
        this.page = page.inc()
    }


//    private fun fetchPopularMoviesByPage(page: Int = 1, isLoadProgress: Boolean = true) {
//        if (DataStore.movies.isEmpty() && isLoadProgress) {
//            loadProgress.visibility = View.VISIBLE
//        }
//        MovieApi.movieService.getPopularFilmsByPage(page)
//            .enqueue(object : Callback<MovieListDataModel> {
//                override fun onFailure(call: Call<MovieListDataModel>, t: Throwable) {
//                    Toast.makeText(requireContext(), "${t.message}", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onResponse(
//                    call: Call<MovieListDataModel>,
//                    response: Response<MovieListDataModel>
//                ) {
//                    if (response.isSuccessful) {
//                        val dtos = response.body()!!.results
//                        val storeSize = DataStore.movies.size
//                        DataStore.movies.addAll(dtos.map { MovieDomainModel(it.id, it.overview, it.backdropPath, it.title, it.backdropPath)})
//                        recyclerView.adapter?.notifyItemRangeInserted(
//                            storeSize,
//                            storeSize + dtos.size
//                        )
//                        loadProgress.visibility = GONE
//                    }
//                }
//            })
//
//        this.page = page.inc()
//    }

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
            fetchPopularMoviesByPage( isLoadProgress = false)
        }
        handle.post(task)
    }
}