package artsok.github.io.movie4k.presentation.fragment

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.fragment.MovieListFragment.FetchDataFlow.*
import artsok.github.io.movie4k.presentation.listener.OnMovieClickListener
import artsok.github.io.movie4k.presentation.listener.OnMovieSelectedListener
import artsok.github.io.movie4k.presentation.recycler.MovieAdapter
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModel
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MovieListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var loadProgress: ProgressBar

    private var adapter: MovieAdapter? = null
    private var listener: OnMovieClickListener? = null
    private var queryTextChangedJob: Job? = null

    private val movieViewModelFactory by lazy { MovieViewModelFactory(activity!!.application) }
    private val movieViewModel by lazy {
        ViewModelProvider(requireActivity(), movieViewModelFactory).get(
            MovieViewModel::class.java
        )
    }

    companion object {
        const val TAG = "MovieListFragment"
        const val visibleThreshold = 2
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        initSwipeRefreshLayout()
        initLoadProgress()
        initRecycler()
        setGridByOrientation(resources.configuration.orientation)
        fetchData(state = INIT)
        initViewModel()
        initSwipeRefreshListener()


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                movieViewModel.resetSearchedLiveData()
                queryTextChangedJob?.cancel()
                queryTextChangedJob =
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        delay(1000)
                        searchMovies(newText)
                    }
                return false
            }
        })
    }


    private fun searchMovies(title: String) {
        movieViewModel.searchMovies(title)
            .observe(
                this.viewLifecycleOwner,
                { value ->
                    value?.let {
                        adapter!!.addSearchedValue(it)
                        adapter?.filter?.filter(title)
                    }
                })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        movieViewModel.resetLiveDataValue()
        movieViewModel.resetSearchedLiveData()
        movieViewModel.onErrorDisplayed()
    }

    private fun initViewModel() {
        movieViewModel.getMoviesFromDB().observe(
            this.viewLifecycleOwner,
            {
                adapter!!.addMovies(it)
                loadProgress.visibility = View.GONE
            })

        movieViewModel.error.observe(
            this.viewLifecycleOwner,
            { error ->
                if (!error.isNullOrBlank()) {
                    showShackBar(error)
                }
                loadProgress.visibility = View.GONE
            })
    }

    private fun showShackBar(error: String) {
        val snackBar = Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG)
        snackBar.setAction(R.string.repeat_message) {
            fetchData(state = START)
        }
        snackBar.show()
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = view!!.findViewById((R.id.swipeRefreshLayout))
    }

    private fun initLoadProgress() {
        loadProgress = view!!.findViewById(R.id.progressBar)
    }

    private fun initRecycler() {
        recyclerView = view!!.findViewById(R.id.recyclerViewFragment)
        adapter = MovieAdapter(
            requireContext(), object : OnMovieSelectedListener {
                override fun onMovieSelected(movie: MovieDomainModel) {
                    movieViewModel.onMovieSelected(movie)
                    listener?.onMovieTextClick()
                }
            })
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = gridLayoutManager.itemCount
                val lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition()
                Log.d(
                    TAG,
                    "totalItemCount = $totalItemCount, lastVisibleItemPosition = $lastVisibleItemPosition"
                )

                if (totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                    Log.d(
                        TAG,
                        "Подгрузить еще данные ${gridLayoutManager.findLastVisibleItemPosition()}"
                    )
                    //TODO: вернуть обратно, так как отключил чтобы проверить поиск
                    //fetchData(state = CONTINUE)
                }
            }
        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                searchView.clearFocus()
            }
        })
    }

    private fun fetchData(isLoadProgress: Boolean = true, state: FetchDataFlow, page: Int = 1) {
        val isEmpty = movieViewModel.isDbEmpty()
        when (state) {
            INIT -> {
                if (isEmpty && isLoadProgress) {
                    Log.d(TAG, "fetchData. Init State $isEmpty && $isLoadProgress")
                    loadProgress.visibility = View.VISIBLE
                    movieViewModel.getMovies()
                } else {
                    Log.d(TAG, "fetchData. Init State")
                }
            }
            START -> {
                Log.d(TAG, "fetchData. START State")
                movieViewModel.getMovies()
            }
            CONTINUE -> {
                Log.d(TAG, "fetchData. CONTINUE State")
                movieViewModel.getMovies()
            }
        }
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

    /**
     * Pull down to update movies. Refresh all DB if time count more than 20 min
     */
    private fun updateMovies() {
        Log.d(TAG, "updateMovies")
        val handle = Handler()
        val task = Runnable {
            movieViewModel.clearAndInitData()
            recyclerView.adapter?.notifyDataSetChanged()
        }
        handle.post(task)
    }

    enum class FetchDataFlow {
        INIT, CONTINUE, START
    }
}


