package com.example.data

import java.util.Locale

object LocalBharatChatEngine {

    fun generateReply(rawPrompt: String, userName: String): String {
        val prompt = rawPrompt.trim()
        val lower = prompt.lowercase(Locale.ROOT)
        val firstName = userName.trim().substringBefore(" ").ifBlank { "Friend" }

        return when {
            lower.containsAny("namaste", "hello", "hi", "hey", "kaise ho", "kya haal", "good morning", "good evening") -> {
                "Namaste $firstName! Welcome to BHARAT AI (Local Demo Mode).\n\n" +
                    "I run completely on your device with zero paid APIs. Ask me to:\n" +
                    "• Plan Indian travel itineraries (Jaipur, Varanasi, Kerala, Ladakh)\n" +
                    "• Draft Hinglish or English festive wishes & emails\n" +
                    "• Share UPI digital safety checklists\n" +
                    "• Suggest Indian recipes, study plans, or Kotlin/app tips"
            }

            lower.containsAny("jaipur", "trip", "travel", "varanasi", "kerala", "goa", "ladakh", "itinerary", "delhi", "mumbai") -> {
                "Here is a crisp 3-day Bharat Travel Plan inspired by your prompt:\n\n" +
                    "Day 1 — Heritage & Architecture\n" +
                    "• Morning: Sunrise visit to iconic forts & stepwells before 9 AM.\n" +
                    "• Afternoon: Local handicrafts bazaar & regional thali lunch.\n" +
                    "• Evening: Sunset panoramic viewpoint and cultural folk show.\n\n" +
                    "Day 2 — Culture & Street Flavors\n" +
                    "• Morning: Guided old-city walking trail & museum stop.\n" +
                    "• Evening: Lakeside chai and curated local food tour.\n\n" +
                    "Day 3 — Slow Morning & Souvenirs\n" +
                    "• Artisan workshop visit, local sweets packing, and relaxed departure.\n\n" +
                    "Tip: Keep digital UPI & offline maps ready for smooth local transit!"
            }

            lower.containsAny("diwali", "holi", "eid", "festive", "wish", "hinglish", "greeting", "birthday") -> {
                "Here are 3 warm Hinglish & English wishes you can copy:\n\n" +
                    "1. \"Aapko aur aapke poore parivaar ko roshni, khushiyan aur samriddhi bhari shubhkamnayein!\"\n\n" +
                    "2. \"May this celebration bring bright moments, good health, and endless success to your home and work. Warm wishes!\"\n\n" +
                    "3. \"Dil se wish kar rahe hain ki aapka har sapna iss saal chamke. Stay blessed and keep shining!\""
            }

            lower.containsAny("upi", "safety", "scam", "fraud", "bank", "cyber", "password", "otp") -> {
                "Top 5 Digital & UPI Safety Rules in India:\n\n" +
                    "1. PIN is ONLY to send money: Never enter your UPI PIN to receive a refund, prize, or cashback.\n" +
                    "2. Verify QR & VPA names: Always check the receiver's registered banking name before confirming.\n" +
                    "3. Never share OTP or screen: Banks and NPCI never ask for OTPs, CVV, or screen-sharing apps.\n" +
                    "4. Set daily app limits: Keep a practical per-transaction cap for everyday QR payments.\n" +
                    "5. Report fast: Call 1930 (National Cyber Crime Helpline) or visit cybercrime.gov.in immediately."
            }

            lower.containsAny("breakfast", "food", "recipe", "chai", "dosa", "poha", "paneer", "diet", "lunch", "dinner") -> {
                "Wholesome Indian Meal Ideas (Quick & Energizing):\n\n" +
                    "• Kanda Poha with Roasted Peanuts: Light, iron-rich, ready in 12 minutes with lemon & curry leaves.\n" +
                    "• Moong Dal Chilla + Mint Chutney: High-protein savory pancakes with grated veggies & paneer.\n" +
                    "• Ragi Idli & Sambar: Calcium-rich millet steamed cakes with warm lentil-vegetable stew.\n" +
                    "• Masala Oats Upma: Fiber-rich bowl with mustard seeds, peas, and roasted cashews.\n\n" +
                    "Pair with ginger-cardamom chai or unsweetened filter coffee!"
            }

            lower.containsAny("hindi", "translate", "meaning", "english", "email", "letter", "leave", "resume") -> {
                "Professional Draft & Bilingual Helper for $firstName:\n\n" +
                    "• Subject: Quick Update / Request\n" +
                    "• Opening: \"Namaste / Dear Team, Hope you are doing well.\"\n" +
                    "• Body: State your main point in 2 clear sentences, include dates/timings, and close politely.\n" +
                    "• Hinglish option: \"Aapke samay aur sahyog ke liye dhanyavaad!\"\n\n" +
                    "Tip: Use the speaker icon below to hear this draft read aloud on your phone."
            }

            lower.containsAny("yoga", "health", "fitness", "study", "exam", "focus", "meditation") -> {
                "Balanced Daily Routine (Bharat Wellness & Focus):\n\n" +
                    "1. Morning Reset (15 mins): Surya Namaskar + 5 minutes of Anulom Vilom breathing.\n" +
                    "2. Deep Focus Blocks: Work or study in 45-minute sprints with 10-minute hydration breaks.\n" +
                    "3. Evening Wind-down: Short walk after dinner and zero screens 30 minutes before sleep."
            }

            lower.containsAny("code", "kotlin", "android", "compose", "python", "startup", "business", "pitch") -> {
                "Practical Builder Checklist for $firstName:\n\n" +
                    "1. Start Lean (MVP First): Solve one real everyday problem with a clean, fast mobile UI.\n" +
                    "2. Design for Bharat: Support offline-friendly workflows, clear typography, and multilingual ease.\n" +
                    "3. Ship & Iterate: Test core navigation and user flows early before adding cloud complexity.\n\n" +
                    "Note: BHARAT AI is running 100% locally in free demo mode."
            }

            else -> {
                "BHARAT AI Local Demo Response for \"$prompt\":\n\n" +
                    "• Summary: Here is a structured starting point tailored for you, $firstName.\n" +
                    "• Step 1: Define the core goal of \"$prompt\" in one crisp sentence.\n" +
                    "• Step 2: Organize into 3 actionable points (Context, Key Steps, Next Action).\n" +
                    "• Step 3: Listen to this draft using the speaker button or try the On-Device Photo Editor.\n\n" +
                    "Note: Cloud AI APIs are disabled in this free MVP; all replies run locally at zero cost."
            }
        }
    }

    private fun String.containsAny(vararg keywords: String): Boolean {
        return keywords.any { this.contains(it) }
    }
}
