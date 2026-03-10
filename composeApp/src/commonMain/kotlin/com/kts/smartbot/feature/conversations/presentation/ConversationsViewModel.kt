package com.kts.smartbot.feature.conversations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kts.smartbot.core.network.ApiException
import com.kts.smartbot.feature.conversations.domain.model.ConversationListFilter
import com.kts.smartbot.feature.conversations.domain.repository.ConversationsRepository
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ConversationsViewModel(
    private val repository: ConversationsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConversationsUiState())
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null

    fun onScreenStarted() {
        if (loadJob?.isActive == true) {
            return
        }

        if (_uiState.value.conversations.isNotEmpty() && _uiState.value.error == null) {
            return
        }

        loadConversations()
    }

    fun onScreenDisposed() {
        loadJob?.cancel()
        loadJob = null
        _uiState.update { current -> current.copy(isLoading = false) }
    }

    fun onRetryClicked() {
        loadConversations()
    }

    private fun loadConversations() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    error = null,
                )
            }

            try {
                val conversations = repository.listConversations(
                    filter = ConversationListFilter(),
                )
                _uiState.update {
                    ConversationsUiState(
                        isLoading = false,
                        conversations = conversations,
                    )
                }
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (throwable: Throwable) {
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        error = throwable.toConversationsError(),
                    )
                }
            }
        }
    }

    override fun onCleared() {
        loadJob?.cancel()
        loadJob = null
        super.onCleared()
    }

    private fun Throwable.toConversationsError(): ConversationsError {
        return when (this) {
            is ApiException -> {
                when {
                    statusCode == HttpStatusCode.Unauthorized || statusCode == HttpStatusCode.Forbidden -> {
                        ConversationsError.Unauthorized
                    }

                    statusCode == HttpStatusCode.ServiceUnavailable || statusCode.value in 500..599 -> {
                        ConversationsError.Network
                    }

                    else -> ConversationsError.Unknown
                }
            }

            else -> ConversationsError.Unknown
        }
    }
}
