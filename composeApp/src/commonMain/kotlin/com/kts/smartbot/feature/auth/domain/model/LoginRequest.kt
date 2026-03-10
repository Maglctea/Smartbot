package com.kts.smartbot.feature.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("captcha_token")
    val captchaToken: String,
    val email: String,
    val password: String,
)
