package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.AiToolId
import com.example.data.BharatCatalog
import com.example.data.BharatUiState
import com.example.ui.components.BharatEmblemMark
import com.example.ui.components.BharatToolCard
import com.example.ui.components.SectionHeaderRow
import com.example.ui.components.TricolorAccentStrip
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SaffronVibrant

@Composable
fun HomeScreen(
    uiState: BharatUiState,
    onOpenTool: (AiToolId) -> Unit,
    onOpenChatWithPrompt: (String) -> Unit,
    onNavigateToToolsTab: () -> Unit,
    onNavigateToWalletTab: () -> Unit,
    onNavigateToProfileTab: () -> Unit,
    onOpenPremiumScreen: () -> Unit
) {
    val firstName = uiState.profile.name.trim().substringBefore(" ").ifBlank { "Friend" }
    val activePlan = BharatCatalog.plans.find { it.id == uiState.selectedPlanId } ?: BharatCatalog.plans.first()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Top Branded Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        BharatEmblemMark(size = 42.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "BHARAT AI",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Namaste, $firstName",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .testTag("home_wallet_pill")
                            .clip(RoundedCornerShape(999.dp))
                            .clickable(onClick = onNavigateToWalletTab),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccountBalanceWallet,
                                contentDescription = "Demo Wallet Balance",
                                tint = SaffronPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${uiState.walletCredits} Cr",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Hero Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(196.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_home_hero),
                                contentDescription = "BHARAT AI Hero Studio Banner",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                NavyDeep.copy(alpha = 0.90f),
                                                NavyRoyal.copy(alpha = 0.68f),
                                                SaffronPrimary.copy(alpha = 0.45f)
                                            )
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    color = PureWhite.copy(alpha = 0.16f),
                                    shape = RoundedCornerShape(999.dp)
                                ) {
                                    Text(
                                        text = "Made for India • Free Demo MVP",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PureWhite,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "India's All-in-One\nAI Creative Studio",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = PureWhite
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Chat locally, edit photos on-device, and preview upcoming generative tools.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PureWhite.copy(alpha = 0.88f)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { onOpenTool(AiToolId.CHAT) },
                                        modifier = Modifier
                                            .heightIn(min = 48.dp)
                                            .testTag("home_hero_chat_button"),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SaffronPrimary,
                                            contentColor = PureWhite
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.ChatBubble,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Try AI Chat Demo",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }
                            }
                        }
                        TricolorAccentStrip()
                    }
                }
            }

            // Quick Prompt Chips
            item {
                Column {
                    Text(
                        text = "Quick Start Prompts",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BharatCatalog.quickChatPrompts.forEachIndexed { index, prompt ->
                            AssistChip(
                                onClick = { onOpenChatWithPrompt(prompt) },
                                label = {
                                    Text(
                                        text = prompt,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = SaffronPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("quick_prompt_chip_$index")
                            )
                        }
                    }
                }
            }

            // Working Tools Section
            item {
                SectionHeaderRow(
                    title = "Ready to Use Now",
                    actionLabel = "All Tools",
                    onActionClick = onNavigateToToolsTab
                )
            }

            item {
                val liveTools = BharatCatalog.tools.take(2)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    liveTools.forEach { tool ->
                        BharatToolCard(
                            tool = tool,
                            onClick = { onOpenTool(tool.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Coming Soon Generative Studio Section
            item {
                SectionHeaderRow(
                    title = "Generative Studio",
                    actionLabel = "Explore",
                    onActionClick = onNavigateToToolsTab
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val studioTools = BharatCatalog.tools.drop(2)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BharatToolCard(
                            tool = studioTools[0],
                            onClick = { onOpenTool(studioTools[0].id) },
                            modifier = Modifier.weight(1f)
                        )
                        BharatToolCard(
                            tool = studioTools[1],
                            onClick = { onOpenTool(studioTools[1].id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    BharatToolCard(
                        tool = studioTools[2],
                        onClick = { onOpenTool(studioTools[2].id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Wallet, Premium & Profile Hub Cards
            item {
                SectionHeaderRow(title = "Account & Membership")
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Wallet + Profile Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_wallet_card")
                                .clip(MaterialTheme.shapes.medium)
                                .clickable(onClick = onNavigateToWalletTab),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AccountBalanceWallet,
                                        contentDescription = "Wallet",
                                        tint = SaffronPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Wallet",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "${uiState.walletCredits} Demo Credits",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SaffronPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_profile_card")
                                .clip(MaterialTheme.shapes.medium)
                                .clickable(onClick = onNavigateToProfileTab),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "Profile",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "User Profile",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = uiState.profile.city,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Premium Membership Preview Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_premium_card")
                            .clip(MaterialTheme.shapes.large)
                            .clickable(onClick = onOpenPremiumScreen),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = NavyDeep)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(NavyDeep, NavyRoyal, SaffronVibrant.copy(alpha = 0.25f))
                                    )
                                )
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(SaffronPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.WorkspacePremium,
                                        contentDescription = "Premium Membership",
                                        tint = PureWhite,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "Premium Membership",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = PureWhite
                                    )
                                    Text(
                                        text = "Current: ${activePlan.name} • Preview Plans",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PureWhite.copy(alpha = 0.82f)
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Open Premium Membership Preview",
                                tint = PureWhite
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(12.dp)) }
        }
    }
}
