package com.example.skillsharex

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class SplashToLoginTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun tapGetStartedNavigatesToLogin() {
        composeRule.onNodeWithText("Get Started").performClick()
        composeRule.onNodeWithText("Skillsharex Login").assertExists()
    }
}
