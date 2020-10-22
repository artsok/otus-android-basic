package artsok.github.io.movie4k.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.listener.OnMovieClickListener
import artsok.github.io.movie4k.presentation.listener.OnMovieSelectedListener
import artsok.github.io.movie4k.presentation.recycler.FavoriteAdapter
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModel
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModelFactory
import com.google.android.material.snackbar.Snackbar


class FavoriteListFragment : Fragment() {

    private lateinit var favoriteRecycler: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private var listener: OnMovieClickListener? = null

    private val movieViewModelFactory by lazy { MovieViewModelFactory(activity!!.application) }
    private val movieViewModel by lazy {
        ViewModelProvider(requireActivity(), movieViewModelFactory).get(
            MovieViewModel::class.java
        )
    }

    companion object {
        const val TAG = "FavoriteListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_favorite_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        initFavoriteRecycler(view)
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is OnMovieClickListener) {
            listener = activity as OnMovieClickListener
        }
    }


    private fun initViewModel() {
        movieViewModel.getFavoriteMovies().observe(this.viewLifecycleOwner, Observer {
            favoriteAdapter.addFavoritesMovies(it)
        })
    }

    private fun initFavoriteRecycler(view: View) {
        favoriteRecycler = view.findViewById(R.id.favorite_rc)
        favoriteRecycler.layoutManager =
            GridLayoutManager(requireContext(), GridLayoutManager.VERTICAL)
        favoriteAdapter = FavoriteAdapter(object : OnMovieSelectedListener {
            override fun onMovieSelected(movie: MovieDomainModel) {
                movieViewModel.onMovieSelected(movie)
                listener?.onMovieTextClick()
            }
        })

        val itemTouchHelper = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                Log.d(TAG, "Position for deleting $position")
                val movie = favoriteAdapter.getItem(position)
                movieViewModel.deleteMovieFromFavoriteList(movie.title)
                favoriteAdapter.removeItem(position)
                showShackBar(movie)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(favoriteRecycler)
        favoriteRecycler.adapter = favoriteAdapter
        favoriteAdapter.registerAdapterDataObserver(Observer(favoriteRecycler))
    }

    private fun showShackBar(movie: MovieDomainModel) {
        val snackBar = Snackbar.make(requireView(), R.string.delete_message, Snackbar.LENGTH_LONG)
        snackBar.setAction(R.string.revert_delete_message) {
            movieViewModel.addMovieFromFavoriteList(movie.title)
        }
        snackBar.show()
    }


    /**
     * Observer for favorite list layouts. When list is empty inflate another layout
     */
    inner class Observer(private val recyclerView: RecyclerView) : AdapterDataObserver() {

        init {
            isFavoriteRecyclerEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            isFavoriteRecyclerEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            isFavoriteRecyclerNotEmpty()
        }

        private fun isFavoriteRecyclerEmpty() {
            val emptyViewVisible = movieViewModel.getFavoriteMoviesCount() == 0
            if (recyclerView.adapter != null && emptyViewVisible) {
                val layout = inflateEmptyView(recyclerView)
                layout.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
                recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
            }
        }

        private fun isFavoriteRecyclerNotEmpty() {
            val emptyViewVisible = movieViewModel.getFavoriteMoviesCount() > 0
            if (recyclerView.adapter != null && emptyViewVisible) {
                recyclerView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            }
        }

        private fun inflateEmptyView(view: View): View {
            return LayoutInflater.from(view.context).inflate(
                R.layout.empty_favorite_list, view.parent as ViewGroup
            )
        }
    }
}
