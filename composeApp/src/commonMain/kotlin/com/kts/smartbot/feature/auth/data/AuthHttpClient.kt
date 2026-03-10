package com.kts.smartbot.feature.auth.data

import com.kts.smartbot.core.config.LocalAuthConfig
import com.kts.smartbot.core.logging.NapierKtorLogger
import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

expect fun createAuthHttpClient(sessionRepository: AuthSessionRepository): HttpClient

internal fun io.ktor.client.HttpClientConfig<*>.configureAuthHttpClient(
    sessionRepository: AuthSessionRepository,
) {
    expectSuccess = false

    install(DefaultRequest) {
        url(LocalAuthConfig.authApiBaseUrl)
        header(HttpHeaders.Accept, ContentType.Application.Json.toString())
        header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        header(HttpHeaders.Origin, LocalAuthConfig.authApiOrigin)
        header("Referer", LocalAuthConfig.authLoginReferer)
        sessionRepository.getCookies()
            .firstOrNull { cookie -> cookie.name.equals("spro_session", ignoreCase = true) }
            ?.value
            ?.takeIf { value -> value.isNotBlank() }
            ?.let { sessionToken ->
                header("spro_session", sessionToken)
            }
    }
    install(HttpCookies) {
        storage = AuthCookiesStorage(sessionRepository = sessionRepository)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
        connectTimeoutMillis = 15_000
        socketTimeoutMillis = 15_000
    }
    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 1)
        exponentialDelay()
    }
    install(Logging) {
        logger = NapierKtorLogger
        level = LogLevel.INFO
        sanitizeHeader { header ->
            header == HttpHeaders.Cookie ||
                header == HttpHeaders.SetCookie ||
                header.equals("spro_session", ignoreCase = true) ||
                header == HttpHeaders.Authorization
        }
    }
}
