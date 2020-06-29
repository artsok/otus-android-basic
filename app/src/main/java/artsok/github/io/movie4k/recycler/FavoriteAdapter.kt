package artsok.github.io.movie4k.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.data.Movie
import com.bumptech.glide.Glide

class FavoriteAdapter(
    private val parent: ViewGroup,
    private val context: Context,
    private val movies: List<Movie>,
    private val itemClickListener: (Movie) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_movie, parent, false)
        return MovieViewHolder(cardView)
    }

    override fun getItemCount(): Int = movies.count { it.favorite }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies.filter { it.favorite }[position]
        (holder as MovieViewHolder).itemTitle.text = movie.title
        Glide.with(holder.itemImage.context)
            .load("$path${movie.backdropPath}")
            .error(R.drawable.ic_error)
            .into(holder.itemImage)
        holder.itemTitle.setOnClickListener {
            itemClickListener(movie)
        }
    }
}