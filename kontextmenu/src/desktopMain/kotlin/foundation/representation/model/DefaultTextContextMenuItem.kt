package foundation.representation.model

import androidx.compose.ui.input.key.Key
import foundation.TextContextMenuArea.Builder
import kontextmenu.kontextmenu.generated.resources.Res
import kontextmenu.kontextmenu.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import ui.input.key.ContextMenuKeyShortcut

/**
 * Data class representing an item in a text context menu.
 *
 * This class contains default properties to display and handle a menu item
 * in a text context menu.
 *
 * @property leadingIcon Optional icon to display before the label
 * @property label The text to display for this menu item
 * @property shortcut Optional keyboard shortcut associated with this menu item
 * @property enabled Whether this menu item is enabled
 * @property onClick Optional callback to invoke when this menu item is clicked
 */
data class DefaultTextContextMenuItem(
    val leadingIcon: DrawableResource?,
    val label: StringResource,
    val shortcut: ContextMenuKeyShortcut?,
    val enabled: Boolean,
    val onClick: (() -> Unit)?
)

/**
 * Creates a list of default text context menu items for common operations.
 *
 * This function creates a list of menu items for cut, copy, and paste operations,
 * with appropriate icons, labels, and keyboard shortcuts. The menu items are enabled
 * only if the corresponding operation is provided.
 *
 * @param cut Optional callback for the cut operation
 * @param copy Optional callback for the copy operation
 * @param paste Optional callback for the paste operation
 * @return A list of DefaultTextContextMenuItem objects
 */
fun defaultTextContextMenuItems(
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?
): List<DefaultTextContextMenuItem> =
    Builder<DefaultTextContextMenuItem>().apply {
        DefaultTextContextMenuItem(
            leadingIcon = Res.drawable.content_cut,
            label = Res.string.cut,
            shortcut = ContextMenuKeyShortcut(key = Key.X, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = cut != null,
            onClick = cut
        ).add()

        DefaultTextContextMenuItem(
            leadingIcon = Res.drawable.content_copy,
            label = Res.string.copy,
            shortcut = ContextMenuKeyShortcut(key = Key.C, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = copy != null,
            onClick = copy
        ).add()

        DefaultTextContextMenuItem(
            leadingIcon = Res.drawable.content_paste,
            label = Res.string.paste,
            shortcut = ContextMenuKeyShortcut(key = Key.V, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = paste != null,
            onClick = paste
        ).add()
    }.items
