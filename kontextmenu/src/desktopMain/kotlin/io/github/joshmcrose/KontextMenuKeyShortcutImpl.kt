package io.github.joshmcrose

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import io.github.joshmcrose.KontextMenuKeyShortcut.Companion.Alt
import io.github.joshmcrose.KontextMenuKeyShortcut.Companion.Control
import io.github.joshmcrose.KontextMenuKeyShortcut.Companion.Meta
import io.github.joshmcrose.KontextMenuKeyShortcut.Companion.Shift
import java.awt.event.KeyEvent

/**
 * Represents a keyboard shortcut for context menu items.
 *
 * This class handles the representation of keyboard shortcuts with support for
 * platform-specific formatting (using symbols on macOS, text on other platforms).
 *
 * @property key The main key of the shortcut
 * @property ctrl Whether the Control key is part of the shortcut
 * @property meta Whether the Meta/Command key is part of the shortcut
 * @property alt Whether the Alt/Option key is part of the shortcut
 * @property shift Whether the Shift key is part of the shortcut
 */
class KontextMenuKeyShortcutImpl(
    override val key: Key,
    override val ctrl: Boolean = false,
    override val meta: Boolean = false,
    override val alt: Boolean = false,
    override val shift: Boolean = false
) : KontextMenuKeyShortcut {
    /**
     * Gets the text representation of the key.
     *
     * @return The text representation of the key
     */
    override fun getKeyText(): String = KeyEvent.getKeyText(key.nativeKeyCode)

    /**
     * Gets a formatted string representation of the keyboard shortcut.
     *
     * This method creates a string that includes all the modifier keys and the main key,
     * formatted according to the current platform (symbols on macOS, text on other platforms).
     *
     * @return A formatted string representation of the keyboard shortcut
     */
    override fun getKeyStroke() = buildString {
        if (ctrl) append(Control)
        if (alt) append(Alt)
        if (shift) append(Shift)
        if (meta) append(Meta)
        append(getKeyText())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KontextMenuKeyShortcutImpl

        if (key != other.key) return false
        if (ctrl != other.ctrl) return false
        if (meta != other.meta) return false
        if (alt != other.alt) return false
        if (shift != other.shift) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + ctrl.hashCode()
        result = 31 * result + meta.hashCode()
        result = 31 * result + alt.hashCode()
        result = 31 * result + shift.hashCode()
        return result
    }

    override fun toString() = buildString {
        if (ctrl) append("Ctrl+")
        if (meta) append("Meta+")
        if (alt) append("Alt+")
        if (shift) append("Shift+")
        append(key)
    }
}