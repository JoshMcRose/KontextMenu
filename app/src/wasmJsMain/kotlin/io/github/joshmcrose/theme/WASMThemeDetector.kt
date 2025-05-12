package io.github.joshmcrose.theme

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme

class WASMThemeDetector(var listen: Boolean = true) {
    val scope = CoroutineScope(Dispatchers.Default)

    private var _themeState = MutableStateFlow(false)
    val themeState = _themeState.asStateFlow()

    init {
        scope.launch {
            while (listen) {
                _themeState.update { currentSystemTheme == SystemTheme.DARK }
                delay(500L)
            }
        }
    }

    fun stopListening() { listen = false }
}