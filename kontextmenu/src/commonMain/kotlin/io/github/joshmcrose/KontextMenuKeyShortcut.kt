package io.github.joshmcrose

import androidx.compose.ui.input.key.Key
import org.jetbrains.skiko.hostOs

interface KontextMenuKeyShortcut {
    val key: Key
    val ctrl: Boolean
    val meta: Boolean
    val alt: Boolean
    val shift: Boolean

    fun getKeyText(): String
    fun getKeyStroke(): String

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    override fun toString(): String

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