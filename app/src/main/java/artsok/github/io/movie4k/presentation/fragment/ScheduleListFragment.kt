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
import artsok.github.io.movie4k.presentation.DateTimePickerUtil
import artsok.github.io.movie4k.presentation.listener.OnScheduledListener
import artsok.github.io.movie4k.presentation.recycler.ScheduleAdapter
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModel
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModelFactory
import artsok.github.io.movie4k.service.AlarmService
import com.google.android.material.snackbar.Snackbar


class ScheduleListFragment : Fragment(), DateTimePickerUtil {

    private lateinit var alarmService: AlarmService
    private lateinit var scheduleRecycler: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val movieViewModelFactory by lazy { MovieViewModelFactory(activity!!.application) }
    private val movieViewModel by lazy {
        ViewModelProvider(requireActivity(), movieViewModelFactory).get(
            MovieViewModel::class.java
        )
    }
    private val fm: FragmentManager
        get() = activity!!.supportFragmentManager

    private fun initAlarmService() {
        alarmService = AlarmService(context!!)
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
        initAlarmService()
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

        scheduleAdapter = ScheduleAdapter(object : OnScheduledListener {
            override fun onEditButtonClick(movie: MovieDomainModel) {
                val stopRequestCode =
                    movieViewModel.getRequestCodeForAlarmService(movie.title, movie.scheduledTime)
                alarmService.stopAlarms(stopRequestCode)
                movieViewModel.updateScheduleFlag(movie.uniqueId, false)
                scheduleAdapter.removeMovie(movie)
                clickScheduleMovieAlarm(fm, movie, movieViewModel, alarmService)
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
                val movie = scheduleAdapter.getItem(position)
                movieViewModel.updateScheduleFlag(movie.uniqueId, false)
                scheduleAdapter.removeItem(position)
                showShackBar(movie)
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(scheduleRecycler)
        scheduleRecycler.adapter = scheduleAdapter
    }


    private fun showShackBar(movie: MovieDomainModel) {
        val snackBar = Snackbar.make(requireView(), R.string.delete_message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }
}