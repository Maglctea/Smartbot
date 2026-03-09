package com.kts.smartbot.feature.onboarding.data

import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPage
import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPageType
import com.kts.smartbot.feature.onboarding.domain.repository.OnboardingRepository

class StaticOnboardingRepository : OnboardingRepository {
    override fun getPages(): List<OnboardingPage> {
        return listOf(
            OnboardingPage(type = OnboardingPageType.Builder),
            OnboardingPage(type = OnboardingPageType.Assistant),
            OnboardingPage(type = OnboardingPageType.Automation),
        )
    }
}
