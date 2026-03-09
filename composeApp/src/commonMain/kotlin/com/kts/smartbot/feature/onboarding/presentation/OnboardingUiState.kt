package com.kts.smartbot.feature.onboarding.presentation

import com.kts.smartbot.feature.onboarding.domain.model.OnboardingPage

data class OnboardingUiState(
    val pages: List<OnboardingPage> = emptyList(),
    val currentPage: Int = 0,
) {
    val isLastPage: Boolean
        get() = pages.isNotEmpty() && currentPage == pages.lastIndex
}

sealed interface OnboardingEffect {
    data class ScrollToPage(val page: Int) : OnboardingEffect

    data object NavigateHome : OnboardingEffect

    data object NavigateAuth : OnboardingEffect
}
