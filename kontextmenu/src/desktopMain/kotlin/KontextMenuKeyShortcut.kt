import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.nativeKeyCode
import org.jetbrains.skiko.hostOs
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
class KontextMenuKeyShortcut(
    val key: Key,
    val ctrl: Boolean = false,
    val meta: Boolean = false,
    val alt: Boolean = false,
    val shift: Boolean = false
) {
    /**
     * Gets the text representation of the key.
     *
     * @return The text representation of the key
     */
    fun getKeyText(): String = KeyEvent.getKeyText(key.nativeKeyCode)

    /**
     * Gets a formatted string representation of the keyboard shortcut.
     *
     * This method creates a string that includes all the modifier keys and the main key,
     * formatted according to the current platform (symbols on macOS, text on other platforms).
     *
     * @return A formatted string representation of the keyboard shortcut
     */
    fun getKeyStroke() = buildString {
        if (ctrl) append(Control)
        if (alt) append(Alt)
        if (shift) append(Shift)
        if (meta) append(Meta)
        append(getKeyText())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KontextMenuKeyShortcut

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

    /**
     * Companion object containing platform-specific modifier key representations.
     */
    companion object {
        private val IsMacOS = hostOs.isMacOS

        /**
         * Platform-specific representation of the Control key.
         * On macOS, this is "⌃", on other platforms, this is "Ctrl+".
         */
        val Control = if (IsMacOS) "⌃" else "Ctrl+"

        /**
         * Platform-specific representation of the Meta/Command key.
         * On macOS, this is "⌘", on other platforms, this is "Win+".
         */
        val Meta = if (IsMacOS) "⌘" else "Win+"

        /**
         * Platform-specific representation of the Alt/Option key.
         * On macOS, this is "⌥", on other platforms, this is "Alt+".
         */
        val Alt = if (IsMacOS) "⌥" else "Alt+"

        /**
         * Platform-specific representation of the Shift key.
         * On macOS, this is "⇧", on other platforms, this is "Shift+".
         */
        val Shift = if (IsMacOS) "⇧" else "Shift+"
    }
}