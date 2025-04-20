package io.github.joshmcrose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement.Floating
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.joshmcrose.custom.CustomContextMenu
import io.github.joshmcrose.theme.AppTheme
import io.github.joshmcrose.theme.rememberDarkTheme
import java.awt.Dimension
import java.awt.Toolkit.getDefaultToolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.ByteArrayInputStream
import java.io.InputStream

fun main() {
    application {
        val windowParams = DpSize(600.dp, 400.dp)
        val windowState = rememberWindowState(size = windowParams, placement = Floating)
        val isDarkTheme = rememberDarkTheme()

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Kontext Menu Demo"
        ) {
            window.minimumSize = Dimension(600.dp.value.toInt(), 400.dp.value.toInt())

            AppTheme(isDarkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize(),) {
                    var selectedImplementation by remember { mutableStateOf(Implementations.M3) }
                    var text by remember { mutableStateOf(TextFieldValue()) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(24.dp))

                        EditorChoices(
                            selected = selectedImplementation,
                            onSelectedChange = { selectedImplementation = it },
                            modifier = Modifier.width(IntrinsicSize.Max)
                        )

                        Spacer(Modifier.height(36.dp))

                        Menu(
                            implementations = selectedImplementation,
                            cut = if (text.selection.collapsed) null else {
                                {
                                    text.getSelectedText().text.copyToClipboard()
                                    text = text.copy(
                                        text = text.getTextBeforeSelection(text.selection.min).text + text.getTextAfterSelection(text.text.length).text,
                                        selection = TextRange(text.selection.min)
                                    )
                                }
                            },
                            copy = if (text.selection.collapsed) null else {
                                { text.getSelectedText().text.copyToClipboard() }
                            },
                            paste = {
                                val before = text.getTextBeforeSelection(text.selection.min).text
                                val after = text.getTextAfterSelection(text.text.length).text
                                val newText = pasteHTML()
                                text = text.copy(
                                    text = before + newText + after,
                                    selection = TextRange(text.selection.min + newText.length)
                                )
                            },
                            selectAll = { text = text.copy(selection = TextRange(0, text.text.length)) }
                        ) {
                            OutlinedTextField(
                                value = text,
                                onValueChange = { text = it },
                                textStyle = typography.bodyLarge,
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                shape = shapes.extraSmall,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = colorScheme.onSurface,
                                    cursorColor = colorScheme.primary,
                                    unfocusedBorderColor = colorScheme.outline,
                                    focusedBorderColor = colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Menu(
    implementations: Implementations,
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?,
    selectAll: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    when (implementations) {
        Implementations.M3 -> {
            foundation.material3.ContextMenu(
                cut = cut,
                copy = copy,
                paste = paste,
                content = content
            )
        }
        Implementations.M2 -> {
            foundation.material.ContextMenu(
                cut = cut,
                copy = copy,
                paste = paste,
                content = content
            )
        }
        Implementations.M3Dialog -> {
            foundation.material3.DialogContextMenu(
                cut = cut,
                copy = copy,
                paste = paste,
                content = content
            )
        }
        Implementations.Custom -> {
            CustomContextMenu(
                cut = cut,
                copy = copy,
                paste = paste,
                selectAll = selectAll,
                content = content
            )
        }
    }
}

@Composable
fun EditorChoices(
    selected: Implementations,
    onSelectedChange: (Implementations) -> Unit,
    modifier: Modifier = Modifier
) {
    val implementations = Implementations.entries

    Surface(
        shape = shapes.extraLarge,
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, colorScheme.outline),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .height(40.dp)
                .background(colorScheme.surface)
                .clip(shape = shapes.extraLarge),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            implementations.forEach { implementation ->
                Text(
                    text = implementation.full,
                    style = typography.labelLarge,
                    color = if (implementation == selected) colorScheme.onSecondaryContainer else colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable { onSelectedChange(implementation) }
                        .background(
                            if (implementation == selected) colorScheme.secondaryContainer else colorScheme.surface
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

fun String.copyToClipboard() {
    val transferable = object : Transferable {
        override fun getTransferDataFlavors(): Array<DataFlavor> = arrayOf(DataFlavor("text/plain"))
        override fun isDataFlavorSupported(flavor: DataFlavor): Boolean = transferDataFlavors.any { flavor == it }
        override fun getTransferData(flavor: DataFlavor): Any = ByteArrayInputStream(toByteArray())
    }

    val clipboard = getDefaultToolkit().systemClipboard
    clipboard.setContents(transferable, null)
}

fun pasteHTML(): String =
    (getDefaultToolkit().systemClipboard.getData(DataFlavor("text/plain")) as InputStream)
        .bufferedReader()
        .readText()
