package com.example.storyapp.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.storyapp.BuildConfig
import com.example.storyapp.R
import com.example.storyapp.utils.EspressoIdlingResource
import org.hamcrest.Matchers.anyOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginlogout_Success() {
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.emailEditText)).perform(
            typeText(BuildConfig.USERNAME),
            closeSoftKeyboard()
        )
        onView(withId(R.id.passwordEditText)).perform(
            typeText(BuildConfig.PASSWORD),
            closeSoftKeyboard()
        )
        onView(withId(R.id.loginButton)).perform(click())
        onView(withText(R.string.dialog_positive_button))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(anyOf(withId(R.id.menu_logout), withText(R.string.logout))).perform(click())
        onView(withText(R.string.yes))
            .inRoot(RootMatchers.isDialog())
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.welcomeActivity)).check(matches(isDisplayed()))
    }
}