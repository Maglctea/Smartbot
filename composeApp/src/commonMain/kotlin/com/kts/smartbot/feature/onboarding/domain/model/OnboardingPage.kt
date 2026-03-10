package com.kts.smartbot.feature.onboarding.domain.model

data class OnboardingPage(
    val type: OnboardingPageType,
)

enum class OnboardingPageType {
    Builder,
    Assistant,
    Automation,
}
