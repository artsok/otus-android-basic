package artsok.github.io.movie4k

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import artsok.github.io.movie4k.MainActivity.Companion.MARKER


class MovieActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MovieActivity"
    }

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var comment: EditText
    private lateinit var like: ImageView

    private lateinit var movie: Movie

    private var favorite: Boolean = false
    private var userComment: StringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        initView()
        setDataToView()
        initListeners()
    }

    override fun onPause() {
        super.onPause()
        Log.d(
            TAG, "Movie is '${title.text}'. Favorite checkbox is enabled: '$favorite'. " +
                    "Comment is '$userComment' "
        )
    }

    private fun initView() {
        imageView = findViewById(R.id.movie_image)
        title = findViewById(R.id.movie_title)
        description = findViewById(R.id.movie_description)
        comment = findViewById(R.id.movie_comment)
        like = findViewById(R.id.movie_like)
    }

    private fun setDataToView() {
        movie = intent.getParcelableExtra(MARKER)!!

        imageView.setImageResource(movie.imageId)
        title.text = movie.title
        description.text = movie.description
        if (movie.favorite) {
            like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
    }

    private fun initListeners() {
        comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                userComment.clear()
                userComment.append(s)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun shareClick(view: View) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hi, It's a good choose - ${movie.title}")
            type = "text/plain"
        }
        sendIntent.resolveActivity(packageManager)?.let {
            startActivity(sendIntent)
        }
    }

    fun clickOnFavorite(view: View) {
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