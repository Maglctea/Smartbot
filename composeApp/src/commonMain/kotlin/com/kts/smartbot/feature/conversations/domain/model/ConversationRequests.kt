package com.kts.smartbot.feature.conversations.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ConversationListFilter(
    val limit: Int = 10,
    val offset: Int = 0,
    val isRead: Boolean? = null,
    val channelIds: List<String> = emptyList(),
    val listId: String? = null,
    val query: String? = null,
    val fromId: Long? = null,
    val fromDateUpdated: String? = null,
)

data class ConversationMessagesFilter(
    val conversationId: Long,
    val channelId: String,
    val userId: String,
    val fromId: String? = null,
    val fromDate: String? = null,
    val limit: Int = 20,
)

@Serializable
data class SendMessageRequest(
    @SerialName("conversation_id")
    val conversationId: Long,
    @SerialName("user_id")
    val userId: String,
    @SerialName("message_text")
    val messageText: String,
    val attachments: List<SendMessageAttachment> = emptyList(),
)

@Serializable
data class SendMessageAttachment(
    @SerialName("_id")
    val id: String,
    @SerialName("as_document")
    val asDocument: Boolean = false,
)

@Serializable
data class StopBotRequest(
    @SerialName("conversation_id")
    val conversationId: Long,
    @SerialName("user_id")
    val userId: String,
)

@Serializable
data class StartBotRequest(
    @SerialName("conversation_id")
    val conversationId: Long,
    @SerialName("user_id")
    val userId: String,
    @SerialName("block_id")
    val blockId: String,
)
