package artsok.github.io.movie4k

import android.app.Application

const val lastResponseTime = "lastResponse"

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: App? = null
            private set
    }

}