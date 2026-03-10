package com.kts.smartbot.feature.auth.presentation

import com.kts.smartbot.feature.auth.domain.model.AuthFailureReason
import com.kts.smartbot.feature.auth.domain.model.AuthProvider

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val captchaToken: String = "",
    val isLoading: Boolean = false,
    val error: AuthFailureReason? = null,
    val captchaResetKey: Int = 0,
    val providers: List<AuthProvider> = AuthProvider.entries,
) {
    val isSubmitEnabled: Boolean
        get() = email.isNotBlank() &&
            password.isNotBlank() &&
            captchaToken.isNotBlank() &&
            !isLoading
}

sealed interface AuthEffect {
    data object NavigateHome : AuthEffect

    data class OpenSocialAuth(
        val provider: AuthProvider,
    ) : AuthEffect
}
