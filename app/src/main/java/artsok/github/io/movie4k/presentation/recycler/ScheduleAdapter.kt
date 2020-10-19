package artsok.github.io.movie4k.presentation.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.listener.OnScheduledListener
import java.util.*

class ScheduleAdapter(
    private val listener: OnScheduledListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val scheduleNotificationList = ArrayList<MovieDomainModel>()

    companion object {
        val TAG = ScheduleAdapter::class.toString()
    }


    fun addScheduleMovies(movies: List<MovieDomainModel>) {
        movies.forEach {
            scheduleNotificationList.add(it)
        }
        val size = this.scheduleNotificationList.size
        Log.d(TAG, "In addScheduleMovies method. Number of size = $size")
        notifyItemRangeInserted(size, size + movies.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.schedule_movie, parent, false)
        return ScheduleViewHolder(cardView)
    }

    override fun getItemCount() = scheduleNotificationList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = scheduleNotificationList[position]
        (holder as ScheduleViewHolder).title.text = movie.title
        holder.time.text = movie.scheduledTime
        holder.editButton.setOnClickListener {
            listener.onEditButtonClick(movie)
        }
    }

    /**
     * Removed item from scheduled list
     */
    internal fun removeItem(position: Int) {
        this.scheduleNotificationList.removeAt(position)
        Log.d(TAG, "Remove element from list")
        notifyItemRemoved(position)
        Log.d(TAG, "Updated items on adapter")
    }

    internal fun removeMovie(movie: MovieDomainModel) {
        val position = this.scheduleNotificationList.indexOf(movie)
        this.scheduleNotificationList.remove(movie)
        notifyItemRemoved(position)
    }

    internal fun getItem(position: Int): MovieDomainModel {
        return this.scheduleNotificationList[position]
    }
}