package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.AiToolId
import com.example.data.BharatCatalog
import com.example.data.ToolAvailability
import com.example.ui.components.BharatToolCard

private enum class ToolsFilter(val label: String) {
    ALL("All Tools"),
    LIVE("Ready Now"),
    COMING_SOON("Coming Soon")
}

@Composable
fun AiToolsScreen(
    onOpenTool: (AiToolId) -> Unit
) {
    var selectedFilter by rememberSaveable { mutableStateOf(ToolsFilter.ALL) }

    val filteredTools = when (selectedFilter) {
        ToolsFilter.ALL -> BharatCatalog.tools
        ToolsFilter.LIVE -> BharatCatalog.tools.filter { it.availability != ToolAvailability.COMING_SOON }
        ToolsFilter.COMING_SOON -> BharatCatalog.tools.filter { it.availability == ToolAvailability.COMING_SOON }
    }

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
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "BHARAT AI Tools",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Explore working local tools and preview upcoming generative studios.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ToolsFilter.entries.forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                            ),
                            modifier = Modifier
                                .heightIn(min = 48.dp)
                                .testTag("tools_filter_${filter.name.lowercase()}")
                        )
                    }
                }
            }

            items(filteredTools, key = { it.id.routeKey }) { tool ->
                BharatToolCard(
                    tool = tool,
                    onClick = { onOpenTool(tool.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}
