package com.example.ui.screens

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.AiToolId
import com.example.data.BharatCatalog
import com.example.data.ToolAvailability
import com.example.ui.components.ToolAvailabilityBadge
import com.example.ui.components.toolIconFor
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronPrimary
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComingSoonToolScreen(
    toolId: AiToolId,
    isNotified: Boolean,
    onToggleNotify: () -> Unit,
    onBack: () -> Unit
) {
    val tool = BharatCatalog.tools.first { it.id == toolId }
    val context = LocalContext.current

    var previewPrompt by rememberSaveable(toolId) {
        mutableStateOf(
            when (toolId) {
                AiToolId.IMAGE_GENERATOR -> "A futuristic Jaipur Hawa Mahal glowing at sunset with golden lanterns"
                AiToolId.VIDEO_GENERATOR -> "Smooth aerial reel over Kerala backwaters with golden morning mist"
                AiToolId.VOICE_STUDIO -> "Namaste India! Welcome to Bharat AI Voice Studio preview."
                else -> ""
            }
        )
    }

    val options = remember(toolId) {
        when (toolId) {
            AiToolId.IMAGE_GENERATOR -> listOf("1:1 Square", "16:9 Banner", "9:16 Story")
            AiToolId.VIDEO_GENERATOR -> listOf("5s Reel", "10s Short", "HD 1080p")
            AiToolId.VOICE_STUDIO -> listOf("English (IN)", "Hindi Preview", "Warm Narrator")
            else -> emptyList()
        }
    }
    var selectedOption by rememberSaveable(toolId) { mutableStateOf(options.firstOrNull() ?: "") }

    // Free on-device Android TextToSpeech preview for AI Voice Studio
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember(context, toolId) {
        if (toolId == AiToolId.VOICE_STUDIO) {
            var engine: TextToSpeech? = null
            engine = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val res = engine?.setLanguage(Locale("en", "IN"))
                    ttsReady = res != TextToSpeech.LANG_MISSING_DATA && res != TextToSpeech.LANG_NOT_SUPPORTED
                }
            }
            engine
        } else null
    }

    DisposableEffect(tts) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(tool.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        ToolAvailabilityBadge(
                            availability = ToolAvailability.COMING_SOON,
                            label = "Coming Soon"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("coming_soon_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 760.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Status Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDeep)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(NavyDeep, NavyRoyal, SaffronPrimary.copy(alpha = 0.35f))
                                )
                            )
                            .padding(22.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(SaffronPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = toolIconFor(toolId),
                                contentDescription = tool.title,
                                tint = PureWhite,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            color = PureWhite.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(999.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Schedule,
                                    contentDescription = null,
                                    tint = PureWhite,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Coming Soon • No Paid APIs Active",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PureWhite
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${tool.title} Studio",
                            style = MaterialTheme.typography.headlineSmall,
                            color = PureWhite
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = tool.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = PureWhite.copy(alpha = 0.86f)
                        )
                    }
                }

                // Interactive Preview Workspace
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Studio Interface Preview",
                            style = MaterialTheme.typography.titleMedium
                        )

                        OutlinedTextField(
                            value = previewPrompt,
                            onValueChange = { previewPrompt = it },
                            label = {
                                Text(
                                    if (toolId == AiToolId.VOICE_STUDIO) "Sample Script" else "Creative Prompt Preview"
                                )
                            },
                            minLines = 2,
                            maxLines = 4,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("coming_soon_prompt_input")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            options.forEach { opt ->
                                FilterChip(
                                    selected = selectedOption == opt,
                                    onClick = { selectedOption = opt },
                                    label = { Text(opt, style = MaterialTheme.typography.labelSmall) },
                                    modifier = Modifier.heightIn(min = 48.dp)
                                )
                            }
                        }

                        if (toolId == AiToolId.VOICE_STUDIO) {
                            OutlinedButton(
                                onClick = {
                                    if (ttsReady && previewPrompt.isNotBlank()) {
                                        tts?.speak(previewPrompt, TextToSpeech.QUEUE_FLUSH, null, "voice_preview")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "On-device speech preview unavailable on this device",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp)
                                    .testTag("voice_local_tts_button"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Test Free On-Device Speech")
                            }
                        }

                        Button(
                            onClick = {
                                onToggleNotify()
                                val msg = if (!isNotified) {
                                    "Added ${tool.title} to your local launch waitlist!"
                                } else {
                                    "Removed from launch waitlist."
                                }
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 52.dp)
                                .testTag("coming_soon_notify_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isNotified) MaterialTheme.colorScheme.secondary else SaffronPrimary,
                                contentColor = PureWhite
                            )
                        ) {
                            Icon(
                                imageVector = if (isNotified) Icons.Filled.NotificationsActive else Icons.Filled.NotificationsNone,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (isNotified) "Waitlist Enabled (Coming Soon)" else "Notify Me When Available"
                            )
                        }
                    }
                }
            }
        }
    }
}
