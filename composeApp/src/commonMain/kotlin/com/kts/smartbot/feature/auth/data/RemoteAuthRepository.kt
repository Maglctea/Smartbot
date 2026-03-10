package com.kts.smartbot.feature.auth.data

import com.kts.smartbot.feature.auth.domain.model.AuthFailureReason
import com.kts.smartbot.feature.auth.domain.model.LoginRequest
import com.kts.smartbot.feature.auth.domain.model.LoginResult
import com.kts.smartbot.feature.auth.domain.repository.AuthRepository
import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class RemoteAuthRepository(
    private val httpClient: HttpClient,
    private val sessionRepository: AuthSessionRepository,
    private val json: Json,
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String,
        captchaToken: String,
    ): LoginResult {
        return try {
            val response = httpClient.post("api/auth/login") {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(
                    json.encodeToString(
                        LoginRequest(
                            captchaToken = captchaToken,
                            email = email,
                            password = password,
                        ),
                    ),
                )
            }
            val body = response.bodyAsText()

            if (response.status.isSuccess()) {
                val payload = body.decodeApiMessage()
                if (payload?.status.equals("error", ignoreCase = true)) {
                    sessionRepository.clear()
                    LoginResult.Failure(reason = mapFailureReason(response.status, payload))
                } else {
                    LoginResult.Success
                }
            } else {
                sessionRepository.clear()
                LoginResult.Failure(
                    reason = mapFailureReason(
                        statusCode = response.status,
                        payload = body.decodeApiMessage(),
                    ),
                )
            }
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (_: Throwable) {
            sessionRepository.clear()
            LoginResult.Failure(reason = AuthFailureReason.Network)
        }
    }

    private fun String.decodeApiMessage(): AuthApiMessage? {
        if (isBlank()) {
            return null
        }

        return runCatching { json.decodeFromString<AuthApiMessage>(this) }
            .getOrNull()
    }

    private fun mapFailureReason(
        statusCode: HttpStatusCode,
        payload: AuthApiMessage?,
    ): AuthFailureReason {
        val code = payload?.code.orEmpty().lowercase()
        val message = payload?.message.orEmpty().lowercase()
        val text = "$code $message"

        return when {
            "captcha" in text -> AuthFailureReason.CaptchaRequired
            statusCode == HttpStatusCode.Unauthorized -> AuthFailureReason.InvalidCredentials
            statusCode == HttpStatusCode.BadRequest && ("email" in text || "password" in text || "credential" in text) -> {
                AuthFailureReason.InvalidCredentials
            }
            statusCode == HttpStatusCode.BadRequest && payload != null -> AuthFailureReason.CaptchaRequired
            statusCode.value in 500..599 -> AuthFailureReason.Network
            else -> AuthFailureReason.Unknown
        }
    }
}

@Serializable
private data class AuthApiMessage(
    val status: String? = null,
    val code: String? = null,
    val message: String? = null,
)
