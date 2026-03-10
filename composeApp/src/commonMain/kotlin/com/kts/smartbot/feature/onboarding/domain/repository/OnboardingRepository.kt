package com.kts.smartbot.feature.onboarding.domain.repository

import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPage

interface OnboardingRepository {
    fun getPages(): List<OnboardingPage>
}
