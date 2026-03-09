package com.kts.smartbot.feature.auth.ui.captcha

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun PlatformSmartCaptchaWidget(
    languageCode: String,
    siteKey: String,
    captchaHost: String,
    onTokenChanged: (String) -> Unit,
    modifier: Modifier,
) {
    val latestTokenChanged = rememberUpdatedState(onTokenChanged)
    val handler = remember { SmartCaptchaMessageHandler() }
    val html = remember(languageCode, siteKey) {
        buildSmartCaptchaHtml(
            languageCode = languageCode,
            siteKey = siteKey,
        )
    }

    handler.onTokenChanged = { token ->
        latestTokenChanged.value(token)
    }

    UIKitView(
        modifier = modifier,
        factory = {
            val userContentController = WKUserContentController().apply {
                addScriptMessageHandler(handler, "smartbotToken")
            }
            val configuration = WKWebViewConfiguration().apply {
                this.userContentController = userContentController
            }

            WKWebView(
                frame = CGRectZero.readValue(),
                configuration = configuration,
            ).apply {
                opaque = false
                backgroundColor = platform.UIKit.UIColor.clearColor
                scrollView.scrollEnabled = false
                loadHTMLString(
                    string = html,
                    baseURL = NSURL(string = "https://$captchaHost/"),
                )
            }
        },
        update = { webView ->
            handler.onTokenChanged = { token ->
                latestTokenChanged.value(token)
            }
            if (webView.URL == null) {
                webView.loadHTMLString(
                    string = html,
                    baseURL = NSURL(string = "https://$captchaHost/"),
                )
            }
        },
    )
}

@OptIn(ExperimentalForeignApi::class)
private class SmartCaptchaMessageHandler : NSObject(), WKScriptMessageHandlerProtocol {
    var onTokenChanged: (String) -> Unit = {}

    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage,
    ) {
        onTokenChanged(didReceiveScriptMessage.body as? String ?: "")
    }
}
