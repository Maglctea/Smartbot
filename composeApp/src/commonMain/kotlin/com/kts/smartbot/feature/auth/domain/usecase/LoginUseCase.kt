package com.kts.smartbot.feature.auth.domain.usecase

import com.kts.smartbot.feature.auth.domain.model.LoginResult
import com.kts.smartbot.feature.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        captchaToken: String,
    ): LoginResult {
        return repository.login(
            email = email,
            password = password,
            captchaToken = captchaToken,
        )
    }
}
