package artsok.github.io.movie4k

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics

const val lastResponseTime = "lastResponse"

class App : Application() {

    lateinit var firebaseAnalytics: FirebaseAnalytics
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    companion object {
        lateinit var instance: App
            private set
    }

}