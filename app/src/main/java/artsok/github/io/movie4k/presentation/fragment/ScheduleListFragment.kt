package artsok.github.io.movie4k.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.recycler.ScheduleAdapter
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModel
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModelFactory
import com.google.android.material.snackbar.Snackbar

class ScheduleListFragment : Fragment() {

    private val fm: FragmentManager
        get() = activity!!.supportFragmentManager

    private lateinit var scheduleRecycler: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val movieViewModelFactory by lazy { MovieViewModelFactory(activity!!.application) }
    private val movieViewModel by lazy {
        ViewModelProvider(requireActivity(), movieViewModelFactory).get(
            MovieViewModel::class.java
        )
    }

    companion object {
        val TAG = ScheduleListFragment::class.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_schedule_list, container, false)
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

    private fun initViewModel() {
        movieViewModel.getScheduleMovies().observe(this.viewLifecycleOwner, Observer {
            scheduleAdapter.addScheduleMovies(it)
        })
    }

    private fun initFavoriteRecycler(view: View) {
        scheduleRecycler = view.findViewById(R.id.schedule_rc)
        scheduleRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        scheduleAdapter = ScheduleAdapter()

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
                val movie = scheduleAdapter.getItem(position)
                movieViewModel.updateScheduleFlag(movie.uniqueId, false)
                scheduleAdapter.removeItem(position)
                showShackBar(movie)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(scheduleRecycler)
        scheduleRecycler.adapter = scheduleAdapter
        //scheduleAdapter.registerAdapterDataObserver(Observer(scheduleRecycler))
    }


    private fun showShackBar(movie: MovieDomainModel) {
        val snackBar = Snackbar.make(requireView(), R.string.delete_message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }


    /**
     * Observer for favorite list layouts. When list is empty inflate another layout.
     * TODO: refactor - the same as in Favorite List Fragment
     */
    inner class Observer(private val recyclerView: RecyclerView) :
        RecyclerView.AdapterDataObserver() {

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