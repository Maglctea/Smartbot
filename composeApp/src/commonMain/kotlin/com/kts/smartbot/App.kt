package com.kts.smartbot

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kts.smartbot.core.navigation.AppNavHost
import com.kts.smartbot.core.theme.SmartbotTheme
import com.kts.smartbot.di.appModule
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = { modules(appModule) }) {
        SmartbotTheme {
            AppNavHost()
        }
    }
}
