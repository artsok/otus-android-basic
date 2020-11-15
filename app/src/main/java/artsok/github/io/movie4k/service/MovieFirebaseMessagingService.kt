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
import artsok.github.io.movie4k.data.repository.MovieRepositoryImpl
import artsok.github.io.movie4k.data.retrofit.MovieApi
import artsok.github.io.movie4k.data.room.AppDatabase
import artsok.github.io.movie4k.domain.usecase.GetMoviesUseCase
import artsok.github.io.movie4k.presentation.MainActivity
import artsok.github.io.movie4k.util.RandomIntUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MovieFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        val TAG = MovieFirebaseMessagingService::class.toString()
    }

    private val useCase = GetMoviesUseCase(
        MovieRepositoryImpl(
            MovieApi.movieService,
            AppDatabase.getInstance(this, CoroutineScope(Dispatchers.IO)).movieDao(),
            AppDatabase.getInstance(this, CoroutineScope(Dispatchers.IO)).scheduleDao()
        )
    )

    /**
     * Message from FMS
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(remoteMessage)
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, token)
    }


    private fun showNotification(remoteMessage: RemoteMessage) {
        val notificationChannelId = "FCM_Notification"

        //val bundle = intent.getBundleExtra(BUNDLE_NAME)
        //val movie = bundle?.getParcelable<MovieDomainModel>(MOVIE_INFO) as MovieDomainModel


        val data = remoteMessage.data["movie"]

        val movie = null
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        //mainActivityIntent.putExtra(ALARM_NOTIFICATION_SCHEDULE, movie)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Movie_recommendation"
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
            .setContentTitle("Your should to watch it!")
            //.setContentText(movie.title)
            .setContentText(data)
            .setSmallIcon(R.drawable.ic_like)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(this)
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(RandomIntUtil.getRandomInt(), builder.build())
    }
}