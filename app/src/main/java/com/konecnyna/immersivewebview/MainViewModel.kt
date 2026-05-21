package com.konecnyna.immersivewebview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CacheMode(val label: String) {
    DEFAULT("Default"),
    CACHE_FIRST("Cache First"),
    NO_CACHE("No Cache"),
    CACHE_ONLY("Cache Only"),
}

class MainViewModel : ViewModel() {
    private val _url = MutableStateFlow("https://www.google.com")
    val url: StateFlow<String> = _url.asStateFlow()

    private val _reloadTrigger = MutableStateFlow(0)
    val reloadTrigger: StateFlow<Int> = _reloadTrigger.asStateFlow()

    private val _cacheMode = MutableStateFlow(CacheMode.DEFAULT)
    val cacheMode: StateFlow<CacheMode> = _cacheMode.asStateFlow()

    private val _clearCacheTrigger = MutableStateFlow(0)
    val clearCacheTrigger: StateFlow<Int> = _clearCacheTrigger.asStateFlow()

    fun updateUrl(newUrl: String) {
        val formatted = if (newUrl.startsWith("http://") || newUrl.startsWith("https://")) {
            newUrl
        } else {
            "https://$newUrl"
        }
        _url.value = formatted
    }

    fun reload() {
        _reloadTrigger.value++
    }

    fun setCacheMode(mode: CacheMode) {
        _cacheMode.value = mode
    }

    fun clearCache() {
        _clearCacheTrigger.value++
    }
}
