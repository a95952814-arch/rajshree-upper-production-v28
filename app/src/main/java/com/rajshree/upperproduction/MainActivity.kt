package com.rajshree.upperproduction

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url ?: return false
                val scheme = url.scheme ?: return false
                if (scheme == "https" || scheme == "http") {
                    if (url.host == "wa.me" || url.host?.endsWith("whatsapp.com") == true) {
                        return openExternal(url)
                    }
                    if (url.host == null) return false
                    return false
                }
                return openExternal(url)
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url.isNullOrBlank()) return false
                val uri = Uri.parse(url)
                if (uri.scheme == "https" || uri.scheme == "http") {
                    if (uri.host == "wa.me" || uri.host?.endsWith("whatsapp.com") == true) {
                        return openExternal(uri)
                    }
                    return false
                }
                return openExternal(uri)
            }
        }

        setContentView(webView)
        webView.loadUrl("file:///android_asset/index.html")
    }

    private fun openExternal(uri: Uri): Boolean {
        return try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            true
        } catch (_: Exception) {
            Toast.makeText(this, "हा link उघडता आला नाही. WhatsApp install आहे का ते तपासा.", Toast.LENGTH_LONG).show()
            true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
