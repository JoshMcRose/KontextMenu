package io.github.joshmcrose.theme

import androidx.compose.runtime.*
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme
import com.jthemedetecor.OsThemeDetector
import kotlin.apply

@Composable
fun rememberDarkTheme(): Boolean {
    var darkTheme by remember { mutableStateOf(currentSystemTheme == SystemTheme.DARK) }

    DisposableEffect(Unit) {
        val darkThemeListener: (Boolean) -> Unit = { darkTheme = it }
        val detector = OsThemeDetector.getDetector().apply { registerListener(darkThemeListener) }
        onDispose { detector.removeListener(darkThemeListener) }
    }

    return darkTheme
}