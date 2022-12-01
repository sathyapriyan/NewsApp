package com.hackernews.newsapp.screens.components

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun LoadWebUrl(
    url: String,
context: Context) {

    AndroidView(factory = {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
            webViewClient = WebViewClient()
            settings.apply {
                javaScriptEnabled = true
            }
            loadUrl(url)
        }
    })
}