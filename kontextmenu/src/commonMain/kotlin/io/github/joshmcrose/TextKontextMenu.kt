package io.github.joshmcrose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAll
import io.github.joshmcrose.TextKontextMenu.TextManager
import io.github.joshmcrose.material3.KontextIcon
import io.github.joshmcrose.material3.KontextMenuText
import io.github.joshmcrose.representation.ContainerizedKontextMenuRepresentation
import io.github.joshmcrose.representation.KontextMenuRepresentation
import io.github.joshmcrose.representation.LocalKontextMenuRepresentation
import io.github.joshmcrose.representation.model.kontextMenuItems
import io.github.joshmcrose.ui.KontextMenuBody

/**
 * Composition local that keeps [TextKontextMenu].
 */
@ExperimentalFoundationApi
val LocalTextKontextMenu:
        ProvidableCompositionLocal<TextKontextMenu> = staticCompositionLocalOf { TextKontextMenu.Default }

@ExperimentalFoundationApi
interface TextKontextMenu { // TODO: Desktop must extend its own version of TextContextMenu. This will have to be split back out
    @Composable
    fun Area(textManager: TextManager, state: KontextMenuState, content: @Composable () -> Unit)

    /**
     * Provides useful methods and information for text for which we show the text context menu.
     */
    @ExperimentalFoundationApi
    interface TextManager {
        /**
         * Action for cutting the selected text to the clipboard. Null if there is no text to cut.
         */
        val cut: (() -> Unit)?

        /**
         * Action for copy the selected text to the clipboard. Null if there is no text to copy.
         */
        val copy: (() -> Unit)?

        /**
         * Action for pasting text from the clipboard. Null if there is no text in the clipboard.
         */
        val paste: (() -> Unit)?
    }

    companion object {
        /**
         * [TextKontextMenu] that is used by default in Compose.
         */
        @ExperimentalFoundationApi
        val Default = object : TextKontextMenu {
            @Composable
            override fun Area(textManager: TextManager, state: KontextMenuState, content: @Composable () -> Unit) {
                val items = kontextMenuItems(
                    cut = textManager.cut,
                    copy = textManager.copy,
                    paste = textManager.paste,
                )

                KontextMenuArea(
                    textManager = textManager,
                    items = items,
                    state = state,
                    menuContent = { items, onDismissRequest ->
                        KontextMenuBody(
                            items = items,
                            containerColor = colorScheme.surfaceContainer,
                            itemHoverColor = colorScheme.onSurface.copy(alpha = .08F),
                            labelColor = colorScheme.onSurface,
                            shortcutColor = colorScheme.onSurfaceVariant,
                            height = 32.dp,
                            verticalPadding = 4.dp,
                            horizontalPadding = 12.dp,
                            internalSpacing = 12.dp,
                            shape = shapes.extraSmall,
                            onDismissRequest = onDismissRequest,
                            icon = { drawable, enabled -> KontextIcon(drawable, enabled) },
                            text = { string, enabled, color -> KontextMenuText(string, enabled, color) }
                        )
                    },
                    content = content
                )
            }
        }
    }
}

/**
 * Main interface for creating custom context menus in text fields.
 *
 * This interface implements the [TextKontextMenu] interface and provides a way to create
 * custom context menus with a list of items and a composable function to display them.
 *
 * In most cases, the default [KontextMenuArea] class should be used to create a context menu.
 * The [kontextMenuArea] function provides public access to the builder pattern for creating custom context menus.
 *
 * @param T The type of items to be displayed in the context menu
 * @property items The list of items to be displayed in the context menu
 * @property menuContent A composable function that renders the content of the menu
 */
@OptIn(ExperimentalFoundationApi::class)
interface IKontextMenuArea<T> : TextKontextMenu {
    val items: List<T>
    val menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit
}

/**
 * Default class for creating custom context menus in text fields.
 *
 * This class implements the [IKontextMenuArea] interface and provides a way to create
 * custom context menus with a list of items and a composable function to display them.
 *
 * @param T The type of items to be displayed in the context menu
 * @property items The list of items to be displayed in the context menu
 * @property menuContent A composable function that renders the content of the menu
 */
@OptIn(ExperimentalFoundationApi::class)
internal class KontextMenuArea<T>(
    override val items: List<T>,
    override val menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit
) : IKontextMenuArea<T> {
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
        state: KontextMenuState,
        content: @Composable (() -> Unit)
    ) { KontextMenuArea(textManager, items, state, menuContent, content) }
}

