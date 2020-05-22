package artsok.github.io.movie4k.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import artsok.github.io.movie4k.DataStore.Companion.movies
import artsok.github.io.movie4k.Movie
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.listener.OnMovieClickListener
import artsok.github.io.movie4k.recycler.FavoriteAdapter


class FavoriteListFragment : Fragment() {

    private lateinit var favoriteRecycler: RecyclerView

    private var listener: OnMovieClickListener? = null

    companion object {
        const val TAG = "FavoriteListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFavoriteRecycler(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is OnMovieClickListener) {
            listener = activity as OnMovieClickListener
        }
    }

//    override fun onResume() {
//        super.onResume()
//        initFavoriteRecycler()
//    }

//    private fun personItemClicked(movie: Movie) {
//        val intent = Intent(this, MovieActivity::class.java)
//        with(intent) {
//            putExtra(MainActivityOLD.MARKER, movie)
//        }
//        startActivity(intent)
//    }

    private fun initFavoriteRecycler(view: View) {
        favoriteRecycler = view.findViewById(R.id.favorite_rc)
        favoriteRecycler.layoutManager =
            GridLayoutManager(requireContext(), GridLayoutManager.VERTICAL)

        val favoriteAdapter = FavoriteAdapter(
            favoriteRecycler.rootView as ViewGroup,
            requireContext(),
            movies
        ) {
            listener?.onMovieTextClick(it)
        }

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
                movies.filter { it.favorite }[position].favorite = false
                favoriteRecycler.adapter?.notifyItemRemoved(position)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(favoriteRecycler)
        favoriteRecycler.adapter = favoriteAdapter
        favoriteAdapter.registerAdapterDataObserver(EmptyObserver(favoriteRecycler))
    }

    inner class EmptyObserver(private val recyclerView: RecyclerView) : AdapterDataObserver() {

        init {
            isFavoriteRecyclerEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            isFavoriteRecyclerEmpty()
        }

        private fun isFavoriteRecyclerEmpty() {
            val emptyViewVisible = movies.count(Movie::favorite) == 0
            if (recyclerView.adapter != null && emptyViewVisible) {
                val layout = inflateEmptyView(recyclerView)
                layout.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
                recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
            }
        }

        private fun inflateEmptyView(view: View): View {
            return LayoutInflater.from(view.context).inflate(
                R.layout.empty_favorite_list, view.parent as ViewGroup
            )
        }
    }
}
