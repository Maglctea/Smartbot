package com.kts.smartbot.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kts.smartbot.feature.auth.ui.AuthLoginRoute
import com.kts.smartbot.feature.home.ui.HomeRouteScreen
import com.kts.smartbot.feature.onboarding.ui.OnboardingRouteScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    Surface(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = OnboardingRoute,
        ) {
            composable<OnboardingRoute> {
                OnboardingRouteScreen(
                    onNavigateHome = { navController.navigateToHomeFromOnboarding() },
                    onNavigateAuth = { navController.navigateToAuth() },
                )
            }

            composable<AuthRoute> {
                AuthLoginRoute(
                    onAuthorized = { navController.navigateToHomeFromAuth() },
                )
            }

            composable<HomeRoute> {
                HomeRouteScreen(
                    onOpenOnboarding = { navController.navigate(OnboardingRoute) },
                    onLogout = { navController.navigateToAuthFromHome() },
                )
            }
        }
    }
}

private fun NavHostController.navigateToAuth() {
    navigate(AuthRoute) {
        popUpTo(OnboardingRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateToAuthFromHome() {
    navigate(AuthRoute) {
        popUpTo(HomeRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateToHomeFromOnboarding() {
    navigate(HomeRoute) {
        popUpTo(OnboardingRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

private fun NavHostController.navigateToHomeFromAuth() {
    navigate(HomeRoute) {
        popUpTo(AuthRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
