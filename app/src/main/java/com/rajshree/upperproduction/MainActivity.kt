package com.rajshree.upperproduction

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.JavascriptInterface
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
        webView.addJavascriptInterface(BackupBridge(), "AndroidBackup")

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


    inner class BackupBridge {
        @JavascriptInterface
        fun saveBackup(content: String) {
            try {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, "upper-production-backup.json")
                    put(MediaStore.Downloads.MIME_TYPE, "application/json")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        put(MediaStore.Downloads.IS_PENDING, 1)
                    }
                }
                val resolver = contentResolver
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: throw Exception("Could not create backup file")
                resolver.openOutputStream(uri)?.use { it.write(content.toByteArray(Charsets.UTF_8)) }
                    ?: throw Exception("Could not write backup file")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val done = ContentValues().apply { put(MediaStore.Downloads.IS_PENDING, 0) }
                    resolver.update(uri, done, null, null)
                }
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Backup Downloads à¤«à¥‹à¤²à¥à¤¡à¤°à¤®à¤§à¥à¤¯à¥‡ à¤¸à¥‡à¤µà¥à¤¹ à¤à¤¾à¤²à¤¾.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Backup à¤¸à¥‡à¤µà¥à¤¹ à¤à¤¾à¤²à¤¾ à¤¨à¤¾à¤¹à¥€: " + (e.message ?: ""), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openExternal(uri: Uri): Boolean {
        return try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            true
        } catch (_: Exception) {
            Toast.makeText(this, "à¤¹à¤¾ link à¤‰à¤˜à¤¡à¤¤à¤¾ à¤†à¤²à¤¾ à¤¨à¤¾à¤¹à¥€. WhatsApp install à¤†à¤¹à¥‡ à¤•à¤¾ à¤¤à¥‡ à¤¤à¤ªà¤¾à¤¸à¤¾.", Toast.LENGTH_LONG).show()
            true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
