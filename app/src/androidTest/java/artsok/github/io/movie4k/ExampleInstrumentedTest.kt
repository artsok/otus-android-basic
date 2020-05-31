package artsok.github.io.movie4k

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import artsok.github.io.movie4k.recycler.MovieAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Before
    fun launchActivity() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Ignore("Need to find solution for CollapsingToolbarLayout with NestedScrollView")
    @Test
    fun shouldBeSelectedColorWhenClickOnItem() {
        val firstItem = 0
        onView(withId(R.id.recyclerViewFragment)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MovieAdapter.ViewHolder>(
                firstItem,
                clickOnViewChild(R.id.card_title)
            )
        )
        onView(withId(R.id.movie_image)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_title)).perform(scrollTo())
        onView(withId(R.id.movie_title)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_description)).perform(scrollTo())
        onView(withId(R.id.movie_description)).check(matches(isDisplayed()))
        onView(withId(R.id.movie_comment)).perform(scrollTo())
        onView(withId(R.id.movie_comment)).perform(
            typeText("feedback"),
            pressImeActionButton()
        )
        pressBack()
        onView(withId(R.id.recyclerViewFragment))
            .perform(RecyclerViewActions.scrollToPosition<MovieAdapter.ViewHolder>(firstItem))
            .check(
                matches(
                    atPositionOnView(
                        firstItem,
                        hasTextColor(R.color.selected),
                        R.id.card_title
                    )
                )
            )
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null
        override fun getDescription() = "Click on a child view with specified id."
        override fun perform(uiController: UiController, view: View) =
            click().perform(uiController, view.findViewById<View>(viewId))
    }

    private fun atPositionOnView(
        position: Int,
        itemMatcher: Matcher<View>,
        targetViewId: Int
    ): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has view id $itemMatcher at position $position")
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder =
                    recyclerView.findViewHolderForAdapterPosition(position)
                val targetView =
                    viewHolder!!.itemView.findViewById<View>(targetViewId)
                return itemMatcher.matches(targetView)
            }
        }
    }
}
