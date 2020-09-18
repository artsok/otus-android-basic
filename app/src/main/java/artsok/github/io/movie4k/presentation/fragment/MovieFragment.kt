package artsok.github.io.movie4k

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.recycler.path
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModel
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModelFactory
import com.bumptech.glide.Glide


class MovieFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var comment: EditText
    private lateinit var like: ImageView
    private lateinit var share: ImageView
    private lateinit var movie: MovieDomainModel

    private var favorite: Boolean = false
    private var userComment: StringBuilder = StringBuilder()

    private val movieViewModelFactory by lazy { MovieViewModelFactory(activity!!.application) }
    private val movieViewModel by lazy {
        ViewModelProvider(requireActivity(), movieViewModelFactory).get(
            MovieViewModel::class.java
        )
    }

    companion object {
        const val TAG = "MovieFragment"
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "In onResume method")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "In onCreateView method")
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "In onViewCreated method")
        initViews(view)

        movieViewModel.selectedMovie.observe(
            this.viewLifecycleOwner,
            Observer<MovieDomainModel> { movie ->
                this.movie = movie
                title.text = movie.title
                description.text = movie.description
                Glide.with(requireContext())
                    .load("$path${movie.posterPath}")
                    .into(imageView)

                if (movie.favorite) {
                    like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                }
            })
        initTextChangedListener()
    }

    private fun initViews(view: View) {
        imageView = view.findViewById(R.id.movie_image)
        title = view.findViewById(R.id.movie_title)
        description = view.findViewById(R.id.movie_description)
        comment = view.findViewById(R.id.movie_comment)
        like = view.findViewById(R.id.movie_like)
        share = view.findViewById(R.id.movie_share_friend)
    }

    override fun onPause() {
        super.onPause()
        Log.d(
            TAG, "Movie is '${title.text}'. Favorite checkbox is enabled: '$favorite'. " +
                    "Comment is '$userComment' "
        )
    }

    private fun initTextChangedListener() {
        comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                userComment.clear()
                userComment.append(s)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })
        like.setOnClickListener { clickOnFavorite() }
        share.setOnClickListener { shareClick() }
    }

    private fun shareClick() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hi, It's a good choose - ${movie.title}")
            type = "text/plain"
        }
        sendIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(sendIntent)
        }
    }

    private fun clickOnFavorite() {
        favorite = if (favorite) {
            Log.d(TAG, "unselect favorite icon")
            like.setImageResource(R.drawable.ic_like)
            movieViewModel.unMoveToFavorite(movie.uniqueId)
            false
        } else {
            Log.d(TAG, "select favorite icon")
            like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            movieViewModel.moveToFavorite(movie.uniqueId)
            true
        }
    }
}