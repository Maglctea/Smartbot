package com.kts.smartbot.feature.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kts.smartbot.feature.auth.domain.model.AuthProvider
import com.kts.smartbot.feature.auth.domain.usecase.LoginUseCase
import com.kts.smartbot.feature.auth.presentation.AuthEffect
import com.kts.smartbot.feature.auth.presentation.AuthViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject
import org.jetbrains.compose.resources.stringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.app_site_url

@Composable
fun AuthLoginRoute(
    onAuthorized: () -> Unit,
) {
    val loginUseCase = koinInject<LoginUseCase>()
    val viewModel = viewModel<AuthViewModel>(
        factory = viewModelFactory {
            initializer {
                AuthViewModel(
                    loginUseCase = loginUseCase,
                )
            }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    var socialAuthProvider by remember { mutableStateOf<AuthProvider?>(null) }
    val uriHandler = LocalUriHandler.current
    val appSiteUrl = stringResource(Res.string.app_site_url)

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                AuthEffect.NavigateHome -> onAuthorized()
                is AuthEffect.OpenSocialAuth -> socialAuthProvider = effect.provider
            }
        }
    }

    AuthLoginScreen(
        uiState = uiState,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onCaptchaTokenChanged = viewModel::onCaptchaTokenChanged,
        onLoginClicked = viewModel::onLoginClicked,
        onProviderClicked = viewModel::onProviderClicked,
        socialAuthPlaceholderProvider = socialAuthProvider,
        onDismissSocialAuthPlaceholder = { socialAuthProvider = null },
        onBrandClicked = { uriHandler.openUri(appSiteUrl) },
    )
}
