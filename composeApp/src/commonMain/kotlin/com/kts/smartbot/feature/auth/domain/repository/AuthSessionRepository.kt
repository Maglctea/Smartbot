package com.kts.smartbot.feature.auth.domain.repository

import com.kts.smartbot.feature.auth.domain.model.AuthCookie

interface AuthSessionRepository {
    fun isAuthorized(): Boolean

    fun getCookies(): List<AuthCookie>

    fun saveCookies(cookies: List<AuthCookie>)

    fun clear()
}
