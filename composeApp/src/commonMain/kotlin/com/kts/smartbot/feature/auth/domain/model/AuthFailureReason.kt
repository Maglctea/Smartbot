package com.kts.smartbot.feature.auth.domain.model

enum class AuthFailureReason {
    InvalidCredentials,
    CaptchaRequired,
    Network,
    Unknown,
}
