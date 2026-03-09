package com.kts.smartbot.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kts.smartbot.feature.auth.domain.model.AuthProvider
import com.kts.smartbot.feature.auth.domain.model.LoginResult
import com.kts.smartbot.feature.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<AuthEffect>()
    val effects = _effects.asSharedFlow()

    fun onEmailChanged(value: String) {
        _uiState.update { state ->
            state.copy(
                email = value,
                error = null,
            )
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { state ->
            state.copy(
                password = value,
                error = null,
            )
        }
    }

    fun onCaptchaTokenChanged(value: String) {
        _uiState.update { state ->
            state.copy(
                captchaToken = value,
                error = null,
            )
        }
    }

    fun onLoginClicked() {
        val state = _uiState.value
        if (!state.isSubmitEnabled) {
            return
        }

        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    error = null,
                )
            }

            when (
                val result = loginUseCase(
                    email = state.email,
                    password = state.password,
                    captchaToken = state.captchaToken,
                )
            ) {
                LoginResult.Success -> {
                    _uiState.update { current -> current.copy(isLoading = false) }
                    _effects.emit(AuthEffect.NavigateHome)
                }

                is LoginResult.Failure -> {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            captchaToken = "",
                            error = result.reason,
                            captchaResetKey = current.captchaResetKey + 1,
                        )
                    }
                }
            }
        }
    }

    fun onProviderClicked(provider: AuthProvider) {
        viewModelScope.launch {
            _effects.emit(AuthEffect.OpenSocialAuth(provider = provider))
        }
    }
}
