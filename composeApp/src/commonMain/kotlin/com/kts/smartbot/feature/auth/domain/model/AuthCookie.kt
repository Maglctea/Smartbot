package com.kts.smartbot.feature.auth.domain.model

data class AuthCookie(
    val name: String,
    val value: String,
)

private val authSessionCookieNames = setOf(
    "spro_session",
    "sessionid",
    "session",
    "auth_session",
    "smartbot_session",
)

fun AuthCookie.isAuthSessionCookie(): Boolean {
    return name.lowercase() in authSessionCookieNames && value.isNotBlank()
}

fun List<AuthCookie>.hasAuthorizedSession(): Boolean {
    return any { cookie -> cookie.isAuthSessionCookie() }
}
