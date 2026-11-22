package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoFilter
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.AiToolId
import com.example.data.AiToolItem
import com.example.data.ToolAvailability
import com.example.ui.theme.IndiaEmerald
import com.example.ui.theme.IndiaEmeraldSoft
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyRoyal
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.SaffronVibrant
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BharatEmblemMark(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(NavyDeep, NavyRoyal)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.72f)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = this.size.minDimension * 0.42f
            drawCircle(
                color = SaffronVibrant,
                radius = radius,
                center = center,
                style = Stroke(width = 2.2.dp.toPx())
            )
            for (i in 0 until 8) {
                val angle = Math.toRadians((i * 45).toDouble())
                val endX = center.x + (radius * 0.85f * cos(angle)).toFloat()
                val endY = center.y + (radius * 0.85f * sin(angle)).toFloat()
                drawLine(
                    color = PureWhite.copy(alpha = 0.9f),
                    start = center,
                    end = Offset(endX, endY),
                    strokeWidth = 1.4.dp.toPx()
                )
            }
            drawCircle(
                color = SaffronPrimary,
                radius = radius * 0.28f,
                center = center
            )
        }
    }
}

@Composable
fun TricolorAccentStrip(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
                .background(SaffronPrimary)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
                .background(PureWhite)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
                .background(NavyDeep)
        )
    }
}

@Composable
fun ToolAvailabilityBadge(
    availability: ToolAvailability,
    label: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (availability) {
        ToolAvailability.LIVE_DEMO -> IndiaEmeraldSoft to IndiaEmerald
        ToolAvailability.ON_DEVICE -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.secondary
        ToolAvailability.COMING_SOON -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
    }

    Surface(
        modifier = modifier,
        color = bgColor,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

fun toolIconFor(id: AiToolId): ImageVector = when (id) {
    AiToolId.CHAT -> Icons.Filled.ChatBubble
    AiToolId.PHOTO_EDITOR -> Icons.Filled.PhotoFilter
    AiToolId.IMAGE_GENERATOR -> Icons.Filled.Image
    AiToolId.VIDEO_GENERATOR -> Icons.Filled.Movie
    AiToolId.VOICE_STUDIO -> Icons.Filled.GraphicEq
}

@Composable
fun BharatToolCard(
    tool: AiToolItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLive = tool.availability != ToolAvailability.COMING_SOON
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tool_card_${tool.id.routeKey}")
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLive) 3.dp else 1.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isLive) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (isLive) {
                                Brush.linearGradient(listOf(SaffronPrimary, SaffronVibrant))
                            } else {
                                Brush.linearGradient(listOf(NavyDeep, NavyRoyal))
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = toolIconFor(tool.id),
                        contentDescription = tool.title,
                        tint = PureWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }

                ToolAvailabilityBadge(
                    availability = tool.availability,
                    label = tool.badgeText
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = tool.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = tool.subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = tool.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tool.creditCostLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isLive) "Open" else "Preview",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeaderRow(
    title: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = SaffronPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (actionLabel != null && onActionClick != null) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            )
        }
    }
}
