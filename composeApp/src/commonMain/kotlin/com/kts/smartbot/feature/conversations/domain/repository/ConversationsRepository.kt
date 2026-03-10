package com.kts.smartbot.feature.conversations.domain.repository

import com.kts.smartbot.feature.conversations.domain.model.Conversation
import com.kts.smartbot.feature.conversations.domain.model.ConversationListFilter
import com.kts.smartbot.feature.conversations.domain.model.ConversationMessage
import com.kts.smartbot.feature.conversations.domain.model.ConversationMessagesFilter
import com.kts.smartbot.feature.conversations.domain.model.SendMessageRequest
import com.kts.smartbot.feature.conversations.domain.model.StartBotRequest
import com.kts.smartbot.feature.conversations.domain.model.StopBotRequest

interface ConversationsRepository {
    suspend fun listConversations(
        filter: ConversationListFilter = ConversationListFilter(),
    ): List<Conversation>

    suspend fun getConversation(
        userId: String,
        id: Long,
    ): Conversation

    suspend fun getConversationLite(
        userId: String,
        id: Long,
    ): Conversation

    suspend fun listMessages(
        filter: ConversationMessagesFilter,
    ): List<ConversationMessage>

    suspend fun sendMessage(
        request: SendMessageRequest,
    )

    suspend fun stopBot(
        request: StopBotRequest,
    )

    suspend fun startBot(
        request: StartBotRequest,
    )

    suspend fun obtainSubscriptionToken(): String
}
