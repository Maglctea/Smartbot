package com.kts.smartbot.feature.conversations.data

import com.kts.smartbot.core.network.ApiEnvelope
import com.kts.smartbot.core.network.ApiErrorEnvelope
import com.kts.smartbot.core.network.ApiException
import com.kts.smartbot.core.network.EmptyApiPayload
import com.kts.smartbot.feature.conversations.domain.model.Conversation
import com.kts.smartbot.feature.conversations.domain.model.ConversationListFilter
import com.kts.smartbot.feature.conversations.domain.model.ConversationListSerializer
import com.kts.smartbot.feature.conversations.domain.model.ConversationMessage
import com.kts.smartbot.feature.conversations.domain.model.ConversationMessageListSerializer
import com.kts.smartbot.feature.conversations.domain.model.ConversationMessagesFilter
import com.kts.smartbot.feature.conversations.domain.model.SendMessageRequest
import com.kts.smartbot.feature.conversations.domain.model.StartBotRequest
import com.kts.smartbot.feature.conversations.domain.model.StopBotRequest
import com.kts.smartbot.feature.conversations.domain.repository.ConversationsRepository
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class ConversationsRepositoryImpl(
    private val httpClient: HttpClient,
    private val json: Json,
) : ConversationsRepository {
    override suspend fun listConversations(filter: ConversationListFilter): List<Conversation> {
        return request<ConversationsPayload> {
            httpClient.get("api/conversations/list") {
                parameter("limit", filter.limit)
                parameter("offset", filter.offset)
                parameter("is_read", filter.isRead?.toString().orEmpty())
                filter.channelIds.forEach { parameter("channel_ids", it) }
                filter.listId.takeIfNotBlank()?.let { parameter("list_id", it) }
                filter.query.takeIfNotBlank()?.let { parameter("q", it) }
                filter.fromId?.let { parameter("from_id", it) }
                filter.fromDateUpdated.takeIfNotBlank()?.let { parameter("from_date_updated", it) }
            }
        }.conversations
    }

    override suspend fun getConversation(userId: String, id: Long): Conversation {
        return request<Conversation> {
            httpClient.get("api/conversations/get_conversation") {
                parameter("user_id", userId)
                parameter("id", id)
            }
        }
    }

    override suspend fun getConversationLite(userId: String, id: Long): Conversation {
        return request<Conversation> {
            httpClient.get("api/conversations/get_conversation_lite") {
                parameter("user_id", userId)
                parameter("id", id)
            }
        }
    }

    override suspend fun listMessages(filter: ConversationMessagesFilter): List<ConversationMessage> {
        return request<MessagesPayload> {
            httpClient.get("api/conversations/list_messages") {
                parameter("conversation_id", filter.conversationId)
                parameter("channel_id", filter.channelId)
                parameter("user_id", filter.userId)
                filter.fromId.takeIfNotBlank()?.let { parameter("from_id", it) }
                filter.fromDate.takeIfNotBlank()?.let { parameter("from_date", it) }
                parameter("limit", filter.limit)
            }
        }.messages
    }

    override suspend fun sendMessage(request: SendMessageRequest) {
        request<EmptyApiPayload> {
            httpClient.post("api/conversations/send_message") {
                setBody(json.encodeToString(request))
            }
        }
    }

    override suspend fun stopBot(request: StopBotRequest) {
        request<EmptyApiPayload> {
            httpClient.post("api/conversations/stop_bot") {
                setBody(json.encodeToString(request))
            }
        }
    }

    override suspend fun startBot(request: StartBotRequest) {
        request<EmptyApiPayload> {
            httpClient.post("api/conversations/start_bot") {
                setBody(json.encodeToString(request))
            }
        }
    }

    override suspend fun obtainSubscriptionToken(): String {
        return request<SubscriptionTokenPayload> {
            httpClient.get("api/conversations/obtain_subscription_token")
        }.subscriptionToken
    }

    private suspend inline fun <reified T> request(
        crossinline execute: suspend () -> HttpResponse,
    ): T {
        try {
            val response = execute()
            val body = response.bodyAsText()
            val apiError = body.decodeApiError()
            Napier.d(tag = "ConversationsApi") { "HTTP ${response.status.value} body: $body" }

            if (!response.status.isSuccess()) {
                throw ApiException(
                    statusCode = response.status,
                    code = apiError?.code,
                    message = apiError?.message ?: "Request failed with status ${response.status.value}",
                )
            }

            val envelope = runCatching {
                json.decodeFromString<ApiEnvelope<T>>(body)
            }.getOrElse { cause ->
                throw ApiException(
                    statusCode = response.status,
                    code = apiError?.code,
                    message = "Failed to decode API response",
                    cause = cause,
                )
            }

            if (!envelope.status.equals("ok", ignoreCase = true)) {
                throw ApiException(
                    statusCode = response.status,
                    code = apiError?.code,
                    message = apiError?.message ?: "API returned status ${envelope.status}",
                )
            }

            return envelope.data
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (apiException: ApiException) {
            throw apiException
        } catch (throwable: Throwable) {
            throw ApiException(
                statusCode = HttpStatusCode.ServiceUnavailable,
                message = "Network request failed",
                cause = throwable,
            )
        }
    }

    private fun String.decodeApiError(): ApiErrorEnvelope? {
        if (isBlank()) {
            return null
        }

        return runCatching { json.decodeFromString<ApiErrorEnvelope>(this) }
            .getOrNull()
    }

    private fun String?.takeIfNotBlank(): String? {
        return this?.trim()?.takeIf { it.isNotEmpty() }
    }
}

@Serializable
private data class ConversationsPayload(
    @Serializable(with = ConversationListSerializer::class)
    val conversations: List<Conversation> = emptyList(),
)

@Serializable
private data class MessagesPayload(
    @Serializable(with = ConversationMessageListSerializer::class)
    val messages: List<ConversationMessage> = emptyList(),
)

@Serializable
private data class SubscriptionTokenPayload(
    @SerialName("subscription_token")
    val subscriptionToken: String,
)
