package io.github.joshmcrose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable

expect fun String.copyToClipboard()

expect fun pasteHTML(): String

@Composable
@NonRestartableComposable
expect inline fun rememberClipboardEventsHandler(
    crossinline onPaste: (String) -> Unit,
    crossinline onCopy: () -> String?,
    crossinline onCut: () -> String?,
    isEnabled: Boolean
)