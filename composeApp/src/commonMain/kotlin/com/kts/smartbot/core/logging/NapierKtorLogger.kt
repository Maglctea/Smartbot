package com.kts.smartbot.core.logging

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.logging.Logger

object NapierKtorLogger : Logger {
    override fun log(message: String) {
        Napier.d(tag = "KtorHttp") { message }
    }
}
