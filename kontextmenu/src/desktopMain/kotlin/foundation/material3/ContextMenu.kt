package foundation.material3

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import foundation.TextContextMenu
import foundation.TextContextMenuArea
import foundation.composable.ContextMenuBody
import foundation.representation.model.defaultTextContextMenuItems
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * A Material 3 implementation of a popup-based context menu.
 *
 * This composable function creates a context menu using a popup with Material 3 styling.
 * It provides cut, copy, and paste functionality with a simple popup interface.
 *
 * @param cut Optional callback for the cut operation
 * @param copy Optional callback for the copy operation
 * @param paste Optional callback for the paste operation
 * @param content The content to display in the context menu area
 */
@Suppress("unused")
@Composable
fun ContextMenu(
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    TextContextMenu(
        content = content,
        textContextMenuArea = TextContextMenuArea(
            menuContent = { items, onDismissRequest ->
                ContextMenuBody(
                    items = items,
                    containerColor = colorScheme.surfaceContainer,
                    itemHoverColor = colorScheme.onSurface.copy(alpha = .08F),
                    labelColor = colorScheme.onSurface,
                    shortcutColor = colorScheme.onSurfaceVariant,
                    height = 32.dp,
                    verticalPadding = 4.dp,
                    horizontalPadding = 12.dp,
                    internalSpacing = 12.dp,
                    shape = shapes.extraSmall,
                    onDismissRequest = onDismissRequest,
                    icon = { drawable, enabled -> TextContextLeadingIcon(drawable, enabled) },
                    text = { string, enabled, color -> ContextMenuText(string, enabled, color) }
                )
            },
            builder = { defaultTextContextMenuItems(cut, copy, paste).addAll() }
        )
    )
}

/**
 * A composable function that creates an icon for a context menu item.
 *
 * This function creates an icon with Material 3 styling, with different
 * appearance based on whether the menu item is enabled.
 *
 * @param res The drawable resource for the icon
 * @param enabled Whether the menu item is enabled
 */
@Composable
fun TextContextLeadingIcon(res: DrawableResource, enabled: Boolean) {
    Icon(
        painter = painterResource(res),
        contentDescription = null,
        tint = if (enabled) colorScheme.onSurfaceVariant else colorScheme.onSurface.copy(alpha = 0.38F),
        modifier = Modifier.size(20.dp)
    )
}

/**
 * A composable function that creates text for a context menu item.
 *
 * This function creates text with Material 3 styling, with different
 * appearance based on whether the menu item is enabled.
 *
 * @param label The text to display
 * @param enabled Whether the menu item is enabled
 * @param color The color to use for the text when enabled
 */
@Composable
fun ContextMenuText(label: String, enabled: Boolean, color: Color) {
    Text(
        text = label,
        color = if (enabled) color else colorScheme.onSurface.copy(alpha = 0.38F),
        style = typography.labelLarge
    )
}
