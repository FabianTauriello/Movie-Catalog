package io.github.fabiantauriello.moviecatalog

import android.widget.AutoCompleteTextView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.fabiantauriello.moviecatalog.ui.MainActivity
import kotlinx.coroutines.delay

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class UiTests {

    @Before
    fun setUp() {
        // launch MainActivity
        ActivityScenario.launch(MainActivity::class.java)
    }

    // Make sure bottom navigation is working as expected
    @Test
    fun testWatchlistNavigation() {
        // click on button
        Espresso.onView(withId(R.id.watchlistFragment)).perform(ViewActions.click())

        Thread.sleep(2000);

        // check that watchlist fragment is displayed
        Espresso.onView(withId(R.id.rv_list))
            .check(matches(isDisplayed()))
    }



}