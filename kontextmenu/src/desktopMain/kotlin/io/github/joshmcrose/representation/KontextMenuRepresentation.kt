package io.github.joshmcrose.representation

import androidx.compose.foundation.ContextMenuState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Interface for creating custom context menu representations.
 * 
 * This interface allows developers to create their own implementation of how a context menu
 * should be displayed. Implementations can range from simple popups to complex dialogs or
 * custom UI components.
 *
 * @see [ContainerizedKontextMenuRepresentation]
 * @see [UncontainerizedKontextMenuRepresentation]
 *
 */
interface KontextMenuRepresentation {
    /**
     * Creates the visual representation of the context menu.
     *
     * @param state The current state of the context menu (open/closed, position)
     * @param menuContent A composable function that renders the content of the menu with the provided items
     * @param items The list of items to be displayed in the context menu
     */
    @Composable
    fun <T> Representation(
        state: ContextMenuState,
        menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
        items: List<T>
    )
}

/**
 * Provides a CompositionLocal for accessing the current [KontextMenuRepresentation].
 *
 * This allows components to access the current representation without having to pass it
 * explicitly through the composition tree.
 */
val LocalKontextMenuRepresentation: ProvidableCompositionLocal<KontextMenuRepresentation> =
    staticCompositionLocalOf { ContainerizedKontextMenuRepresentation() }