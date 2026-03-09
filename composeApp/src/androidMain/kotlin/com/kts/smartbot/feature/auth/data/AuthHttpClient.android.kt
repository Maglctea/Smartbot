package com.kts.smartbot.feature.auth.data

import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun createAuthHttpClient(sessionRepository: AuthSessionRepository): HttpClient {
    return HttpClient(OkHttp) {
        configureAuthHttpClient(sessionRepository = sessionRepository)
    }
}
