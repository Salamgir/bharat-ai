package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.bharatDataStore: DataStore<Preferences> by preferencesDataStore(name = "bharat_ai_prefs")

data class StoredAppData(
    val isLoggedIn: Boolean = false,
    val isGuestMode: Boolean = false,
    val name: String = "Aarav Sharma",
    val contact: String = "aarav.demo@bharatai.in",
    val city: String = "Bengaluru, Karnataka",
    val language: String = "English + हिन्दी",
    val bio: String = "Exploring India's creative AI tools in free demo mode.",
    val walletCredits: Int = 1250,
    val chatMessagesCount: Int = 0,
    val photosEditedCount: Int = 0,
    val dailyBonusClaimed: Boolean = false,
    val selectedPlanId: String = "free",
    val notifiedTools: Set<String> = emptySet()
)

class BharatPreferencesRepository(private val context: Context) {

    private object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val IS_GUEST_MODE = booleanPreferencesKey("is_guest_mode")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_CONTACT = stringPreferencesKey("user_contact")
        val USER_CITY = stringPreferencesKey("user_city")
        val USER_LANGUAGE = stringPreferencesKey("user_language")
        val USER_BIO = stringPreferencesKey("user_bio")
        val WALLET_CREDITS = intPreferencesKey("wallet_credits")
        val CHAT_COUNT = intPreferencesKey("chat_count")
        val PHOTO_EDIT_COUNT = intPreferencesKey("photo_edit_count")
        val DAILY_BONUS_CLAIMED = booleanPreferencesKey("daily_bonus_claimed")
        val SELECTED_PLAN_ID = stringPreferencesKey("selected_plan_id")
        val NOTIFIED_TOOLS = stringSetPreferencesKey("notified_tools")
    }

    val storedDataFlow: Flow<StoredAppData> = context.bharatDataStore.data.map { prefs ->
        StoredAppData(
            isLoggedIn = prefs[Keys.IS_LOGGED_IN] ?: false,
            isGuestMode = prefs[Keys.IS_GUEST_MODE] ?: false,
            name = prefs[Keys.USER_NAME] ?: "Aarav Sharma",
            contact = prefs[Keys.USER_CONTACT] ?: "aarav.demo@bharatai.in",
            city = prefs[Keys.USER_CITY] ?: "Bengaluru, Karnataka",
            language = prefs[Keys.USER_LANGUAGE] ?: "English + हिन्दी",
            bio = prefs[Keys.USER_BIO] ?: "Exploring India's creative AI tools in free demo mode.",
            walletCredits = prefs[Keys.WALLET_CREDITS] ?: 1250,
            chatMessagesCount = prefs[Keys.CHAT_COUNT] ?: 0,
            photosEditedCount = prefs[Keys.PHOTO_EDIT_COUNT] ?: 0,
            dailyBonusClaimed = prefs[Keys.DAILY_BONUS_CLAIMED] ?: false,
            selectedPlanId = prefs[Keys.SELECTED_PLAN_ID] ?: "free",
            notifiedTools = prefs[Keys.NOTIFIED_TOOLS] ?: emptySet()
        )
    }

    suspend fun signInDemo(name: String, contact: String, isGuest: Boolean) {
        context.bharatDataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = true
            prefs[Keys.IS_GUEST_MODE] = isGuest
            prefs[Keys.USER_NAME] = name.trim().ifBlank { if (isGuest) "Guest Explorer" else "Aarav Sharma" }
            prefs[Keys.USER_CONTACT] = contact.trim().ifBlank { if (isGuest) "guest@bharatai.demo" else "aarav.demo@bharatai.in" }
        }
    }

    suspend fun signOutDemo() {
        context.bharatDataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = false
        }
    }

    suspend fun updateProfile(profile: UserProfile) {
        context.bharatDataStore.edit { prefs ->
            prefs[Keys.USER_NAME] = profile.name.trim().ifBlank { "Aarav Sharma" }
            prefs[Keys.USER_CONTACT] = profile.contact.trim().ifBlank { "aarav.demo@bharatai.in" }
            prefs[Keys.USER_CITY] = profile.city.trim().ifBlank { "Bengaluru, Karnataka" }
            prefs[Keys.USER_LANGUAGE] = profile.language.trim().ifBlank { "English + हिन्दी" }
            prefs[Keys.USER_BIO] = profile.bio.trim().ifBlank { "Exploring BHARAT AI." }
            prefs[Keys.IS_GUEST_MODE] = false
        }
    }

    suspend fun incrementChatUsage() {
        context.bharatDataStore.edit { prefs ->
            val count = prefs[Keys.CHAT_COUNT] ?: 0
            prefs[Keys.CHAT_COUNT] = count + 1
        }
    }

    suspend fun incrementPhotoEditUsage() {
        context.bharatDataStore.edit { prefs ->
            val count = prefs[Keys.PHOTO_EDIT_COUNT] ?: 0
            prefs[Keys.PHOTO_EDIT_COUNT] = count + 1
        }
    }

    suspend fun claimDailyBonus() {
        context.bharatDataStore.edit { prefs ->
            if (prefs[Keys.DAILY_BONUS_CLAIMED] != true) {
                val current = prefs[Keys.WALLET_CREDITS] ?: 1250
                prefs[Keys.WALLET_CREDITS] = current + 100
                prefs[Keys.DAILY_BONUS_CLAIMED] = true
            }
        }
    }

    suspend fun selectPreviewPlan(planId: String) {
        context.bharatDataStore.edit { prefs ->
            prefs[Keys.SELECTED_PLAN_ID] = planId
        }
    }

    suspend fun toggleToolNotification(toolRouteKey: String) {
        context.bharatDataStore.edit { prefs ->
            val current = prefs[Keys.NOTIFIED_TOOLS] ?: emptySet()
            prefs[Keys.NOTIFIED_TOOLS] = if (toolRouteKey in current) {
                current - toolRouteKey
            } else {
                current + toolRouteKey
            }
        }
    }
}
