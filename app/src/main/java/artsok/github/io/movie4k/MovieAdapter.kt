package artsok.github.io.movie4k

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private val context: Context,
    private val movies: ArrayList<Movie>,
    private val itemClickListener: (Movie) -> Unit
) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_movie, parent, false)
        return ViewHolder(cardView)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemImage.setImageResource(movies[position].imageId)
        viewHolder.itemTitle.text = movies[position].title
        if (movies[position].selected) { //Refactor: use selected approach as discuss on the animation lecture
            viewHolder.itemTitle.setTextColor(getColor(context, R.color.selected))
        }
        viewHolder.itemTitle.setOnClickListener {
            itemClickListener(movies[position])
        }
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var itemImage: ImageView = item.findViewById(R.id.card_image)
        var itemTitle: TextView = item.findViewById(R.id.card_title)
    }
}