package artsok.github.io.movie4k.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import artsok.github.io.movie4k.presentation.MainActivity
import artsok.github.io.movie4k.util.RandomIntUtil

const val BUNDLE_NAME = "myBundle"
const val MOVIE_INFO = "MOVIE_INFO"
const val ALARM_NOTIFICATION_SCHEDULE = "ALARM_NOTIFICATION_SCHEDULE"

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "channel"
        val TAG = AlarmReceiver::class.toString()
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive")
        Log.d(TAG, "action = " + intent.action)
        showNotification(context, intent)
    }


    private fun showNotification(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(BUNDLE_NAME)
        val movie = bundle?.getParcelable<MovieDomainModel>(MOVIE_INFO) as MovieDomainModel
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        mainActivityIntent.putExtra(ALARM_NOTIFICATION_SCHEDULE, movie)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel name"
            val description = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Your should to watch it!")
            .setContentText(movie.title)
            .setSmallIcon(R.drawable.ic_like)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(RandomIntUtil.getRandomInt(), builder.build())
    }
}