package foundation.material

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import foundation.TextContextMenu
import foundation.TextContextMenuArea
import foundation.composable.ContextMenuBody
import foundation.representation.model.defaultTextContextMenuItems
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * A Material implementation of a popup-based context menu.
 *
 * This composable function creates a context menu using a popup with Material styling.
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
                    containerColor = colors.surface,
                    itemHoverColor = colors.primary.copy(alpha = .04F),
                    labelColor = colors.onSurface,
                    shortcutColor = colors.onSurface,
                    height = 48.dp,
                    verticalPadding = 8.dp,
                    horizontalPadding = 24.dp,
                    internalSpacing = 20.dp,
                    shape = RectangleShape,
                    onDismissRequest = onDismissRequest,
                    icon = { drawable, enabled -> TextContextLeadingIcon(drawable, enabled) },
                    text = { string, enabled, _ -> ContextMenuText(string, enabled) }
                )
            },
            builder = { defaultTextContextMenuItems(cut, copy, paste).addAll() }
        )
    )
}

/**
 * A composable function that creates an icon for a context menu item.
 *
 * This function creates an icon with Material styling, with different
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
        tint = if (enabled) colors.primary.copy(alpha = .54f) else colors.primary.copy(alpha = .38f),
        modifier = Modifier.size(24.dp)
    )
}

/**
 * A composable function that creates text for a context menu item.
 *
 * This function creates text with Material styling, with different
 * appearance based on whether the menu item is enabled.
 *
 * @param label The text to display
 * @param enabled Whether the menu item is enabled
 */
@Composable
fun ContextMenuText(label: String, enabled: Boolean) {
    Text(
        text = label,
        color = if (enabled) colors.onSurface else colors.onSurface.copy(alpha = 0.38F),
        style = typography.body1
    )
}
