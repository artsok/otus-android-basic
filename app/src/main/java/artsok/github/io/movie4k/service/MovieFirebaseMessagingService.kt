package artsok.github.io.movie4k.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import artsok.github.io.movie4k.R
import artsok.github.io.movie4k.data.model.toDomainModel
import artsok.github.io.movie4k.data.retrofit.MovieApi
import artsok.github.io.movie4k.presentation.MainActivity
import artsok.github.io.movie4k.util.RandomIntUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

const val NOTIFICATION_FCM = "ALARM_NOTIFICATION_SCHEDULE"
const val ANY_MOVIE_RANDOM_ID = 1

class MovieFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        val TAG = MovieFirebaseMessagingService::class.toString()
    }

    private val movieService = MovieApi.movieService

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(remoteMessage)
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "FCM token: $token")
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val notificationChannelId = "FCM_Notification"
        val id = remoteMessage.data["movie"]?.toInt() ?: ANY_MOVIE_RANDOM_ID
        val messageText = remoteMessage.data["text"]
        val movie = runBlocking(Dispatchers.IO) {
            coroutineScope {
                val movie = async {
                    movieService.getMovie(id).toDomainModel()
                }
                movie.await()
            }
        }

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.putExtra(NOTIFICATION_FCM, movie)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Movie recommendations"
            val description = "The most interesting film to watch"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(notificationChannelId, name, importance)
            channel.description = description
            val notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(this, notificationChannelId)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(messageText)
            .setContentText("${movie.title}\n ${movie.description}")
            .setSmallIcon(R.drawable.ic_like)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(RandomIntUtil.getRandomInt(), builder.build())
    }
}