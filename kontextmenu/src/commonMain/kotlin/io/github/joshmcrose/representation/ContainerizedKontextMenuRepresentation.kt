package io.github.joshmcrose.representation

import androidx.compose.runtime.Composable
import io.github.joshmcrose.KontextMenuState
import io.github.joshmcrose.ui.KontextMenuPopup

/**
 * Default implementation of [KontextMenuRepresentation] that creates a basic popup for the context menu.
 *
 * This implementation provides a simple popup container for the context menu content. It handles:
 * - Displaying the popup when the context menu is open
 * - Positioning the popup at the correct location
 * - Providing an onDismiss request handler to close the menu
 *
 * The actual content of the menu is provided by the caller through the `menuContent` parameter of `Representation`.
 */
class ContainerizedKontextMenuRepresentation : KontextMenuRepresentation {
    /**
     * Creates a popup representation of the context menu.
     *
     * @param state The current state of the context menu
     * @param menuContent A composable function that renders the content of the menu
     * @param items The list of items to be displayed in the context menu
     */
    @Composable
    override fun <T> Representation(
        state: KontextMenuState,
        menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
        items: List<T>
    ) {
        val status = state.status
        if (status is KontextMenuState.Status.Open) {
            KontextMenuPopup(
                status = status,
                onDismissRequest = { state.status = KontextMenuState.Status.Closed }
            ) { dismissRequest ->
                menuContent(items, dismissRequest)
            }
        }
    }
}
