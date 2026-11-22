package com.example.data

import androidx.compose.runtime.Immutable

enum class AiToolId(val routeKey: String) {
    CHAT("tool_chat"),
    PHOTO_EDITOR("tool_photo_editor"),
    IMAGE_GENERATOR("tool_image_generator"),
    VIDEO_GENERATOR("tool_video_generator"),
    VOICE_STUDIO("tool_voice_studio")
}

enum class ToolAvailability {
    LIVE_DEMO,
    ON_DEVICE,
    COMING_SOON
}

@Immutable
data class AiToolItem(
    val id: AiToolId,
    val title: String,
    val subtitle: String,
    val description: String,
    val availability: ToolAvailability,
    val badgeText: String,
    val creditCostLabel: String
)

@Immutable
data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestampLabel: String
)

@Immutable
data class WalletTransaction(
    val id: String,
    val title: String,
    val subtitle: String,
    val amountDelta: String,
    val isCredit: Boolean
)

@Immutable
data class PremiumPlan(
    val id: String,
    val name: String,
    val priceLabel: String,
    val tagline: String,
    val highlights: List<String>,
    val isRecommended: Boolean = false
)

@Immutable
data class UserProfile(
    val name: String = "Aarav Sharma",
    val contact: String = "aarav.demo@bharatai.in",
    val city: String = "Bengaluru, Karnataka",
    val language: String = "English + हिन्दी",
    val bio: String = "Exploring India's creative AI tools in free demo mode."
)

@Immutable
data class BharatUiState(
    val isLoaded: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isGuestMode: Boolean = false,
    val profile: UserProfile = UserProfile(),
    val walletCredits: Int = 1250,
    val chatMessagesCount: Int = 0,
    val photosEditedCount: Int = 0,
    val dailyBonusClaimed: Boolean = false,
    val selectedPlanId: String = "free",
    val notifiedToolIds: Set<String> = emptySet(),
    val chatMessages: List<ChatMessage> = emptyList(),
    val isAssistantTyping: Boolean = false,
    val recentTransactions: List<WalletTransaction> = emptyList()
)

object BharatCatalog {
    val tools: List<AiToolItem> = listOf(
        AiToolItem(
            id = AiToolId.CHAT,
            title = "AI Chat",
            subtitle = "Instant Local Assistant",
            description = "Ask questions, draft messages, plan Indian trips, or brainstorm ideas in our free local demo engine.",
            availability = ToolAvailability.LIVE_DEMO,
            badgeText = "Live Demo",
            creditCostLabel = "Free Demo"
        ),
        AiToolItem(
            id = AiToolId.PHOTO_EDITOR,
            title = "Photo Editor",
            subtitle = "On-Device Studio",
            description = "Pick any photo from your gallery and apply Saffron Glow, Royal Navy, contrast, saturation & rotation.",
            availability = ToolAvailability.ON_DEVICE,
            badgeText = "On-Device",
            creditCostLabel = "0 Credits"
        ),
        AiToolItem(
            id = AiToolId.IMAGE_GENERATOR,
            title = "AI Image Generator",
            subtitle = "Text-to-Art Canvas",
            description = "Create Indian festival posters, architectural scenes, and digital artwork from text prompts.",
            availability = ToolAvailability.COMING_SOON,
            badgeText = "Coming Soon",
            creditCostLabel = "Preview Only"
        ),
        AiToolItem(
            id = AiToolId.VIDEO_GENERATOR,
            title = "AI Video Generator",
            subtitle = "Short Reels & Clips",
            description = "Transform scripts and images into crisp vertical motion clips for creators across Bharat.",
            availability = ToolAvailability.COMING_SOON,
            badgeText = "Coming Soon",
            creditCostLabel = "Preview Only"
        ),
        AiToolItem(
            id = AiToolId.VOICE_STUDIO,
            title = "AI Voice",
            subtitle = "Multilingual Voice Lab",
            description = "Natural Indian voices across Hindi, Tamil, Bengali, Marathi & English—plus free on-device speech test.",
            availability = ToolAvailability.COMING_SOON,
            badgeText = "Coming Soon",
            creditCostLabel = "TTS Preview"
        )
    )

    val plans: List<PremiumPlan> = listOf(
        PremiumPlan(
            id = "free",
            name = "Swadeshi Free",
            priceLabel = "₹0 / month",
            tagline = "Active Demo MVP tier — completely free to explore.",
            highlights = listOf(
                "Unlimited Local AI Chat Demo",
                "On-Device Photo Editor & Filters",
                "1,250 Demo Bharat Credits",
                "Preview access to Coming Soon tools"
            )
        ),
        PremiumPlan(
            id = "plus",
            name = "Bharat Plus (Preview)",
            priceLabel = "₹199 / month",
            tagline = "Designed for students & everyday creators (preview only).",
            highlights = listOf(
                "5,000 Monthly Cloud AI Credits (planned)",
                "HD AI Image Generation (Coming Soon)",
                "Regional Voice Studio in 10+ languages",
                "Priority local & cloud workspace sync"
            ),
            isRecommended = true
        ),
        PremiumPlan(
            id = "pro",
            name = "Bharat Pro (Preview)",
            priceLabel = "₹499 / month",
            tagline = "Built for studios, startups & teams (no checkout yet).",
            highlights = listOf(
                "20,000 Monthly AI Credits (planned)",
                "AI Video Reels & 4K Upscaling (Coming Soon)",
                "Custom Voice Cloning Studio (Coming Soon)",
                "Commercial creator license"
            )
        )
    )

    val quickChatPrompts: List<String> = listOf(
        "Plan a 3-day Jaipur trip",
        "Write a Diwali wish in Hinglish",
        "Explain UPI safety tips",
        "Healthy Indian breakfast ideas"
    )
}
