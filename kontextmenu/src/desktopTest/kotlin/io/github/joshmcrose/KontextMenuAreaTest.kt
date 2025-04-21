package io.github.joshmcrose
import androidx.compose.runtime.Composable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KontextMenuAreaTest {

    @Test
    fun `test Builder add method`() {
        // Arrange
        val builder = Builder<String>()
        val item = "Test Item"

        // Act
        builder.apply { item.add() }

        // Assert
        assertEquals(1, builder.items.size)
        assertEquals(item, builder.items[0])
    }

    @Test
    fun `test Builder addAll method`() {
        // Arrange
        val builder = Builder<String>()
        val items = listOf("Item 1", "Item 2", "Item 3")

        // Act
        builder.apply { items.addAll() }

        // Assert
        assertEquals(3, builder.items.size)
        assertTrue(builder.items.containsAll(items))
    }

    @Test
    fun `test Builder toTextContentMenu method`() {
        // Arrange
        val builder = Builder<String>()
        val items = listOf("Item 1", "Item 2", "Item 3")
        builder.apply { items.addAll() }

        // Act
        val menuContent: @Composable (items: List<String>, onDismissRequest: (() -> Unit)?) -> Unit = { _, _ -> }
        val textContextMenu = builder.toKontextMenu(menuContent)

        // Assert
        assertEquals(items, textContextMenu.items)
        // We can't directly test the menuContent function, but we can verify it's assigned
        assertTrue(textContextMenu.menuContent === menuContent)
    }
}
