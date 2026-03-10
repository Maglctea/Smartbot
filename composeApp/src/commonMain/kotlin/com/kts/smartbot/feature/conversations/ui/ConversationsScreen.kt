package com.kts.smartbot.feature.conversations.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kts.smartbot.core.ui.SmartbotCard
import com.kts.smartbot.core.ui.SmartbotSecondaryCard
import com.kts.smartbot.feature.conversations.domain.model.Conversation
import com.kts.smartbot.feature.conversations.presentation.ConversationsError
import com.kts.smartbot.feature.conversations.presentation.ConversationsUiState
import org.jetbrains.compose.resources.stringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.home_conversations_attachment_placeholder
import smartbot.composeapp.generated.resources.home_conversations_description
import smartbot.composeapp.generated.resources.home_conversations_empty
import smartbot.composeapp.generated.resources.home_conversations_error_network
import smartbot.composeapp.generated.resources.home_conversations_error_unauthorized
import smartbot.composeapp.generated.resources.home_conversations_error_unknown
import smartbot.composeapp.generated.resources.home_conversations_last_message_empty
import smartbot.composeapp.generated.resources.home_conversations_loading
import smartbot.composeapp.generated.resources.home_conversations_retry
import smartbot.composeapp.generated.resources.home_conversations_title
import smartbot.composeapp.generated.resources.home_conversations_unread

@Composable
fun ConversationsScreen(
    uiState: ConversationsUiState,
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ConversationsHeader()

        when {
            uiState.isLoading -> ConversationsLoadingState()
            uiState.error != null -> ConversationsErrorState(
                error = uiState.error,
                onRetryClicked = onRetryClicked,
            )
            uiState.conversations.isEmpty() -> ConversationsEmptyState()
            else -> LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 8.dp),
            ) {
                items(
                    items = uiState.conversations,
                    key = { conversation ->
                        conversation.id?.toString()
                            ?: conversation.user?.id
                            ?: conversation.channel?.id
                            ?: conversation.hashCode().toString()
                    },
                ) { conversation ->
                    ConversationListItem(conversation = conversation)
                }
            }
        }
    }
}

@Composable
private fun ConversationsHeader() {
    val colors = MaterialTheme.colorScheme

    SmartbotCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(Res.string.home_conversations_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(Res.string.home_conversations_description),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ConversationsLoadingState() {
    SmartbotCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
            Text(
                text = stringResource(Res.string.home_conversations_loading),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun ConversationsEmptyState() {
    SmartbotCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.home_conversations_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ConversationsErrorState(
    error: ConversationsError,
    onRetryClicked: () -> Unit,
) {
    val message = when (error) {
        ConversationsError.Unauthorized -> stringResource(Res.string.home_conversations_error_unauthorized)
        ConversationsError.Network -> stringResource(Res.string.home_conversations_error_network)
        ConversationsError.Unknown -> stringResource(Res.string.home_conversations_error_unknown)
    }

    SmartbotCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(onClick = onRetryClicked) {
                Text(stringResource(Res.string.home_conversations_retry))
            }
        }
    }
}

@Composable
private fun ConversationListItem(
    conversation: Conversation,
) {
    val colors = MaterialTheme.colorScheme

    SmartbotSecondaryCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = conversation.initialLetter(),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = conversation.title(),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (conversation.isRead == false) {
                        UnreadBadge()
                    }
                }

                Text(
                    text = conversation.previewText(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = conversation.metaLine(),
                    style = MaterialTheme.typography.labelMedium,
                    color = colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun UnreadBadge() {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Text(
            text = stringResource(Res.string.home_conversations_unread),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun Conversation.previewText(): String {
    val message = lastMessage
    val messageText = message?.text

    return when {
        !messageText.isNullOrBlank() -> messageText
        message?.attachments?.isNotEmpty() == true -> {
            stringResource(Res.string.home_conversations_attachment_placeholder)
        }

        else -> stringResource(Res.string.home_conversations_last_message_empty)
    }
}

private fun Conversation.title(): String {
    val conversationUser = user
    val conversationChannel = channel
    val username = conversationUser?.username
    val channelName = conversationChannel?.name
    val fullName = listOfNotNull(conversationUser?.firstName, conversationUser?.lastName)
        .joinToString(separator = " ")
        .trim()

    return when {
        fullName.isNotBlank() -> fullName
        !username.isNullOrBlank() -> "@$username"
        !channelName.isNullOrBlank() -> channelName
        id != null -> "Conversation #$id"
        else -> "Conversation"
    }
}

private fun Conversation.metaLine(): String {
    return listOfNotNull(
        channel?.kind?.uppercase(),
        user?.username?.takeIf { it.isNotBlank() }?.let { "@$it" },
        id?.let { "#$it" },
    ).joinToString(separator = " • ")
}

private fun Conversation.initialLetter(): String {
    val seed = title().trim()

    return seed.firstOrNull()?.uppercase() ?: "C"
}
