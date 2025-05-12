package io.github.joshmcrose

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.github.joshmcrose.theme.AppTheme
import io.github.joshmcrose.theme.rememberDarkTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = "Compose KontextMenu") {
        val isDarkTheme = rememberDarkTheme()

        AppTheme(isDarkTheme = isDarkTheme) {
            App()
        }
    }
}