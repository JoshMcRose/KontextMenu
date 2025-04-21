package foundation.representation

import androidx.compose.foundation.ContextMenuState
import androidx.compose.runtime.Composable

/**
 * Implementation of [KontextMenuRepresentation] that provides minimal structure.
 *
 * Unlike [ContainerizedKontextMenuRepresentation], this implementation doesn't provide any UI container.
 * It only handles the context menu state and provides the onDismissRequest lambda to the content.
 * This allows developers to create a completely custom UI for their context menus, such as dialogs
 * or other complex components.
 */
class UncontainerizedKontextMenuRepresentation : KontextMenuRepresentation {
    /**
     * Creates a minimal representation of the context menu without any UI container.
     *
     * @param state The current state of the context menu
     * @param menuContent A composable function that renders the content of the menu
     * @param items The list of items to be displayed in the context menu
     */
    @Composable
    override fun <T> Representation(
        state: ContextMenuState,
        menuContent: @Composable ((List<T>, (() -> Unit)?) -> Unit),
        items: List<T>
    ) {
        val status = state.status
        if (status is ContextMenuState.Status.Open) {
            menuContent(items) { state.status = ContextMenuState.Status.Closed }
        }
    }
}
