package com.konecnyna.immersivewebview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _url = MutableStateFlow("https://www.google.com")
    val url: StateFlow<String> = _url.asStateFlow()

    private val _reloadTrigger = MutableStateFlow(0)
    val reloadTrigger: StateFlow<Int> = _reloadTrigger.asStateFlow()

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
}
