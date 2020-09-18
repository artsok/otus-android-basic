package artsok.github.io.movie4k.presentation.recycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.listener.OnMovieSelectedListener
import com.bumptech.glide.Glide

const val path = "https://image.tmdb.org/t/p/w500"

class MovieAdapter(
    private val context: Context,
    private val listener: OnMovieSelectedListener
) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    companion object {
        const val TAG = "MovieAdapter"
    }

    //private val movies = DataStore.movies
    private var movies = emptyList<MovieDomainModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_movie, parent, false)
        return ViewHolder(cardView)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemTitle.text = movies[position].title
        if (movies[position].selected) { //Refactor: use selected approach as discuss on the animation lecture
            viewHolder.itemTitle.setTextColor(
                getColor(
                    context,
                    R.color.selected
                )
            )
        }

        viewHolder.itemTitle.setOnClickListener {
            listener.onMovieSelected(movies[position])
        }
        Glide.with(viewHolder.itemImage.context)
            .load("$path${movies[position].backdropPath}")
            .error(R.drawable.ic_error)
            .into(viewHolder.itemImage)
    }

    fun addMovies(movies: List<MovieDomainModel>) {
        //DataStore.movies.addAll(movies)
        this.movies = movies
        //val storeSize = this.movies.size
        //Log.d(TAG, "In addMovies method. Number of size = $storeSize")

        //notifyItemRangeInserted(storeSize, storeSize + movies.size)
        notifyDataSetChanged()
        Log.d(TAG, "In addMovies method. Number of size = ${movies.size}")
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var itemImage: ImageView = item.findViewById(R.id.card_image)
        var itemTitle: TextView = item.findViewById(R.id.card_title)
    }
}