package io.github.joshmcrose

import androidx.compose.ui.input.key.Key
import io.github.joshmcrose.KontextMenuKeyShortcut.Companion.Alt
import io.github.joshmcrose.KontextMenuKeyShortcut.Companion.Control
import io.github.joshmcrose.KontextMenuKeyShortcut.Companion.Meta
import io.github.joshmcrose.KontextMenuKeyShortcut.Companion.Shift
import org.w3c.dom.events.KeyboardEventInit

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
    override fun getKeyText(): String = KeyboardEventInit(key = key.toString(), code = key.keyCode.toString()).key ?: ""

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
        if (other == null || this::class != other::class) return false

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