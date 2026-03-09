package com.kts.smartbot.feature.onboarding.domain.usecase

import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPage
import com.kts.smartbot.feature.onboarding.domain.repository.OnboardingRepository

class GetOnboardingPagesUseCase(
    private val repository: OnboardingRepository,
) {
    operator fun invoke(): List<OnboardingPage> = repository.getPages()
}
