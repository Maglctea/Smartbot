package com.kts.smartbot.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kts.smartbot.feature.auth.domain.usecase.IsUserAuthorizedUseCase
import com.kts.smartbot.feature.onboarding.domain.usecase.GetOnboardingPagesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    getOnboardingPagesUseCase: GetOnboardingPagesUseCase,
    private val isUserAuthorizedUseCase: IsUserAuthorizedUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        OnboardingUiState(
            pages = getOnboardingPagesUseCase(),
        ),
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<OnboardingEffect>()
    val effects = _effects.asSharedFlow()

    fun onPageChanged(page: Int) {
        _uiState.update { state ->
            if (state.currentPage == page) {
                state
            } else {
                state.copy(currentPage = page)
            }
        }
    }

    fun onSkipClicked() {
        emitFinishEffect()
    }

    fun onPrimaryActionClicked() {
        val state = _uiState.value
        if (state.isLastPage) {
            emitFinishEffect()
            return
        }

        viewModelScope.launch {
            _effects.emit(OnboardingEffect.ScrollToPage(state.currentPage + 1))
        }
    }

    private fun emitFinishEffect() {
        viewModelScope.launch {
            _effects.emit(
                if (isUserAuthorizedUseCase()) {
                    OnboardingEffect.NavigateHome
                } else {
                    OnboardingEffect.NavigateAuth
                },
            )
        }
    }
}
