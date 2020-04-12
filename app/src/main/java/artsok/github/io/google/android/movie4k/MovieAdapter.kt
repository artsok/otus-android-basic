package artsok.github.io.google.android.movie4k

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(val context: Context, val movies: ArrayList<Movie>) :
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
        viewHolder.itemDescription.text = movies[position].description

    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var itemImage: ImageView = item.findViewById(R.id.image)
        var itemTitle: TextView = item.findViewById(R.id.title)
        var itemDescription: TextView = item.findViewById(R.id.description)
    }
}