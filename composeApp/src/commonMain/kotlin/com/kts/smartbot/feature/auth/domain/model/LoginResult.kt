package com.kts.smartbot.feature.auth.domain.model

sealed interface LoginResult {
    data object Success : LoginResult

    data class Failure(
        val reason: AuthFailureReason,
    ) : LoginResult
}
