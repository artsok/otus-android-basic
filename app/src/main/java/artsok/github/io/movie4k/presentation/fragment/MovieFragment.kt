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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.recycler.path
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModel
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModelFactory
import artsok.github.io.movie4k.service.AlarmService
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime


//Данные которые кладем в setAlarm, сам movie не обогощен данными. Например зашел в кино, проставил лайк потом хочу заскедулить его на опр.время а приезжает херовый
class MovieFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var comment: EditText
    private lateinit var like: ImageView
    private lateinit var share: ImageView
    private lateinit var schedule: ImageView
    private lateinit var movie: MovieDomainModel
    private lateinit var alarmService: AlarmService

    private var favorite: Boolean = false
    private var userComment: StringBuilder = StringBuilder()

    private val fm: FragmentManager
        get() = activity!!.supportFragmentManager

    private val movieViewModelFactory by lazy { MovieViewModelFactory(activity!!.application) }
    private val movieViewModel by lazy {
        ViewModelProvider(requireActivity(), movieViewModelFactory).get(
            MovieViewModel::class.java
        )
    }

    companion object {
        val TAG = MovieFragment::class.toString()
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
        initAlarmService()

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
        schedule = view.findViewById(R.id.movie_schedule)
    }

    private fun initAlarmService() {
        alarmService = AlarmService(context!!)
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
        schedule.setOnClickListener { clickScheduleMovieAlarm() }
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


    private fun clickScheduleMovieAlarm() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.show(fm, picker.toString())

        picker.addOnPositiveButtonClickListener {
            val localDateTime = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(picker.selection!!.toLong()),
                ZoneOffset.UTC
            ).toLocalDate()

            val materialTimePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(CLOCK_24H)
                .build()

            materialTimePicker.show(fm, materialTimePicker.toString())

            materialTimePicker.addOnPositiveButtonClickListener {
                val newHour = materialTimePicker.hour
                val newMinute = materialTimePicker.minute
                val scheduleDateTime =
                    localDateTime.atTime(newHour, newMinute).atZone(ZoneId.systemDefault())
                movieViewModel.updateScheduleTime(movie.uniqueId, scheduleDateTime.toString())

                alarmService.setExactAlarm(movie, ZonedDateTime.now().plusSeconds(6).toInstant().toEpochMilli())
                //alarmService.setExactAlarm(movie, scheduleDateTime.toInstant().toEpochMilli())
                alarmService.stopAlarms()
            }
        }
    }

    private fun clickOnFavorite() {
        favorite = if (favorite) {
            Log.d(TAG, "unselect favorite icon")
            like.setImageResource(R.drawable.ic_like)
            movieViewModel.unMoveToFavorite(movie.uniqueId)
            movie.favorite = false
            false
        } else {
            Log.d(TAG, "select favorite icon")
            like.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            movieViewModel.moveToFavorite(movie.uniqueId)
            movie.favorite = true
            true
        }
    }
}