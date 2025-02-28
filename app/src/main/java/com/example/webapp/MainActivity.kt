package com.example.webapp

import android.annotation.SuppressLint

import android.os.Bundle

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var openWebButton: Button
    private lateinit var introTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var webView: WebView
    private lateinit var webBridge: WebBridgeInterface


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openWebButton = findViewById<Button>(R.id.openWebButton)
        nameTextView = findViewById(R.id.nameTextView)
        introTextView = findViewById(R.id.introTextView)
        imageView = findViewById(R.id.imageView)
        webView = findViewById(R.id.webView)


        setupWebView()
        webView.clearCache(true)
        webView.clearHistory()
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE



        openWebButton.setOnClickListener {
            //webView.loadUrl("https://nameform.netlify.app/")
            webView.loadUrl("http://192.168.160.84:8000")
            webView.visibility = WebView.VISIBLE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webBridge = WebBridgeInterface(this, ::onImageCaptured, nameTextView, introTextView, imageView)
        webView.addJavascriptInterface(webBridge, "Android")

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.let {
                    Log.d("WebViewConsole", "${it.message()} -- From line ${it.lineNumber()} of ${it.sourceId()}")
                }
                return true
            }
        }

        WebView.setWebContentsDebuggingEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        webBridge.handleCameraResult(requestCode, resultCode, data)
    }

    private fun onImageCaptured(base64Image: String) {
        webView.post {
            webView.evaluateJavascript("javascript:receiveImageFromAndroid('$base64Image')", null)
        }
    }


}