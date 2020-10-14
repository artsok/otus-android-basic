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
        val bundle = intent.getBundleExtra("myBundle")
        val movie = bundle.getParcelable("MOVIE_INFO") as MovieDomainModel
        val mainActivityIntent = Intent(context, MainActivity::class.java)
        mainActivityIntent.putExtra("ALARM_NOTIFICATION_SCHEDULE", movie)
        val pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel name" // getString(R.string.channel_name)
            val description = "Channel description" //getString(R.string.channel_description)
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
            .setPriority(NotificationCompat.PRIORITY_LOW) // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)

            /*.addAction(R.drawable.baseline_details_24, "Click me", pendingIntent)*/
            /* .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources,
                    R.drawable.sq500x500))
                    .setSummaryText("Summary"))*/
            /*.setStyle(NotificationCompat.BigTextStyle().bigText(resources.getString(R.string.lorem_ipsum)))*/
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(RandomIntUtil.getRandomInt(), builder.build())
    }
}