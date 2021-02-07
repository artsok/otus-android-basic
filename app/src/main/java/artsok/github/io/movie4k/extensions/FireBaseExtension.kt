package artsok.github.io.movie4k.extensions

import android.os.Bundle
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import com.google.firebase.analytics.FirebaseAnalytics


fun FirebaseAnalytics.logEventMethod(methodName: String, movie: MovieDomainModel) {
    logEvent(
        FirebaseAnalytics.Event.SELECT_CONTENT,
        Bundle().apply {
            putString(methodName, movie.title)
        })
}

