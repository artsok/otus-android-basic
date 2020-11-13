package artsok.github.io.movie4k.extensions

import android.os.Bundle
import artsok.github.io.movie4k.domain.model.MovieDomainModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID
import com.google.firebase.analytics.FirebaseAnalytics.Param.METHOD


fun FirebaseAnalytics.logEventMethod(methodName: String, movie: MovieDomainModel) {
    logEvent(
        FirebaseAnalytics.Event.SELECT_CONTENT,
        Bundle().apply {
            putString(METHOD, methodName)
            putString(ITEM_ID, movie.title)
        })
}