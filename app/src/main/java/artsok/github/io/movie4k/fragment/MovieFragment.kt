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
import artsok.github.io.movie4k.data.DataStore
import artsok.github.io.movie4k.data.Movie


class MovieFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var comment: EditText
    private lateinit var like: ImageView
    private lateinit var share: ImageView
    private lateinit var movie: Movie

    private var favorite: Boolean = false
    private var userComment: StringBuilder = StringBuilder()


    companion object {
        const val TAG = "MovieFragment"
        private const val MOVIE = "MOVIE"

        fun newInstance(movie: Movie): MovieFragment {
            val fragment = MovieFragment()
            val bundle = Bundle()
            bundle.putParcelable(MOVIE, movie)
            fragment.arguments = bundle
            return fragment
        }
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
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setDataToView()
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

    private fun setDataToView() {
        movie = arguments?.getParcelable(MOVIE)!!
        //imageView.setImageResource(movie.imageId)
        title.text = movie.title
        description.text = movie.description
        if (movie.favorite) {
            like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
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
            like.setImageResource(R.drawable.ic_like)
            DataStore.movies.find { it.uniqueId == movie.uniqueId }?.favorite = false
            false
        } else {
            like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            DataStore.movies.find { it.uniqueId == movie.uniqueId }?.favorite = true
            true
        }
    }
}