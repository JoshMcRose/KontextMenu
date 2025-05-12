package io.github.joshmcrose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun rememberDarkTheme(): Boolean {
    val darkThemeDetector = WASMThemeDetector()
    val darkTheme by darkThemeDetector.themeState.collectAsState()

    DisposableEffect(Unit) {
        onDispose { darkThemeDetector.stopListening() }
    }

    return darkTheme
}
