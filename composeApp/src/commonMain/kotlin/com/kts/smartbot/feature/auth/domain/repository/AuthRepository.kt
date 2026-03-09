package com.kts.smartbot.feature.auth.domain.repository

import com.kts.smartbot.feature.auth.domain.model.LoginResult

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
        captchaToken: String,
    ): LoginResult
}
