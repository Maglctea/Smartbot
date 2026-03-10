package com.kts.smartbot.feature.chats.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kts.smartbot.core.ui.SmartbotCard
import org.jetbrains.compose.resources.stringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.home_chats_description
import smartbot.composeapp.generated.resources.home_chats_title

@Composable
fun ChatsScreen(
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    SmartbotCard(modifier = modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(Res.string.home_chats_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(Res.string.home_chats_description),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant,
            )
        }
    }
}
