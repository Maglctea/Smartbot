package com.kts.smartbot.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kts.smartbot.core.ui.SmartbotCard
import com.kts.smartbot.core.ui.SmartbotSecondaryCard
import org.jetbrains.compose.resources.stringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.home_brand
import smartbot.composeapp.generated.resources.home_description
import smartbot.composeapp.generated.resources.home_metric_channels_description
import smartbot.composeapp.generated.resources.home_metric_channels_title
import smartbot.composeapp.generated.resources.home_metric_channels_value
import smartbot.composeapp.generated.resources.home_metric_integrations_description
import smartbot.composeapp.generated.resources.home_metric_integrations_title
import smartbot.composeapp.generated.resources.home_metric_integrations_value
import smartbot.composeapp.generated.resources.home_open_onboarding
import smartbot.composeapp.generated.resources.home_title

@Composable
fun HomeScreen(
    onOpenOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        HomeHeroCard()

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HomeMetricCard(
                title = stringResource(Res.string.home_metric_channels_title),
                value = stringResource(Res.string.home_metric_channels_value),
                description = stringResource(Res.string.home_metric_channels_description),
                modifier = Modifier.weight(1f),
            )
            HomeMetricCard(
                title = stringResource(Res.string.home_metric_integrations_title),
                value = stringResource(Res.string.home_metric_integrations_value),
                description = stringResource(Res.string.home_metric_integrations_description),
                modifier = Modifier.weight(1f),
            )
        }

        Button(onClick = onOpenOnboarding) {
            Text(stringResource(Res.string.home_open_onboarding))
        }
    }
}

@Composable
private fun HomeHeroCard() {
    val colors = MaterialTheme.colorScheme

    SmartbotCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.home_brand),
                style = MaterialTheme.typography.labelLarge,
                color = colors.primary,
            )
            Text(
                text = stringResource(Res.string.home_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(Res.string.home_description),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HomeMetricCard(
    title: String,
    value: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    SmartbotSecondaryCard(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
