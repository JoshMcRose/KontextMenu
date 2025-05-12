package io.github.joshmcrose

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement.Floating
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.joshmcrose.theme.AppTheme
import io.github.joshmcrose.theme.rememberDarkTheme
import java.awt.Dimension

fun main() {
    application {
        val windowParams = DpSize(800.dp, 600.dp)
        val windowState = rememberWindowState(size = windowParams, placement = Floating)
        val isDarkTheme = rememberDarkTheme()

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "KontextMenu Demo"
        ) {
            window.minimumSize = Dimension(600.dp.value.toInt(), 400.dp.value.toInt())

            AppTheme(isDarkTheme = isDarkTheme) {
                App()
            }
        }
    }
}
