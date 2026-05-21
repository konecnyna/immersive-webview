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
}
