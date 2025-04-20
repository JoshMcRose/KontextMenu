package foundation.representation.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DefaultTextContextMenuItemTest {

    @Test
    fun `test defaultTextContextMenuItems function exists`() {
        // Arrange
        val cut = { }
        val copy = { }
        val paste = { }

        // Act
        val items = defaultTextContextMenuItems(cut, copy, paste)

        // Assert
        assertEquals(3, items.size)

        // Check that all items have the expected properties
        items.forEach { item ->
            assertTrue(item.enabled)
            assertNotNull(item.leadingIcon)
            assertNotNull(item.label)
            assertNotNull(item.shortcut)
            assertNotNull(item.onClick)
        }
    }

    @Test
    fun `test defaultTextContextMenuItems with null operations`() {
        // Arrange & Act
        val items = defaultTextContextMenuItems(null, null, null)

        // Assert
        assertEquals(3, items.size)

        // All items should be disabled and have null onClick
        items.forEach { item ->
            assertFalse(item.enabled)
            assertEquals(null, item.onClick)
            // Even with null operations, the items should still have icons, labels, and shortcuts
            assertNotNull(item.leadingIcon)
            assertNotNull(item.label)
            assertNotNull(item.shortcut)
        }
    }
}
