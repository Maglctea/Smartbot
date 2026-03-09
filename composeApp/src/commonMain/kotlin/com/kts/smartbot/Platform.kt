package com.kts.smartbot

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform