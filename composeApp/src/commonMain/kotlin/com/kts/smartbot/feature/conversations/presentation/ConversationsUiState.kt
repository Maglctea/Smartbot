package com.kts.smartbot.feature.conversations.presentation

import com.kts.smartbot.feature.conversations.domain.model.Conversation

data class ConversationsUiState(
    val isLoading: Boolean = true,
    val conversations: List<Conversation> = emptyList(),
    val error: ConversationsError? = null,
)

enum class ConversationsError {
    Unauthorized,
    Network,
    Unknown,
}
