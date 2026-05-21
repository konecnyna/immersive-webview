package com.konecnyna.immersivewebview

import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun initialUrlIsGoogle() {
        val viewModel = MainViewModel()
        assertEquals("https://www.google.com", viewModel.url.value)
    }

    @Test
    fun updateUrlWithHttpsPrefixKeepsIt() {
        val viewModel = MainViewModel()
        viewModel.updateUrl("https://example.com")
        assertEquals("https://example.com", viewModel.url.value)
    }

    @Test
    fun updateUrlWithHttpPrefixKeepsIt() {
        val viewModel = MainViewModel()
        viewModel.updateUrl("http://example.com")
        assertEquals("http://example.com", viewModel.url.value)
    }

    @Test
    fun updateUrlWithoutPrefixAddsHttps() {
        val viewModel = MainViewModel()
        viewModel.updateUrl("example.com")
        assertEquals("https://example.com", viewModel.url.value)
    }

    @Test
    fun reloadIncrementsTrigger() {
        val viewModel = MainViewModel()
        assertEquals(0, viewModel.reloadTrigger.value)
        viewModel.reload()
        assertEquals(1, viewModel.reloadTrigger.value)
        viewModel.reload()
        assertEquals(2, viewModel.reloadTrigger.value)
    }

    @Test
    fun initialCacheModeIsDefault() {
        val viewModel = MainViewModel()
        assertEquals(CacheMode.DEFAULT, viewModel.cacheMode.value)
    }

    @Test
    fun setCacheModeUpdatesState() {
        val viewModel = MainViewModel()
        viewModel.setCacheMode(CacheMode.NO_CACHE)
        assertEquals(CacheMode.NO_CACHE, viewModel.cacheMode.value)
        viewModel.setCacheMode(CacheMode.CACHE_FIRST)
        assertEquals(CacheMode.CACHE_FIRST, viewModel.cacheMode.value)
        viewModel.setCacheMode(CacheMode.CACHE_ONLY)
        assertEquals(CacheMode.CACHE_ONLY, viewModel.cacheMode.value)
    }

    @Test
    fun clearCacheIncrementsTrigger() {
        val viewModel = MainViewModel()
        assertEquals(0, viewModel.clearCacheTrigger.value)
        viewModel.clearCache()
        assertEquals(1, viewModel.clearCacheTrigger.value)
        viewModel.clearCache()
        assertEquals(2, viewModel.clearCacheTrigger.value)
    }

    @Test
    fun httpCleartextUrlIsPreserved() {
        val viewModel = MainViewModel()
        viewModel.updateUrl("http://10.0.0.1:8080")
        assertEquals("http://10.0.0.1:8080", viewModel.url.value)
    }

    @Test
    fun immersiveModeDefaultsOff() {
        val viewModel = MainViewModel()
        assertEquals(false, viewModel.immersiveMode.value)
    }

    @Test
    fun toggleImmersiveModeFlipsState() {
        val viewModel = MainViewModel()
        assertEquals(false, viewModel.immersiveMode.value)
        viewModel.toggleImmersiveMode()
        assertEquals(true, viewModel.immersiveMode.value)
        viewModel.toggleImmersiveMode()
        assertEquals(false, viewModel.immersiveMode.value)
    }
}
