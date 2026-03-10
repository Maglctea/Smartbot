package com.kts.smartbot.core.network

import io.ktor.http.HttpStatusCode

class ApiException(
    val statusCode: HttpStatusCode,
    val code: String? = null,
    override val message: String,
    cause: Throwable? = null,
) : Exception(message, cause)
