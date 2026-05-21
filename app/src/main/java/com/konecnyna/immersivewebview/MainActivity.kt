package com.konecnyna.immersivewebview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.konecnyna.immersivewebview.ui.WebViewScreen
import com.konecnyna.immersivewebview.ui.theme.ImmersiveWebViewTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImmersiveWebViewTheme {
                WebViewScreen(viewModel = viewModel)
            }
        }
    }
}
