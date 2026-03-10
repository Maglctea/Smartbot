package com.kts.smartbot.feature.conversations.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable
data class Conversation(
    val channel: ConversationChannel? = null,
    @SerialName("is_read")
    val isRead: Boolean? = null,
    @SerialName("last_message")
    val lastMessage: ConversationMessage? = null,
    val state: ConversationState? = null,
    val user: ConversationUser? = null,
    val id: Long? = null,
    @SerialName("date_updated")
    val dateUpdated: String? = null,
)

@Serializable
data class ConversationChannel(
    @SerialName("_id")
    val id: String? = null,
    val kind: String? = null,
    val name: String? = null,
    val url: String? = null,
    @SerialName("external_kind")
    val externalKind: String? = null,
    @SerialName("photo_url")
    val photoUrl: String? = null,
)

@Serializable
data class ConversationState(
    @SerialName("stopped_by_manager")
    val stoppedByManager: Boolean? = null,
    @SerialName("operator_tagged")
    val operatorTagged: Boolean? = null,
    @SerialName("has_unanswered_operator_message")
    val hasUnansweredOperatorMessage: Boolean? = null,
)

@Serializable
data class ConversationUser(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("last_name")
    val lastName: String? = null,
    @SerialName("last_seen")
    val lastSeen: String? = null,
    val url: String? = null,
    val username: String? = null,
    val photo: ConversationUserPhoto? = null,
)

@Serializable
data class ConversationUserPhoto(
    val url: String? = null,
)

@Serializable
data class ConversationMessage(
    @Serializable(with = ConversationAttachmentListSerializer::class)
    val attachments: List<ConversationAttachment> = emptyList(),
    @SerialName("date_created")
    val dateCreated: String? = null,
    val id: String? = null,
    val kind: String? = null,
    @SerialName("manager_email")
    val managerEmail: String? = null,
    val text: String? = null,
    @SerialName("conversation_id")
    val conversationId: Long? = null,
    @SerialName("is_read")
    val isRead: Boolean? = null,
    @SerialName("block_id")
    val blockId: String? = null,
    @SerialName("scenario_id")
    val scenarioId: String? = null,
    val bucket: String? = null,
    val extra: JsonObject? = null,
)

@Serializable
data class ConversationAttachment(
    @SerialName("as_document")
    val asDocument: Boolean? = null,
    val filename: String? = null,
    @SerialName("preview_url")
    val previewUrl: String? = null,
    val size: Long? = null,
    val type: String? = null,
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null,
)

internal object ConversationListSerializer :
    SingleOrListJsonSerializer<Conversation>(Conversation.serializer())

internal object ConversationMessageListSerializer :
    SingleOrListJsonSerializer<ConversationMessage>(ConversationMessage.serializer())

internal object ConversationAttachmentListSerializer :
    SingleOrListJsonSerializer<ConversationAttachment>(ConversationAttachment.serializer())

internal abstract class SingleOrListJsonSerializer<T>(
    serializer: KSerializer<T>,
) : JsonTransformingSerializer<List<T>>(ListSerializer(serializer)) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when (element) {
            JsonNull -> JsonArray(emptyList())
            is JsonArray -> element
            else -> JsonArray(listOf(element))
        }
    }
}
