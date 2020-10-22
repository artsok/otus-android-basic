package artsok.github.io.movie4k.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.receiver.AlarmReceiver
import artsok.github.io.movie4k.receiver.BUNDLE_NAME
import artsok.github.io.movie4k.receiver.MOVIE_INFO

class AlarmService(private val context: Context) {

    private val alarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    companion object {
        val TAG = AlarmService::class.toString()
    }

    fun setExactAlarm(movie: MovieDomainModel, time: Long, requestCode: Int) {
        setAlarm(time, getPendingIntent(getIntent().apply {
            val bundle = Bundle()
            bundle.putParcelable(MOVIE_INFO, movie)
            putExtra(BUNDLE_NAME, bundle)
        }, requestCode))
    }

    fun stopAlarms(requestCode: Int) {
        Log.d(TAG, "stopAlarm for $requestCode")
        getPendingIntent(getIntent(), requestCode).cancel()
    }

    private fun setAlarm(time: Long, pendingIntent: PendingIntent) {
        alarmManager.let {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
    }

    private fun getIntent(): Intent = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent, requestCode: Int) =
        PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
}