package artsok.github.io.movie4k.fragment

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.movie4k.DataStore
import artsok.github.io.movie4k.Movie
import artsok.github.io.movie4k.MovieAdapter
import artsok.github.io.movie4k.R

class MovieListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private var listener: OnMovieClickListener? = null

    interface OnMovieClickListener {
        fun onMovieTextClick(item: Movie)
    }

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
        setGridByOrientation(resources.configuration.orientation)
        recyclerView.adapter =
            MovieAdapter(requireContext(), DataStore.movies) {
                listener?.onMovieTextClick(it)
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
                    GridLayoutManager(requireContext(), landscapeTableSpan, GridLayoutManager.VERTICAL, false)

            }
            ORIENTATION_PORTRAIT -> {
                recyclerView.layoutManager =
                    GridLayoutManager(requireContext(), portraitTableSpan, GridLayoutManager.VERTICAL, false)
            }
        }
    }
}