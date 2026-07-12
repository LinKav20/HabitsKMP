package com.github.linkav20.habits.core.uikit.components.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import com.github.linkav20.habits.core.uikit.HabitTheme
import com.multiplatform.webview.request.RequestInterceptor
import com.multiplatform.webview.request.WebRequest
import com.multiplatform.webview.request.WebRequestInterceptResult
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

@Composable
fun TemplateHtmlWebView(
    content: String,
    isExplicitHeightForWebViewRequired: Boolean,
    modifier: Modifier = Modifier,
    textColor: Color? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        val density = LocalDensity.current
        val uriHandler = LocalUriHandler.current
        var webViewHeight by remember { mutableStateOf(200.dp) }
        val navigator = rememberWebViewNavigator(
            requestInterceptor = TemplateRequestInterceptor(uriHandler),
        )
        val htmlTextColor = textColor ?: HabitTheme.colors.textPrimary
        val htmlData = remember(content, htmlTextColor) {
            buildHtml(content, htmlTextColor)
        }
        val state = rememberWebViewStateWithHTMLData(htmlData).apply {
            webSettings.apply {
                isJavaScriptEnabled = true
                androidWebSettings.loadsImagesAutomatically = true
                this.backgroundColor = HabitTheme.colors.surfaceContainer
                iOSWebSettings.showVerticalScrollIndicator = false
                iOSWebSettings.scrollEnabled = false
                iOSWebSettings.bounces = false
            }
        }
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isExplicitHeightForWebViewRequired) {
                        Modifier.height(webViewHeight)
                    } else {
                        Modifier.wrapContentHeight()
                    },
                ),
            navigator = navigator,
        )

        if (state.loadingState is LoadingState.Finished && isExplicitHeightForWebViewRequired) {
            navigator.evaluateJavaScript("document.body.scrollHeight") { result ->
                result.toFloatOrNull()?.let { px ->
                    webViewHeight = with(density) { px.dp }
                }
            }
        }
    }
}

private fun buildHtml(
    content: String,
    textColor: Color,
): String =
    """
        <html>
            <body style="color: ${textColor.toHtmlColor()};">
                $content
            </body>
        </html>
    """.trimIndent()

private fun Color.toHtmlColor(): String {
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    return "rgba($r, $g, $b, $alpha)"
}

class TemplateRequestInterceptor(
    private val uriHandler: UriHandler,
) : RequestInterceptor {
    override fun onInterceptUrlRequest(
        request: WebRequest,
        navigator: WebViewNavigator,
    ): WebRequestInterceptResult {
        return when {
            request.url.startsWith("about:blank") ||
                    request.url.startsWith("data:") -> {
                WebRequestInterceptResult.Allow
            }
            else -> {
                uriHandler.openUri(request.url)
                WebRequestInterceptResult.Reject
            }
        }
    }
}
