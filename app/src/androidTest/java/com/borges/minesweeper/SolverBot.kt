package com.borges.minesweeper

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SolverBot {

    @Rule @JvmField
    val mainActivity = ActivityTestRule(MainActivity::class.java)

    @Test
    fun solve() {
        onView(withId(R.id.button_5_10)).perform(click())

        onView(withId(R.id.button_0_0)).perform(longClick())
    }

}