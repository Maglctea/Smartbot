package com.kts.smartbot.feature.auth.ui.captcha

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.kts.smartbot.core.config.LocalAuthConfig

@Composable
fun SmartCaptchaWidget(
    languageCode: String,
    resetKey: Int,
    onTokenChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    key(resetKey, languageCode) {
        PlatformSmartCaptchaWidget(
            languageCode = languageCode,
            siteKey = LocalAuthConfig.smartCaptchaSiteKey,
            captchaHost = LocalAuthConfig.smartCaptchaHost,
            onTokenChanged = onTokenChanged,
            modifier = modifier,
        )
    }
}

@Composable
internal expect fun PlatformSmartCaptchaWidget(
    languageCode: String,
    siteKey: String,
    captchaHost: String,
    onTokenChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
)

internal fun buildSmartCaptchaHtml(
    languageCode: String,
    siteKey: String,
): String {
    val safeLanguageCode = languageCode
        .replace("\\", "\\\\")
        .replace("'", "\\'")

    val safeSiteKey = siteKey
        .replace("\\", "\\\\")
        .replace("'", "\\'")

    return """
        <!doctype html>
        <html lang="$safeLanguageCode">
        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
            <style>
                html, body {
                    margin: 0;
                    padding: 0;
                    width: 100%;
                    height: 100%;
                    background: transparent;
                    overflow: hidden;
                    font-family: sans-serif;
                }
                #captcha-container {
                    min-height: 96px;
                    width: 100%;
                    height: 100%;
                }
            </style>
            <script
                src="https://smartcaptcha.cloud.yandex.ru/captcha.js?render=onload&amp;onload=smartbotOnCaptchaReady"
                defer
            ></script>
        </head>
        <body>
            <div id="captcha-container"></div>
            <script>
                function notifyToken(token) {
                    const safeToken = token || '';

                    if (window.SmartbotBridge && typeof window.SmartbotBridge.onTokenChanged === 'function') {
                        window.SmartbotBridge.onTokenChanged(safeToken);
                    }

                    const iosHandler = window.webkit &&
                        window.webkit.messageHandlers &&
                        window.webkit.messageHandlers.smartbotToken;

                    if (iosHandler && typeof iosHandler.postMessage === 'function') {
                        iosHandler.postMessage(safeToken);
                    }
                }

                function clearToken() {
                    notifyToken('');
                }

                function smartbotOnCaptchaReady() {
                    window.smartCaptcha.render('captcha-container', {
                        sitekey: '$safeSiteKey',
                        webview: true,
                        hl: '$safeLanguageCode',
                        callback: function(token) {
                            notifyToken(token);
                        },
                        'expired-callback': clearToken,
                        'challenge-hidden-callback': clearToken
                    });
                }
            </script>
        </body>
        </html>
    """.trimIndent()
}
