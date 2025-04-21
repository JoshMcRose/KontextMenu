package ui

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ComposablesTest {

    @Test
    fun `test composable package structure`() {
        // This test verifies that the composable package contains the expected functions
        // We can't directly test Composable functions, but we can verify the package structure
        
        // Get the package
        val packageName = "foundation.composable"
        
        // Verify that the package exists
        val packageClass = Class.forName("$packageName.ComposablesKt")
        assertNotNull(packageClass, "Package $packageName should exist")
        
        // Verify that the package contains the expected functions
        // We can't directly reference the functions, but we can check if they're mentioned in the class
        val methods = packageClass.methods
        val methodNames = methods.map { it.name }
        
        assertTrue(methodNames.any { it.contains("ContextMenuBody") }, 
            "Package should contain ContextMenuBody function")
        assertTrue(methodNames.any { it.contains("MenuItemContent") || it.contains("ItemContent") }, 
            "Package should contain MenuItemContent function")
        assertTrue(methodNames.any { it.contains("MenuItemTextBody") || it.contains("ItemTextBody") }, 
            "Package should contain MenuItemTextBody function")
        assertTrue(methodNames.any { it.contains("onHover") }, 
            "Package should contain onHover extension function")
    }
}