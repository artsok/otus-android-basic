package artsok.github.io.movie4k.presentation.recycler

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.listener.OnMovieSelectedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.time.LocalTime

const val path = "https://image.tmdb.org/t/p/w500"

class MovieAdapter(
    private val context: Context,
    private val listener: OnMovieSelectedListener
) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>(), Filterable {

    companion object {
        const val TAG = "MovieAdapter"
    }

    private var movies = emptyList<MovieDomainModel>()
    private var moviesFilter = emptyList<MovieDomainModel>()
    private var searchedMovies = mutableSetOf<MovieDomainModel>()

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
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d(TAG, "Failed to load image")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .into(viewHolder.itemImage)
    }

    fun addMovies(movies: List<MovieDomainModel>) {
        this.movies = movies
        //notifyItemRangeInserted(storeSize, storeSize + movies.size) TODO: use it
        notifyDataSetChanged()
        Log.d(TAG, "In addMovies method. Number of size = ${movies.size}")
    }

    fun addSearchedValue(list: List<MovieDomainModel>) {
        this.searchedMovies.addAll(list)
        Log.d(
            TAG,
            "Корзина элементов searchedMovies ${searchedMovies.size}" + LocalTime.now()
        )
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var itemImage: ImageView = item.findViewById(R.id.card_image)
        var itemTitle: TextView = item.findViewById(R.id.card_title)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence): FilterResults {
                Log.d(TAG, "Вызов performFiltering")
                val filterResults = FilterResults()
                filterResults.values = searchedMovies.toList()
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                Log.d(TAG, "Вызов publishResults")
                movies = results?.values as List<MovieDomainModel>
                notifyDataSetChanged()
                searchedMovies.clear()
                println("Завершение publishResults")
            }

        }
    }
}