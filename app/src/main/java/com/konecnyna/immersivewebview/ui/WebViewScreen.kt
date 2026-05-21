package com.konecnyna.immersivewebview.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.konecnyna.immersivewebview.CacheMode
import com.konecnyna.immersivewebview.MainViewModel

@Composable
fun WebViewScreen(viewModel: MainViewModel) {
    val url by viewModel.url.collectAsStateWithLifecycle()
    val reloadTrigger by viewModel.reloadTrigger.collectAsStateWithLifecycle()
    val cacheMode by viewModel.cacheMode.collectAsStateWithLifecycle()
    val clearCacheTrigger by viewModel.clearCacheTrigger.collectAsStateWithLifecycle()
    val immersiveMode by viewModel.immersiveMode.collectAsStateWithLifecycle()
    var showUrlDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var progress by remember { mutableIntStateOf(0) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, webView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> webView?.onPause()
                Lifecycle.Event.ON_RESUME -> webView?.onResume()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.testTag("bottom_bar"),
                tonalElevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = { webView?.goBack() },
                        enabled = canGoBack,
                        modifier = Modifier.testTag("btn_back"),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(
                        onClick = { webView?.goForward() },
                        enabled = canGoForward,
                        modifier = Modifier.testTag("btn_forward"),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Forward")
                    }
                    IconButton(
                        onClick = { viewModel.reload() },
                        modifier = Modifier.testTag("btn_reload"),
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Reload")
                    }
                    Box {
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.testTag("btn_menu"),
                        ) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.testTag("overflow_menu"),
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit URL") },
                                onClick = {
                                    showMenu = false
                                    showUrlDialog = true
                                },
                                modifier = Modifier.testTag("menu_edit_url"),
                            )
                            DropdownMenuItem(
                                text = { Text("Clear Cache") },
                                onClick = {
                                    showMenu = false
                                    viewModel.clearCache()
                                },
                                modifier = Modifier.testTag("menu_clear_cache"),
                            )
                            DropdownMenuItem(
                                text = { Text("Immersive (under status bar)") },
                                onClick = {
                                    viewModel.toggleImmersiveMode()
                                    showMenu = false
                                },
                                trailingIcon = {
                                    if (immersiveMode) {
                                        Icon(Icons.Filled.Check, contentDescription = "Enabled")
                                    }
                                },
                                modifier = Modifier.testTag("menu_immersive_toggle"),
                            )
                            HorizontalDivider()
                            CacheMode.entries.forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.label) },
                                    onClick = {
                                        viewModel.setCacheMode(mode)
                                        showMenu = false
                                    },
                                    leadingIcon = {
                                        RadioButton(
                                            selected = cacheMode == mode,
                                            onClick = null,
                                        )
                                    },
                                    modifier = Modifier.testTag("menu_cache_${mode.name}"),
                                )
                            }
                        }
                    }
                }
            }
        },
    ) { innerPadding ->
        val topPadding = if (immersiveMode) {
            0.dp
        } else {
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding,
                    bottom = innerPadding.calculateBottomPadding(),
                ),
        ) {
            @SuppressLint("SetJavaScriptEnabled")
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("webview"),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.databaseEnabled = true
                        settings.cacheMode = WebSettings.LOAD_DEFAULT
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?,
                            ): Boolean = false

                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                canGoBack = view?.canGoBack() ?: false
                                canGoForward = view?.canGoForward() ?: false
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                canGoBack = view?.canGoBack() ?: false
                                canGoForward = view?.canGoForward() ?: false
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                progress = newProgress
                            }
                        }

                        webView = this
                    }
                },
                update = { view ->
                    view.settings.cacheMode = cacheMode.toWebSettingsValue()
                },
            )

            if (progress < 100) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .testTag("progress_bar"),
                )
            }
        }
    }

    LaunchedEffect(url) {
        webView?.loadUrl(url)
    }

    LaunchedEffect(reloadTrigger) {
        if (reloadTrigger > 0) {
            webView?.reload()
        }
    }

    LaunchedEffect(clearCacheTrigger) {
        if (clearCacheTrigger > 0) {
            webView?.clearCache(true)
            webView?.clearHistory()
        }
    }

    if (showUrlDialog) {
        UrlDialog(
            currentUrl = url,
            onDismiss = { showUrlDialog = false },
            onConfirm = { newUrl ->
                viewModel.updateUrl(newUrl)
                showUrlDialog = false
            },
        )
    }
}

private fun CacheMode.toWebSettingsValue(): Int = when (this) {
    CacheMode.DEFAULT -> WebSettings.LOAD_DEFAULT
    CacheMode.CACHE_FIRST -> WebSettings.LOAD_CACHE_ELSE_NETWORK
    CacheMode.NO_CACHE -> WebSettings.LOAD_NO_CACHE
    CacheMode.CACHE_ONLY -> WebSettings.LOAD_CACHE_ONLY
}

@Composable
fun UrlDialog(
    currentUrl: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var text by remember { mutableStateOf(currentUrl) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("url_dialog"),
        title = { Text("Enter URL") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("URL") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("url_input"),
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) },
                modifier = Modifier.testTag("btn_confirm_url"),
            ) {
                Text("Go")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_dismiss_url"),
            ) {
                Text("Cancel")
            }
        },
    )
}
