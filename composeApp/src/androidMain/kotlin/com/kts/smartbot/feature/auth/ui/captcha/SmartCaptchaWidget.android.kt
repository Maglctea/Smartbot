package com.kts.smartbot.feature.auth.ui.captcha

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
internal actual fun PlatformSmartCaptchaWidget(
    languageCode: String,
    siteKey: String,
    captchaHost: String,
    onTokenChanged: (String) -> Unit,
    modifier: Modifier,
) {
    val latestTokenChanged = rememberUpdatedState(onTokenChanged)
    val bridge = remember { SmartCaptchaBridge() }
    val html = remember(languageCode, siteKey) {
        buildSmartCaptchaHtml(
            languageCode = languageCode,
            siteKey = siteKey,
        )
    }

    bridge.onTokenReceived = { token ->
        latestTokenChanged.value(token)
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            SmartCaptchaContainer(context).apply {
                bind(
                    bridge = bridge,
                    html = html,
                    captchaHost = captchaHost,
                    onTokenChanged = latestTokenChanged.value,
                )
            }
        },
        update = { container ->
            container.bind(
                bridge = bridge,
                html = html,
                captchaHost = captchaHost,
                onTokenChanged = latestTokenChanged.value,
            )
        },
    )
}

@SuppressLint("SetJavaScriptEnabled")
private class SmartCaptchaContainer(
    context: Context,
) : FrameLayout(context) {
    private val cookieManager = CookieManager.getInstance()
    private var popupWebView: WebView? = null
    private var isBridgeAttached = false

    private val mainWebView = createWebView(isPopup = false)

    init {
        cookieManager.setAcceptCookie(true)
        addView(
            mainWebView,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
            ),
        )
    }

    fun bind(
        bridge: SmartCaptchaBridge,
        html: String,
        captchaHost: String,
        onTokenChanged: (String) -> Unit,
    ) {
        bridge.onTokenReceived = onTokenChanged
        if (!isBridgeAttached) {
            mainWebView.addJavascriptInterface(bridge, "SmartbotBridge")
            isBridgeAttached = true
        }
        mainWebView.loadSmartCaptchaPage(
            html = html,
            captchaHost = captchaHost,
        )
    }

    private fun createWebView(isPopup: Boolean): WebView {
        return WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            setBackgroundColor(Color.TRANSPARENT)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(
                    view: WebView?,
                    url: String?,
                ) {
                    super.onPageFinished(view, url)
                    if (isPopup && url == "about:blank") {
                        closePopup()
                    }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    return super.onConsoleMessage(consoleMessage)
                }

                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?,
                ): Boolean {
                    val transport = resultMsg?.obj as? WebView.WebViewTransport ?: return false
                    closePopup()

                    val popup = createWebView(isPopup = true)
                    popupWebView = popup
                    addView(
                        popup,
                        LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT,
                        ),
                    )

                    transport.webView = popup
                    resultMsg.sendToTarget()
                    return true
                }

                override fun onCloseWindow(window: WebView?) {
                    closePopup()
                }
            }
        }
    }

    private fun closePopup() {
        popupWebView?.let { popup ->
            removeView(popup)
            popup.stopLoading()
            popup.destroy()
        }
        popupWebView = null
    }
}

private fun WebView.loadSmartCaptchaPage(
    html: String,
    captchaHost: String,
) {
    if (url == null) {
        loadDataWithBaseURL(
            "https://$captchaHost/",
            html,
            "text/html",
            "utf-8",
            null,
        )
    }
}

private class SmartCaptchaBridge {
    private val handler = Handler(Looper.getMainLooper())

    var onTokenReceived: (String) -> Unit = {}

    @JavascriptInterface
    fun onTokenChanged(token: String?) {
        handler.post {
            onTokenReceived(token.orEmpty())
        }
    }
}
