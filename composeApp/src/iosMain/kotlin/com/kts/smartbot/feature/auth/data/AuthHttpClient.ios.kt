package com.kts.smartbot.feature.auth.data

import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun createAuthHttpClient(sessionRepository: AuthSessionRepository): HttpClient {
    return HttpClient(Darwin) {
        configureAuthHttpClient(sessionRepository = sessionRepository)
    }
}
