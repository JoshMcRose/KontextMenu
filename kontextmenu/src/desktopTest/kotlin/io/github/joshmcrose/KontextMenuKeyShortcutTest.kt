package io.github.joshmcrose

import androidx.compose.ui.input.key.Key
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KontextMenuKeyShortcutTest {

    @Test
    fun `test ContextMenuKeyShortcut creation`() {
        // Arrange & Act
        val shortcut = KontextMenuKeyShortcut(
            key = Key.X,
            ctrl = true,
            meta = false,
            alt = true,
            shift = false
        )
        
        // Assert
        assertEquals(Key.X, shortcut.key)
        assertTrue(shortcut.ctrl)
        assertFalse(shortcut.meta)
        assertTrue(shortcut.alt)
        assertFalse(shortcut.shift)
    }
    
    @Test
    fun `test ContextMenuKeyShortcut getKeyText`() {
        // Arrange
        val shortcut = KontextMenuKeyShortcut(key = Key.X)
        
        // Act
        val keyText = shortcut.getKeyText()
        
        // Assert
        assertTrue(keyText.isNotEmpty())
        assertTrue(keyText.contains("X", ignoreCase = true))
    }
    
    @Test
    fun `test ContextMenuKeyShortcut getKeyStroke with no modifiers`() {
        // Arrange
        val shortcut = KontextMenuKeyShortcut(key = Key.X)
        
        // Act
        val keyStroke = shortcut.getKeyStroke()
        
        // Assert
        assertTrue(keyStroke.isNotEmpty())
        assertTrue(keyStroke.contains("X", ignoreCase = true))
    }
    
    @Test
    fun `test ContextMenuKeyShortcut getKeyStroke with all modifiers`() {
        // Arrange
        val shortcut = KontextMenuKeyShortcut(
            key = Key.X,
            ctrl = true,
            meta = true,
            alt = true,
            shift = true
        )
        
        // Act
        val keyStroke = shortcut.getKeyStroke()
        
        // Assert
        assertTrue(keyStroke.isNotEmpty())
        assertTrue(keyStroke.contains("X", ignoreCase = true))
        
        // Check that the modifiers are included in some form
        // The exact format depends on the platform, so we just check that they're included
        val lowerKeyStroke = keyStroke.lowercase()
        assertTrue(lowerKeyStroke.contains("ctrl") || lowerKeyStroke.contains("⌃"), 
            "KeyStroke should include Ctrl modifier")
        assertTrue(lowerKeyStroke.contains("meta") || lowerKeyStroke.contains("⌘") || lowerKeyStroke.contains("win"), 
            "KeyStroke should include Meta modifier")
        assertTrue(lowerKeyStroke.contains("alt") || lowerKeyStroke.contains("⌥"), 
            "KeyStroke should include Alt modifier")
        assertTrue(lowerKeyStroke.contains("shift") || lowerKeyStroke.contains("⇧"), 
            "KeyStroke should include Shift modifier")
    }
    
    @Test
    fun `test ContextMenuKeyShortcut equals and hashCode`() {
        // Arrange
        val shortcut1 = KontextMenuKeyShortcut(
            key = Key.X,
            ctrl = true,
            meta = false,
            alt = true,
            shift = false
        )
        
        val shortcut2 = KontextMenuKeyShortcut(
            key = Key.X,
            ctrl = true,
            meta = false,
            alt = true,
            shift = false
        )
        
        val shortcut3 = KontextMenuKeyShortcut(
            key = Key.Y,
            ctrl = true,
            meta = false,
            alt = true,
            shift = false
        )
        
        // Assert
        assertEquals(shortcut1, shortcut2)
        assertEquals(shortcut1.hashCode(), shortcut2.hashCode())
        
        assertFalse(shortcut1 == shortcut3)
        assertFalse(shortcut1.hashCode() == shortcut3.hashCode())
    }
    
    @Test
    fun `test ContextMenuKeyShortcut toString`() {
        // Arrange
        val shortcut = KontextMenuKeyShortcut(
            key = Key.X,
            ctrl = true,
            meta = true,
            alt = false,
            shift = false
        )
        
        // Act
        val string = shortcut.toString()
        
        // Assert
        assertTrue(string.contains("Ctrl+"))
        assertTrue(string.contains("Meta+"))
        assertTrue(string.contains("X"))
        assertFalse(string.contains("Alt+"))
        assertFalse(string.contains("Shift+"))
    }
}