package io.github.joshmcrose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import java.awt.Toolkit.getDefaultToolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.ByteArrayInputStream
import java.io.InputStream

actual fun String.copyToClipboard() {
    val transferable = object : Transferable {
        override fun getTransferDataFlavors(): Array<DataFlavor> = arrayOf(DataFlavor("text/plain"))
        override fun isDataFlavorSupported(flavor: DataFlavor): Boolean = transferDataFlavors.any { flavor == it }
        override fun getTransferData(flavor: DataFlavor): Any = ByteArrayInputStream(toByteArray())
    }

    val clipboard = getDefaultToolkit().systemClipboard
    clipboard.setContents(transferable, null)
}

actual fun pasteHTML(): String =
    (getDefaultToolkit().systemClipboard.getData(DataFlavor("text/plain")) as InputStream)
        .bufferedReader()
        .readText()

@Composable
@NonRestartableComposable
actual inline fun rememberClipboardEventsHandler(
    crossinline onPaste: (String) -> Unit,
    crossinline onCopy: () -> String?,
    crossinline onCut: () -> String?,
    isEnabled: Boolean
) { /* No-op */ }