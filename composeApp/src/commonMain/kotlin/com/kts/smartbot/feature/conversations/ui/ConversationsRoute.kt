package com.kts.smartbot.feature.conversations.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kts.smartbot.feature.conversations.domain.repository.ConversationsRepository
import com.kts.smartbot.feature.conversations.presentation.ConversationsViewModel
import org.koin.compose.koinInject

@Composable
fun ConversationsRoute() {
    val repository = koinInject<ConversationsRepository>()
    val viewModel = viewModel<ConversationsViewModel>(
        factory = viewModelFactory {
            initializer {
                ConversationsViewModel(
                    repository = repository,
                )
            }
        },
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.onScreenStarted()
    }

    DisposableEffect(viewModel) {
        onDispose {
            viewModel.onScreenDisposed()
        }
    }

    ConversationsScreen(
        uiState = uiState,
        onRetryClicked = viewModel::onRetryClicked,
    )
}
