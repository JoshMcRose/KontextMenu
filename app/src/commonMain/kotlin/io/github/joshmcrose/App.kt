package io.github.joshmcrose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.unit.dp
import io.github.joshmcrose.custom.CustomContextMenu
import io.github.joshmcrose.material3.DialogKontextMenu
import io.github.joshmcrose.material3.KontextMenu
import io.github.joshmcrose.utils.copyToClipboard
import io.github.joshmcrose.utils.pasteHTML
import io.github.joshmcrose.utils.rememberClipboardEventsHandler

@Composable
fun App(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        var selectedImplementation by remember { mutableStateOf(Implementations.M3) }
        var text by remember { mutableStateOf(TextFieldValue()) }
        var newText: String? by remember { mutableStateOf(null) }

        rememberClipboardEventsHandler(
            onPaste = { newText = it },
            onCut = { text.getSelectedText().text },
            onCopy = { text.getSelectedText().text },
            isEnabled = true
        )

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
                    val pastedText = newText ?: pasteHTML()
                    text = text.copy(
                        text = before + pastedText + after,
                        selection = TextRange(text.selection.min + pastedText.length)
                    )
                    newText = null
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
            KontextMenu(
                cut = cut,
                copy = copy,
                paste = paste,
                content = content
            )
        }
        Implementations.M2 -> {
            io.github.joshmcrose.material.KontextMenu(
                cut = cut,
                copy = copy,
                paste = paste,
                content = content
            )
        }
        Implementations.M3Dialog -> {
            DialogKontextMenu(
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
