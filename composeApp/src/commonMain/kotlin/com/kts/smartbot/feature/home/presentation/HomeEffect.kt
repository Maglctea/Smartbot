package com.kts.smartbot.feature.home.presentation

sealed interface HomeEffect {
    data object NavigateAuth : HomeEffect
}
