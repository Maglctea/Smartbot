package com.kts.smartbot.core.logging

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object AppLogger {
    private var initialized = false

    fun init() {
        if (initialized) {
            return
        }

        Napier.base(DebugAntilog())
        initialized = true
    }
}
