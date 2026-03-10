package com.kts.smartbot.core.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiEnvelope<T>(
    val data: T,
    val status: String,
)

@Serializable
data class ApiErrorEnvelope(
    val code: String? = null,
    val status: String? = null,
    val message: String? = null,
)

@Serializable
data class EmptyApiPayload(
    val ignored: String? = null,
)
