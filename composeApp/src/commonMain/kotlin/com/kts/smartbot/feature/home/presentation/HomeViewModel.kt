package com.kts.smartbot.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kts.smartbot.feature.auth.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects = _effects.asSharedFlow()

    fun onLogoutClicked() {
        viewModelScope.launch {
            logoutUseCase()
            _effects.emit(HomeEffect.NavigateAuth)
        }
    }
}
