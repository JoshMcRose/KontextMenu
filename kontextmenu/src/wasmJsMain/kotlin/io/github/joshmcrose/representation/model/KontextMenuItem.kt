package io.github.joshmcrose.representation.model

import androidx.compose.ui.input.key.Key
import io.github.joshmcrose.Builder
import io.github.joshmcrose.KontextMenuKeyShortcutImpl
import io.github.joshmcrose.kontextmenu.generated.resources.Res
import io.github.joshmcrose.kontextmenu.generated.resources.content_copy
import io.github.joshmcrose.kontextmenu.generated.resources.content_cut
import io.github.joshmcrose.kontextmenu.generated.resources.content_paste
import io.github.joshmcrose.kontextmenu.generated.resources.copy
import io.github.joshmcrose.kontextmenu.generated.resources.cut
import io.github.joshmcrose.kontextmenu.generated.resources.paste
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

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
actual fun kontextMenuItems(
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?
): List<KontextMenuItem> =
    Builder<KontextMenuItem>().apply {
        KontextMenuItem(
            leadingIcon = Res.drawable.content_cut,
            label = Res.string.cut,
            shortcut = KontextMenuKeyShortcutImpl(key = Key.X, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = cut != null,
            onClick = cut
        ).add()

        KontextMenuItem(
            leadingIcon = Res.drawable.content_copy,
            label = Res.string.copy,
            shortcut = KontextMenuKeyShortcutImpl(key = Key.C, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = copy != null,
            onClick = copy
        ).add()

        KontextMenuItem(
            leadingIcon = Res.drawable.content_paste,
            label = Res.string.paste,
            shortcut = KontextMenuKeyShortcutImpl(key = Key.V, ctrl = hostOs == OS.Windows, meta = hostOs == OS.MacOS),
            enabled = paste != null,
            onClick = paste
        ).add()
    }.items