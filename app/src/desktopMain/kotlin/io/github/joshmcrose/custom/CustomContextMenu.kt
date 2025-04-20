package io.github.joshmcrose.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.onClick
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import foundation.TextContextMenu
import foundation.TextContextMenuArea
import foundation.composable.onHover
import foundation.material3.ContextMenuBody
import foundation.material3.ContextMenuText
import foundation.material3.TextContextIcon
import foundation.representation.model.DefaultTextContextMenuItem
import kontextmenu.app.generated.resources.Res
import kontextmenu.app.generated.resources.arrow_forward
import kontextmenu.app.generated.resources.content_copy
import kontextmenu.app.generated.resources.content_cut
import kontextmenu.app.generated.resources.content_paste
import kontextmenu.app.generated.resources.copy
import kontextmenu.app.generated.resources.cut
import kontextmenu.app.generated.resources.paste
import kontextmenu.app.generated.resources.select_all
import org.jetbrains.compose.resources.stringResource
import java.awt.SystemColor.text

@Composable
fun CustomContextMenu(
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?,
    selectAll: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    TextContextMenu(
        content = content,
        textContextMenuArea = TextContextMenuArea(
            menuContent = { items, onDismissRequest ->
                ContextMenuBody {  // ContextMenuBody is a pre-made container designed to Material 3 specifications.
                    items.forEach { item ->
                        MenuRow(item) {
                            item.onClick?.let {
                                onDismissRequest?.invoke()
                                it()
                            }
                        }
                    }
                }
            },
            builder = { // Use this lambda to define the menu items that will appear in your context menu.
                MenuItem(
                    leadingIcon = Res.drawable.content_cut,
                    trailingIcon = Res.drawable.arrow_forward,
                    label = Res.string.cut,
                    enabled = cut != null,
                    onClick = cut
                ).add() // Use the `add()` builder function to add the menu item to the list

                MenuItem(
                    leadingIcon = Res.drawable.content_copy,
                    trailingIcon = Res.drawable.arrow_forward,
                    label = Res.string.copy,
                    enabled = copy != null,
                    onClick = copy
                ).add()

                MenuItem(
                    leadingIcon = Res.drawable.content_paste,
                    trailingIcon = Res.drawable.arrow_forward,
                    label = Res.string.paste,
                    enabled = paste != null,
                    onClick = paste
                ).add()

                MenuItem(
                    leadingIcon = Res.drawable.select_all,
                    trailingIcon = Res.drawable.arrow_forward,
                    label = Res.string.select_all,
                    enabled = true,
                    onClick = selectAll
                ).add()
            }
        )
    )
}

@Composable
fun MenuRow(
    item: MenuItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .clickable(enabled = item.enabled, onClick = onClick)
            .onHover { hovered = it }
            .background(if (hovered && item.enabled) colorScheme.onSurface.copy(alpha = .08F) else colorScheme.surfaceContainer)
            .fillMaxWidth()
            .widthIn(min = 112.dp, max = 280.dp)
            .height(32.dp)
            .padding(PaddingValues(horizontal = 12.dp, vertical = 0.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
    ) {
        item.leadingIcon?.let { TextContextIcon(res = it, enabled = item.enabled) }
        MenuItemInternal(item)
    }
}

@Composable
private fun MenuItemInternal(item: MenuItem) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // TextContextIcon and ContextMenuText are convenience functions for adding icons and text to a menu item using Material 3 styling.
        ContextMenuText(stringResource(item.label), item.enabled, colorScheme.onSurface)

        Spacer(Modifier.width(36.dp))

        item.trailingIcon?.let { TextContextIcon(res = it, enabled = item.enabled) }
    }
}