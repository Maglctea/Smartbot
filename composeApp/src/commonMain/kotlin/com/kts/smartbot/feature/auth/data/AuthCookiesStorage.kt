package com.kts.smartbot.feature.auth.data

import com.kts.smartbot.feature.auth.domain.model.AuthCookie
import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url

class AuthCookiesStorage(
    private val sessionRepository: AuthSessionRepository,
) : CookiesStorage {
    override suspend fun addCookie(
        requestUrl: Url,
        cookie: Cookie,
    ) {
        val updatedCookies = sessionRepository.getCookies()
            .filterNot { it.name == cookie.name }
            .toMutableList()
            .apply {
                add(
                    AuthCookie(
                        name = cookie.name,
                        value = cookie.value,
                    ),
                )
            }

        sessionRepository.saveCookies(updatedCookies)
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        return sessionRepository.getCookies().map { cookie ->
            Cookie(
                name = cookie.name,
                value = cookie.value,
            )
        }
    }

    override fun close() = Unit
}
