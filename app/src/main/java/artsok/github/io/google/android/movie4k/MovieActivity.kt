package artsok.github.io.google.android.movie4k

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


class MovieActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var comment: EditText
    private lateinit var movie: Movie

    private var chosenLike: Boolean = false
    private var userComment: StringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        movie = intent.getParcelableExtra(MARKER)!!

        imageView = findViewById(R.id.movie_image)
        title = findViewById(R.id.movie_title)
        description = findViewById(R.id.movie_description)
        comment = findViewById(R.id.movie_comment)

        imageView.setImageResource(movie.imageId)
        title.text = movie.title
        description.text = movie.description

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


    override fun onPause() {
        super.onPause()
        Log.d(
            TAG, "Movie is '${title.text}. Like checkbox is enabled: '$chosenLike'. " +
                    "Comment is '$userComment' "
        )
    }

    fun shareMovieClick(view: View) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hi, It's a good choose - ${movie.title}")
            type = "text/plain"
        }
        sendIntent.resolveActivity(packageManager)?.let {
            startActivity(sendIntent)
        }
    }

    fun likeClick(view: View) {
        val like = findViewById<ImageView>(R.id.movie_like)
        chosenLike = if (chosenLike) {
            like.setImageResource(R.drawable.ic_action_name)
            false
        } else {
            like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            true
        }
    }
}