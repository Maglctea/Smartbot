package com.kts.smartbot.buildlogic

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class GenerateLocalConfigTask : DefaultTask() {
    @get:Input
    abstract val siteKey: Property<String>

    @get:Input
    abstract val host: Property<String>

    @get:Input
    abstract val apiBaseUrl: Property<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val escapedSiteKey = siteKey.get().escapeKotlinString()
        val escapedHost = host.get().escapeKotlinString()
        val escapedApiBaseUrl = apiBaseUrl.get().escapeKotlinString()
        val escapedApiOrigin = apiBaseUrl.get().removeSuffix("/").escapeKotlinString()
        val escapedAuthLoginReferer = "${apiBaseUrl.get()}auth/login/".escapeKotlinString()
        val configFile = outputDir.get().file("com/kts/smartbot/core/config/LocalAuthConfig.kt").asFile

        configFile.parentFile.mkdirs()
        configFile.writeText(
            """
            package com.kts.smartbot.core.config

            internal object LocalAuthConfig {
                const val smartCaptchaSiteKey: String = "$escapedSiteKey"
                const val smartCaptchaHost: String = "$escapedHost"
                const val authApiBaseUrl: String = "$escapedApiBaseUrl"
                const val authApiOrigin: String = "$escapedApiOrigin"
                const val authLoginReferer: String = "$escapedAuthLoginReferer"
            }
            """.trimIndent(),
        )
    }

    private fun String.escapeKotlinString(): String {
        return replace("\\", "\\\\").replace("\"", "\\\"")
    }
}
