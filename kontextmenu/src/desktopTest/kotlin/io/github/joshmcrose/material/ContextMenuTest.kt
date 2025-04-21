package io.github.joshmcrose.material
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MaterialKontextMenuTest {

    @Test
    fun `test material package structure`() {
        // This test verifies that the material package contains the expected classes
        // We can't directly test Composable functions, but we can verify the package structure

        // Get the package
        val packageName = "foundation.material"

        // Verify that the package exists
        val packageClass = Class.forName("$packageName.KontextMenuKt")
        assertNotNull(packageClass, "Package $packageName should exist")

        // Verify that the package contains the expected functions
        // We can't directly reference the functions, but we can check if they're mentioned in the class
        val methods = packageClass.methods
        val methodNames = methods.map { it.name }

        assertTrue(methodNames.any { it.contains("KontextMenu") },
            "Package should contain KontextMenu function")
        assertTrue(methodNames.any { it.contains("KontextIcon") },
            "Package should contain KontextIcon function")
        assertTrue(methodNames.any { it.contains("KontextMenuText") },
            "Package should contain KontextMenuText function")
    }
}
