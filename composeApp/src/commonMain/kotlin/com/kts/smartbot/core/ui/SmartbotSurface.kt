package com.kts.smartbot.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kts.smartbot.core.theme.SmartbotDefaults

@Composable
fun SmartbotCard(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(SmartbotDefaults.cardPadding),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = SmartbotDefaults.cardShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = SmartbotDefaults.cardElevation,
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(paddingValues),
        ) {
            content()
        }
    }
}

@Composable
fun SmartbotSecondaryCard(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(SmartbotDefaults.compactCardPadding),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = SmartbotDefaults.secondaryCardShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = SmartbotDefaults.secondaryCardElevation,
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.padding(paddingValues),
        ) {
            content()
        }
    }
}

@Composable
fun SmartbotBrandPill(
    text: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = if (onClick != null) {
            modifier.clickable(onClick = onClick)
        } else {
            modifier
        },
        shape = SmartbotDefaults.pillShape,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
