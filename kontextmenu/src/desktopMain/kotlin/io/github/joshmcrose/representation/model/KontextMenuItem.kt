package io.github.joshmcrose.representation.model

import androidx.compose.ui.input.key.Key
import io.github.joshmcrose.Builder
import io.github.joshmcrose.KontextMenuKeyShortcut
import io.github.joshmcrose.kontextmenu.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

/**
 * Data class representing an item in a text context menu.
 *
 * This class contains a standard list of properties to display and handle a menu item
 * in a text context menu.
 *
 * @property leadingIcon Optional icon to display before the label
 * @property label The text to display for this menu item
 * @property shortcut Optional keyboard shortcut associated with this menu item
 * @property enabled Whether this menu item is enabled
 * @property onClick Optional callback to invoke when this menu item is clicked
 */
data class KontextMenuItem(
    val leadingIcon: DrawableResource?,
    val label: StringResource,
    val shortcut: KontextMenuKeyShortcut?,
    val enabled: Boolean,
    val onClick: (() -> Unit)?
)

/**
 * Creates a list of [KontextMenuItem]s for common operations.
 *
 * This function creates a list of menu items for cut, copy, and paste operations,
 * with appropriate icons, labels, and keyboard shortcuts. The menu items are enabled
 * only if the corresponding operation is provided.
 *
 * @param cut Optional callback for the cut operation
 * @param copy Optional callback for the copy operation
 * @param paste Optional callback for the paste operation
 * @return A list of [KontextMenuItem] objects
 */
fun kontextMenuItems(
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?
): List<KontextMenuItem> =
    Builder<KontextMenuItem>().apply {
        KontextMenuItem(
            leadingIcon = Res.drawable.content_cut,
            label = Res.string.cut,
            shortcut = KontextMenuKeyShortcut(key = Key.X, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = cut != null,
            onClick = cut
        ).add()

        KontextMenuItem(
            leadingIcon = Res.drawable.content_copy,
            label = Res.string.copy,
            shortcut = KontextMenuKeyShortcut(key = Key.C, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = copy != null,
            onClick = copy
        ).add()

        KontextMenuItem(
            leadingIcon = Res.drawable.content_paste,
            label = Res.string.paste,
            shortcut = KontextMenuKeyShortcut(key = Key.V, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = paste != null,
            onClick = paste
        ).add()
    }.items