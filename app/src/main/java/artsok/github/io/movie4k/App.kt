package artsok.github.io.movie4k

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

const val lastResponseTime = "lastResponse"

class App : Application() {

    lateinit var firebaseAnalytics: FirebaseAnalytics
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Log.d(TAG, "FCM registration token $token")
        })
        FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(R.xml.default_config)
            val configSettings = FirebaseRemoteConfigSettings.Builder()
            configSettings.minimumFetchIntervalInSeconds = 10L
            configSettings.fetchTimeoutInSeconds = 5L
            setConfigSettingsAsync(configSettings.build())
            //Remote config will apply on next start of the app
            fetchAndActivate()
        }
    }

    companion object {
        val TAG = App::class.toString()
        lateinit var instance: App
            private set
    }
}