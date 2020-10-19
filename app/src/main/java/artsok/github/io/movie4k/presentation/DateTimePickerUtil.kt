package artsok.github.io.movie4k.presentation

import androidx.fragment.app.FragmentManager
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.viewmodel.MovieViewModel
import artsok.github.io.movie4k.service.AlarmService
import artsok.github.io.movie4k.util.RandomIntUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

interface DateTimePickerUtil {

    fun clickScheduleMovieAlarm(
        fm: FragmentManager,
        movie: MovieDomainModel,
        movieViewModel: MovieViewModel,
        alarmService: AlarmService
    ) {
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
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            materialTimePicker.show(fm, materialTimePicker.toString())

            materialTimePicker.addOnPositiveButtonClickListener {
                val newHour = materialTimePicker.hour
                val newMinute = materialTimePicker.minute
                val scheduleDateTime =
                    localDateTime.atTime(newHour, newMinute).atZone(ZoneId.systemDefault())
                val requestCode = RandomIntUtil.getRandomInt()
                val alarmTime = scheduleDateTime.toInstant().toEpochMilli()

                movieViewModel.updateScheduleTime(movie.uniqueId, scheduleDateTime.toString())
                movieViewModel.saveScheduleInfo(movie.title, requestCode, alarmTime)
                alarmService.setExactAlarm(
                    movie,
                    alarmTime,
                    requestCode
                )
            }
        }
    }
}