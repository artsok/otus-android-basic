package artsok.github.io.movie4k.presentation.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.listener.OnMovieSelectedListener
import com.bumptech.glide.Glide
import java.util.*

class FavoriteAdapter(
    private val listener: OnMovieSelectedListener
) : RecyclerView.Adapter<ViewHolder>() {

    private val favorites = ArrayList<MovieDomainModel>()

    companion object {
        const val TAG = "FavoriteAdapter"
    }

    fun addFavoritesMovies(movies: List<MovieDomainModel>) {
        if (!favorites.containsAll(movies)) {
            favorites.addAll(movies)
        }
        val storeSize = this.favorites.size
        Log.d(TAG, "In addFavoritesMovies method. Number of size = $storeSize")
        notifyItemRangeInserted(storeSize, storeSize + movies.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_movie, parent, false)
        return MovieViewHolder(cardView)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = favorites[position]
        (holder as MovieViewHolder).itemTitle.text = movie.title
        Glide.with(holder.itemImage.context)
            .load("$path${movie.backdropPath}")
            .error(R.drawable.ic_error)
            .into(holder.itemImage)
        holder.itemTitle.setOnClickListener {
            listener.onMovieSelected(movie)
        }
    }
}