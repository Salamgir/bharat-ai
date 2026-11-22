package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.BharatPreferencesRepository
import com.example.data.BharatUiState
import com.example.data.ChatMessage
import com.example.data.LocalBharatChatEngine
import com.example.data.StoredAppData
import com.example.data.UserProfile
import com.example.data.WalletTransaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class BharatViewModel(
    private val repository: BharatPreferencesRepository
) : ViewModel() {

    private val initialGreeting = ChatMessage(
        id = "welcome_msg",
        text = "Namaste! I'm BHARAT AI (Local Demo). Ask me to draft Hinglish messages, plan Indian trips, share UPI safety tips, or brainstorm ideas—all offline & free!",
        isUser = false,
        timestampLabel = "Ready"
    )

    private val chatMessagesFlow = MutableStateFlow(listOf(initialGreeting))
    private val typingFlow = MutableStateFlow(false)

    val uiState: StateFlow<BharatUiState> = combine(
        repository.storedDataFlow,
        chatMessagesFlow,
        typingFlow
    ) { stored, messages, isTyping ->
        BharatUiState(
            isLoaded = true,
            isLoggedIn = stored.isLoggedIn,
            isGuestMode = stored.isGuestMode,
            profile = UserProfile(
                name = stored.name,
                contact = stored.contact,
                city = stored.city,
                language = stored.language,
                bio = stored.bio
            ),
            walletCredits = stored.walletCredits,
            chatMessagesCount = stored.chatMessagesCount,
            photosEditedCount = stored.photosEditedCount,
            dailyBonusClaimed = stored.dailyBonusClaimed,
            selectedPlanId = stored.selectedPlanId,
            notifiedToolIds = stored.notifiedTools,
            chatMessages = messages,
            isAssistantTyping = isTyping,
            recentTransactions = buildTransactions(stored)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BharatUiState()
    )

    private fun buildTransactions(stored: StoredAppData): List<WalletTransaction> {
        val list = mutableListOf<WalletTransaction>()
        if (stored.dailyBonusClaimed) {
            list += WalletTransaction(
                id = "tx_bonus",
                title = "Daily Free Demo Bonus",
                subtitle = "Demo credit reward claimed",
                amountDelta = "+100 Credits",
                isCredit = true
            )
        }
        if (stored.chatMessagesCount > 0) {
            list += WalletTransaction(
                id = "tx_chat",
                title = "AI Chat Demo Sessions",
                subtitle = "${stored.chatMessagesCount} local prompts answered",
                amountDelta = "0 Credits (Free)",
                isCredit = true
            )
        }
        if (stored.photosEditedCount > 0) {
            list += WalletTransaction(
                id = "tx_photo",
                title = "On-Device Photo Studio",
                subtitle = "${stored.photosEditedCount} local filter previews saved",
                amountDelta = "0 Credits (Free)",
                isCredit = true
            )
        }
        list += WalletTransaction(
            id = "tx_welcome",
            title = "Swadeshi Welcome Grant",
            subtitle = "Starter MVP demo balance",
            amountDelta = "+1,250 Credits",
            isCredit = true
        )
        return list
    }

    fun signInDemo(name: String, contact: String) {
        viewModelScope.launch {
            repository.signInDemo(name = name, contact = contact, isGuest = false)
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            repository.signInDemo(name = "Guest Explorer", contact = "guest@bharatai.demo", isGuest = true)
        }
    }

    fun signOutDemo() {
        viewModelScope.launch {
            repository.signOutDemo()
        }
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateProfile(profile)
        }
    }

    fun sendChatMessage(rawText: String) {
        val trimmed = rawText.trim()
        if (trimmed.isEmpty() || typingFlow.value) return

        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = trimmed,
            isUser = true,
            timestampLabel = timeNow
        )

        chatMessagesFlow.update { it + userMsg }
        typingFlow.value = true

        viewModelScope.launch {
            delay(420)
            val reply = LocalBharatChatEngine.generateReply(
                rawPrompt = trimmed,
                userName = uiState.value.profile.name
            )
            val replyTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            val assistantMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                text = reply,
                isUser = false,
                timestampLabel = replyTime
            )
            chatMessagesFlow.update { it + assistantMsg }
            typingFlow.value = false
            repository.incrementChatUsage()
        }
    }

    fun clearChat() {
        chatMessagesFlow.value = listOf(initialGreeting)
    }

    fun recordPhotoEditSaved() {
        viewModelScope.launch {
            repository.incrementPhotoEditUsage()
        }
    }

    fun claimDailyBonus() {
        viewModelScope.launch {
            repository.claimDailyBonus()
        }
    }

    fun selectPreviewPlan(planId: String) {
        viewModelScope.launch {
            repository.selectPreviewPlan(planId)
        }
    }

    fun toggleToolNotification(toolRouteKey: String) {
        viewModelScope.launch {
            repository.toggleToolNotification(toolRouteKey)
        }
    }

    class Factory(
        private val repository: BharatPreferencesRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BharatViewModel(repository) as T
        }
    }
}
