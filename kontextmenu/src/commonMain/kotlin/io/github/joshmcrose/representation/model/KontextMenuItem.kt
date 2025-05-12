package io.github.joshmcrose.representation.model

import io.github.joshmcrose.KontextMenuKeyShortcut
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

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
expect fun kontextMenuItems(
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?
): List<KontextMenuItem>
