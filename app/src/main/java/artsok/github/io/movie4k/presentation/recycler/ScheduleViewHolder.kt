package artsok.github.io.movie4k.presentation.recycler

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.movie4k.R

class ScheduleViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val title: TextView = item.findViewById(R.id.movie_title)
    val time: TextView = item.findViewById(R.id.schedule_time)
    val editButton: ImageButton = item.findViewById(R.id.edit_image)
}