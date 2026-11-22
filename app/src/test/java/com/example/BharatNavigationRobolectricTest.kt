package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w411dp-h915dp-xxhdpi")
class BharatNavigationRobolectricTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun ensureSignedInOnHome() {
        composeTestRule.waitUntil(timeoutMillis = 8_000) {
            composeTestRule.onAllNodesWithTag("login_guest_button").fetchSemanticsNodes().isNotEmpty() ||
                composeTestRule.onAllNodesWithTag("home_hero_chat_button").fetchSemanticsNodes().isNotEmpty()
        }
        if (composeTestRule.onAllNodesWithTag("login_guest_button").fetchSemanticsNodes().isNotEmpty()) {
            composeTestRule.onNodeWithTag("login_guest_button")
                .performScrollTo()
                .performClick()
        }
        composeTestRule.waitUntil(timeoutMillis = 8_000) {
            composeTestRule.onAllNodesWithTag("home_hero_chat_button").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.waitForIdle()
    }

    @Test
    fun loginAndBottomNavigationTabs_workEndToEnd() {
        ensureSignedInOnHome()

        // Home tab is visible
        composeTestRule.onNodeWithTag("home_hero_chat_button").assertIsDisplayed()

        // Navigate to AI Tools tab
        composeTestRule.onNodeWithTag("nav_ai_tools").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("BHARAT AI Tools").assertIsDisplayed()
        composeTestRule.onNodeWithTag("tools_filter_coming_soon").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("tool_card_tool_image_generator").assertIsDisplayed()

        // Navigate to Wallet tab and claim daily bonus
        composeTestRule.onNodeWithTag("nav_wallet").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("1250 Credits").assertIsDisplayed()
        composeTestRule.onNodeWithTag("wallet_claim_bonus_button")
            .performScrollTo()
            .performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithTag("wallet_open_premium_button").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("1350 Credits").assertIsDisplayed()

        // Open Premium Membership preview from Wallet
        composeTestRule.onNodeWithTag("wallet_open_premium_button")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Premium Membership").assertIsDisplayed()
        composeTestRule.onNodeWithTag("premium_select_plus")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("premium_back_button").performClick()
        composeTestRule.waitForIdle()

        // Navigate to Profile tab, edit local profile, and sign out back to Login
        composeTestRule.onNodeWithTag("nav_profile").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("profile_edit_button")
            .performScrollTo()
            .performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithTag("edit_profile_name_input").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("edit_profile_name_input")
            .performScrollTo()
            .performTextClearance()
        composeTestRule.onNodeWithTag("edit_profile_name_input")
            .performTextInput("Priya Nair")
        composeTestRule.onNodeWithTag("edit_profile_save_button")
            .performScrollTo()
            .performClick()
        composeTestRule.waitUntil(timeoutMillis = 8_000) {
            composeTestRule.onAllNodesWithText("Priya Nair").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Priya Nair").assertIsDisplayed()

        composeTestRule.onNodeWithTag("profile_sign_out_button")
            .performScrollTo()
            .performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithTag("login_submit_button").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("login_submit_button").assertIsDisplayed()
    }

    @Test
    fun aiChatAndToolsScreens_navigateAndRespondLocally() {
        ensureSignedInOnHome()

        // Open AI Chat from Home hero CTA
        composeTestRule.onNodeWithTag("home_hero_chat_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("chat_message_input").performTextInput("Explain UPI safety tips")
        composeTestRule.onNodeWithTag("chat_send_button").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule.onAllNodesWithTag("chat_back_button").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("chat_back_button").performClick()
        composeTestRule.waitForIdle()

        // Open AI Tools -> Photo Editor -> Back
        composeTestRule.onNodeWithTag("nav_ai_tools").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("tool_card_tool_photo_editor").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("photo_rotate_button").assertIsDisplayed().performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("photo_editor_back_button").performClick()
        composeTestRule.waitForIdle()

        // Open Coming Soon tool (AI Video Generator) -> Toggle waitlist -> Back
        composeTestRule.onNodeWithTag("tool_card_tool_video_generator").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("coming_soon_notify_button")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("coming_soon_back_button").performClick()
        composeTestRule.waitForIdle()
    }
}
