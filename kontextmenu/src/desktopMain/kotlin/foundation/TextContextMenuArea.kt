package foundation

import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.contextMenuOpenDetector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.foundation.text.TextContextMenu
import androidx.compose.foundation.text.TextContextMenu.TextManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import foundation.TextContextMenuArea.Builder
import foundation.representation.DefaultTextContextMenuRepresentation
import foundation.representation.LocalTextContextMenuRepresentation
import foundation.representation.TextContextMenuRepresentation

/**
 * Main class for creating custom context menus in text fields.
 *
 * This class implements the [TextContextMenu] interface and provides a way to create
 * custom context menus with a list of items and a composable function to display them.
 *
 * @param T The type of items to be displayed in the context menu
 * @property items The list of items to be displayed in the context menu
 * @property menuContent A composable function that renders the content of the menu
 */
@OptIn(ExperimentalFoundationApi::class)
class TextContextMenuArea<T>(
    val items: List<T>,
    val menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit
) : TextContextMenu {
    /**
     * Implements the Area function from the TextContextMenu interface.
     *
     * @param textManager The text manager that handles text operations
     * @param state The state of the context menu
     * @param content The content to display in the context menu area
     */
    @Composable
    override fun Area(
        textManager: TextManager,
        state: ContextMenuState,
        content: @Composable (() -> Unit)
    ) {
        TextContextMenuArea(textManager, items, state, menuContent, content)
    }

    /**
     * Builder class for creating a TextContextMenuArea with a fluent API.
     *
     * This class provides extension functions to easily add items to the menu.
     *
     * @param T The type of items to be displayed in the context menu
     */
    class Builder<T>() {
        private val _items = mutableListOf<T>()
        val items: List<T> get() = _items

        /**
         * Adds an item to the menu.
         */
        fun T.add() = _items.add(this)

        /**
         * Adds all items from a list to the menu.
         */
        fun List<T>.addAll() = _items.addAll(this)

        /**
         * Creates a TextContextMenuArea with the collected items and the provided menu content.
         */
        fun toTextContentMenu(menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit) =
            TextContextMenuArea(_items, menuContent)
    }
}

/**
 * Creates a context menu area for text components.
 * This composable function sets up a context menu with the provided items.
 *
 * @param textManager The text manager that handles text operations
 * @param items The list of context menu items to display
 * @param state The state of the context menu
 * @param menuContent A composable function that renders the content of the menu
 * @param content The content to display in the context menu area
 */
@ExperimentalFoundationApi
@Composable
private fun <T> TextContextMenuArea(
    textManager: TextManager,
    items: List<T>,
    state: ContextMenuState,
    menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.contextMenuOpenDetector(
            key = Pair(textManager, state)
        ) { pointerPosition ->
            // Open the context menu at the pointer position
            state.status = ContextMenuState.Status.Open(Rect(pointerPosition, 0f))
        },
        propagateMinConstraints = true,
    ) {
        content()
        LocalTextContextMenuRepresentation.current.Representation(state, menuContent, items)
    }
}

/**
 * Creates a TextContextMenuArea with a builder pattern.
 *
 * This function provides a convenient way to create a TextContextMenuArea using a builder pattern.
 *
 * @param menuContent A composable function that renders the content of the menu
 * @param builder A builder function to configure the TextContextMenuArea
 * @return A configured TextContextMenuArea
 */
inline fun <T> TextContextMenuArea(
    noinline menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
    builder: Builder<T>.() -> Unit
) = Builder<T>().apply(builder).toTextContentMenu(menuContent)

/**
 * Creates a text context menu with the provided items and menu content.
 *
 * This function sets up a text context menu with the provided items and menu content
 * and provides it to the content through CompositionLocalProvider.
 *
 * @param items The list of items to be displayed in the context menu
 * @param menuContent A composable function that renders the content of the menu
 * @param textContextMenuRepresentation The representation to use for the context menu
 * @param content The content to display in the context menu area
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> TextContextMenu(
    items: List<T>,
    menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
    textContextMenuRepresentation: TextContextMenuRepresentation = DefaultTextContextMenuRepresentation(),
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalTextContextMenuRepresentation provides textContextMenuRepresentation,
    LocalTextContextMenu provides TextContextMenuArea(items, menuContent)
) { content() }

/**
 * Creates a text context menu with the provided TextContextMenuArea.
 *
 * This function sets up a text context menu with the provided TextContextMenuArea
 * and provides it to the content through CompositionLocalProvider.
 *
 * @param textContextMenuArea The TextContextMenuArea to use
 * @param textContextMenuRepresentation The representation to use for the context menu
 * @param content The content to display in the context menu area
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> TextContextMenu(
    textContextMenuArea: TextContextMenuArea<T>,
    textContextMenuRepresentation: TextContextMenuRepresentation = DefaultTextContextMenuRepresentation(),
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalTextContextMenuRepresentation provides textContextMenuRepresentation,
    LocalTextContextMenu provides textContextMenuArea
) { content() }
