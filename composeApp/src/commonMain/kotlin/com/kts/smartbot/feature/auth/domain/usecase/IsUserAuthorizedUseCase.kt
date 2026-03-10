package com.kts.smartbot.feature.auth.domain.usecase

import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository

class IsUserAuthorizedUseCase(
    private val repository: AuthSessionRepository,
) {
    operator fun invoke(): Boolean = repository.isAuthorized()
}