/**
 * Creates a context menu area for text components.
 * This composable function sets up a context menu with the provided items.
 *
 * If a custom implementation of this Composable is needed (for example, if platform-specific functionality is needed),
 * in addition to creating a new Composable, you will need to extend the [IKontextMenuArea] interface and apply your composable to the `Area` function.
 *
 * @param textManager The text manager that handles text operations
 * @param items The list of context menu items to display
 * @param state The state of the context menu
 * @param menuContent A composable function that renders the content of the menu
 * @param content The content to display in the context menu area
 */
@ExperimentalFoundationApi
@Composable
private fun <T> KontextMenuArea(
    textManager: TextManager,
    items: List<T>,
    state: KontextMenuState,
    menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.kontextMenuOpenDetector(
            key = Pair(textManager, state)
        ) { pointerPosition ->
            // Open the context menu at the pointer position
            state.status = KontextMenuState.Status.Open(Rect(pointerPosition, 0f))
        },
        propagateMinConstraints = true,
    ) {
        content()
        LocalKontextMenuRepresentation.current.Representation(state, menuContent, items)
    }
}

/**
 * Creates a text context menu with the provided [IKontextMenuArea] and [KontextMenuRepresentation].
 *
 * This function sets up a text context menu with the provided [IKontextMenuArea] and [KontextMenuRepresentation]
 * and provides them to the content through CompositionLocalProvider.
 *
 * @param kontextMenuArea The [IKontextMenuArea] to use
 * @param kontextMenuRepresentation The representation to use for the context menu
 * @param content The content to display in the context menu area
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> KontextMenu(
    kontextMenuArea: IKontextMenuArea<T>,
    kontextMenuRepresentation: KontextMenuRepresentation = ContainerizedKontextMenuRepresentation(),
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalKontextMenuRepresentation provides kontextMenuRepresentation,
    LocalTextKontextMenu provides kontextMenuArea
) { content() }

/**
 * Creates a text context menu with the provided items and menu content.
 *
 * This function sets up a text context menu with the provided items and menu content
 * and provides it to the content through CompositionLocalProvider.
 *
 * @param items The list of items to be displayed in the context menu
 * @param menuContent A composable function that renders the content of the menu
 * @param kontextMenuRepresentation The representation to use for the context menu
 * @param content The content to display in the context menu area
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> KontextMenu(
    items: List<T>,
    menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
    kontextMenuRepresentation: KontextMenuRepresentation = ContainerizedKontextMenuRepresentation(),
    content: @Composable () -> Unit
) = CompositionLocalProvider(
   LocalKontextMenuRepresentation provides kontextMenuRepresentation,
    LocalTextKontextMenu provides KontextMenuArea(items, menuContent)
) { content() }

/**
 * Creates an [IKontextMenuArea] with a builder pattern.
 *
 * This function provides a convenient way to create an [IKontextMenuArea] using a builder pattern.
 *
 * @param menuContent A composable function that renders the content of the menu
 * @param builder A builder function to configure the [IKontextMenuArea]
 * @return A configured [IKontextMenuArea]
 */
inline fun <T> kontextMenuArea(
    noinline menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
    builder: Builder<T>.() -> Unit
) = Builder<T>().apply(builder).toKontextMenu(menuContent)

/**
 * Builder class for creating an [IKontextMenuArea] with a fluent API.
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
     * Creates an [IKontextMenuArea] with the collected items and the provided menu content.
     */
    fun toKontextMenu(
        menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit
    ): IKontextMenuArea<T> =
        KontextMenuArea(_items) { items, onDismissRequest ->
            menuContent(items, onDismissRequest)
        }
}

/**
 * Detects events that open a context menu (mouse right-clicks).
 *
 * @param key The pointer input handling coroutine will be canceled and **re-started** when
 * [kontextMenuOpenDetector] is recomposed with a different [key].
 * @param enabled Whether to enable the detection.
 * @param onOpen Invoked when a context menu opening event is detected, with the local offset it
 * should be opened at.
 */
@ExperimentalFoundationApi
fun Modifier.kontextMenuOpenDetector(
    key: Any? = Unit,
    enabled: Boolean = true,
    onOpen: (Offset) -> Unit
): Modifier {
    return if (enabled) {
        this.pointerInput(key) {
            awaitEachGesture {
                val event = awaitEventFirstDown()
                if (event.buttons.isSecondaryPressed) {
                    event.changes.forEach { it.consume() }
                    onOpen(event.changes[0].position)
                }
            }
        }
    } else {
        this
    }
}

private suspend fun AwaitPointerEventScope.awaitEventFirstDown(): PointerEvent {
    var event: PointerEvent
    do {
        event = awaitPointerEvent()
    } while (
        !event.changes.fastAll { it.changedToDown() }
    )
    return event
}