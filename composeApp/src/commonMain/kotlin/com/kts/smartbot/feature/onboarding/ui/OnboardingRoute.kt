package com.kts.smartbot.feature.onboarding.ui

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kts.smartbot.feature.auth.domain.usecase.IsUserAuthorizedUseCase
import com.kts.smartbot.feature.onboarding.domain.usecase.GetOnboardingPagesUseCase
import com.kts.smartbot.feature.onboarding.presentation.OnboardingEffect
import com.kts.smartbot.feature.onboarding.presentation.OnboardingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.koinInject
import org.jetbrains.compose.resources.stringResource
import smartbot.composeapp.generated.resources.Res
import smartbot.composeapp.generated.resources.app_site_url

@Composable
fun OnboardingRouteScreen(
    onNavigateHome: () -> Unit,
    onNavigateAuth: () -> Unit,
) {
    val getOnboardingPagesUseCase = koinInject<GetOnboardingPagesUseCase>()
    val isUserAuthorizedUseCase = koinInject<IsUserAuthorizedUseCase>()
    val uriHandler = LocalUriHandler.current
    val appSiteUrl = stringResource(Res.string.app_site_url)
    val viewModel = viewModel<OnboardingViewModel>(
        factory = viewModelFactory {
            initializer {
                OnboardingViewModel(
                    getOnboardingPagesUseCase = getOnboardingPagesUseCase,
                    isUserAuthorizedUseCase = isUserAuthorizedUseCase,
                )
            }
        },
    )
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { uiState.pages.size })

    BindOnboardingPager(
        pagerState = pagerState,
        onPageChanged = viewModel::onPageChanged,
    )

    LaunchedEffect(viewModel, pagerState) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                OnboardingEffect.NavigateAuth -> onNavigateAuth()
                OnboardingEffect.NavigateHome -> onNavigateHome()
                is OnboardingEffect.ScrollToPage -> pagerState.animateScrollToPage(effect.page)
            }
        }
    }

    OnboardingScreen(
        uiState = uiState,
        pagerState = pagerState,
        onSkipClicked = viewModel::onSkipClicked,
        onSiteClicked = { uriHandler.openUri(appSiteUrl) },
        onPrimaryActionClicked = viewModel::onPrimaryActionClicked,
    )
}

@Composable
private fun BindOnboardingPager(
    pagerState: PagerState,
    onPageChanged: (Int) -> Unit,
) {
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page: Int -> onPageChanged(page) }
    }
}
