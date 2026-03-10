package com.kts.smartbot.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kts.smartbot.feature.auth.domain.usecase.LogoutUseCase
import com.kts.smartbot.feature.home.presentation.HomeEffect
import com.kts.smartbot.feature.home.presentation.HomeViewModel
import com.kts.smartbot.feature.main.ui.MainScreen
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun HomeRouteScreen(
    onOpenOnboarding: () -> Unit,
    onLogout: () -> Unit,
) {
    val logoutUseCase = koinInject<LogoutUseCase>()
    val viewModel = viewModel<HomeViewModel>(
        factory = viewModelFactory {
            initializer {
                HomeViewModel(
                    logoutUseCase = logoutUseCase,
                )
            }
        },
    )

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                HomeEffect.NavigateAuth -> onLogout()
            }
        }
    }

    MainScreen(
        onOpenOnboarding = onOpenOnboarding,
        onLogoutClicked = viewModel::onLogoutClicked,
    )
}
