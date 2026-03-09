package com.kts.smartbot.feature.auth.domain.usecase

import com.kts.smartbot.feature.auth.domain.repository.AuthSessionRepository

class LogoutUseCase(
    private val repository: AuthSessionRepository,
) {
    operator fun invoke() {
        repository.clear()
    }
}
