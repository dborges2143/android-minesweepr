package com.borges.minesweeper

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.Button
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SolverBot {

    @Rule @JvmField
    val mainActivity = ActivityTestRule(MainActivity::class.java)

    private val activity get() = mainActivity.activity

    @Test
    fun solve() {
        onView(withId(R.id.button_5_10)).perform(click())

        val button5_10: Button = activity.findViewById(R.id.button_5_10)
        println("button_5_10 value: ${button5_10.text}")
        println("button_5_10's background color: ${button5_10.solidColor}")
    }

}