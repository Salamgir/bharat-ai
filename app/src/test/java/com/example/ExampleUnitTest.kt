package com.example

import com.example.data.BharatCatalog
import com.example.data.LocalBharatChatEngine
import com.example.data.ToolAvailability
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun localChatEngine_returnsIndianContextResponses() {
    val greeting = LocalBharatChatEngine.generateReply("Namaste", "Aarav Sharma")
    assertTrue(greeting.contains("Namaste Aarav"))

    val trip = LocalBharatChatEngine.generateReply("Plan a 3-day Jaipur trip", "Aarav")
    assertTrue(trip.contains("Day 1"))

    val upi = LocalBharatChatEngine.generateReply("Explain UPI safety tips", "Aarav")
    assertTrue(upi.contains("1930"))
  }

  @Test
  fun catalog_marksGenerativeStudioToolsAsComingSoon() {
    val comingSoonIds = BharatCatalog.tools
      .filter { it.availability == ToolAvailability.COMING_SOON }
      .map { it.title }
    assertEquals(
      listOf("AI Image Generator", "AI Video Generator", "AI Voice"),
      comingSoonIds
    )
  }
}
