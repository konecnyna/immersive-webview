package com.konecnyna.immersivewebview

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.konecnyna.immersivewebview.ui.WebViewScreen
import com.konecnyna.immersivewebview.ui.theme.ImmersiveWebViewTheme
import org.junit.Rule
import org.junit.Test

class WebViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun bottomBarIsDisplayed() {
        composeTestRule.setContent {
            ImmersiveWebViewTheme {
                WebViewScreen(viewModel = MainViewModel())
            }
        }
        composeTestRule.onNodeWithTag("bottom_bar").assertIsDisplayed()
    }

    @Test
    fun allNavigationButtonsExist() {
        composeTestRule.setContent {
            ImmersiveWebViewTheme {
                WebViewScreen(viewModel = MainViewModel())
            }
        }
        composeTestRule.onNodeWithTag("btn_back").assertExists()
        composeTestRule.onNodeWithTag("btn_forward").assertExists()
        composeTestRule.onNodeWithTag("btn_reload").assertExists()
        composeTestRule.onNodeWithTag("btn_url").assertExists()
    }

    @Test
    fun urlDialogOpensOnEditClick() {
        composeTestRule.setContent {
            ImmersiveWebViewTheme {
                WebViewScreen(viewModel = MainViewModel())
            }
        }
        composeTestRule.onNodeWithTag("btn_url").performClick()
        composeTestRule.onNodeWithTag("url_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("url_input").assertExists()
    }

    @Test
    fun urlDialogDismissesOnCancel() {
        composeTestRule.setContent {
            ImmersiveWebViewTheme {
                WebViewScreen(viewModel = MainViewModel())
            }
        }
        composeTestRule.onNodeWithTag("btn_url").performClick()
        composeTestRule.onNodeWithTag("url_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("btn_dismiss_url").performClick()
        composeTestRule.onNodeWithTag("url_dialog").assertDoesNotExist()
    }
}
