package com.kts.smartbot.feature.auth.data

import com.kts.smartbot.feature.auth.domain.model.AuthCookie
import com.kts.smartbot.feature.auth.domain.model.hasAuthorizedSession
import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository

class InMemoryAuthSessionRepository : AuthSessionRepository {
    private var cookies: List<AuthCookie> = emptyList()

    override fun isAuthorized(): Boolean {
        return cookies.hasAuthorizedSession()
    }

    override fun getCookies(): List<AuthCookie> {
        return cookies
    }

    override fun saveCookies(cookies: List<AuthCookie>) {
        this.cookies = cookies
    }

    override fun clear() {
        cookies = emptyList()
    }
}
