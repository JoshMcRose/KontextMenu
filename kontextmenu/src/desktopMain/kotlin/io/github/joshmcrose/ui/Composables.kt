package io.github.joshmcrose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.joshmcrose.representation.model.KontextMenuItem
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource

/**
 * A composable function that creates the body of a context menu.
 *
 * This function creates a column with menu items, with customizable appearance and behavior.
 * It handles the layout, styling, and interaction of the menu items.
 *
 * @param items The list of menu items to display
 * @param shape The shape of the context menu
 * @param containerColor The background color of the context menu
 * @param itemHoverColor The background color of menu items when hovered
 * @param labelColor The color of the menu item labels
 * @param shortcutColor The color of the menu item shortcuts
 * @param height The height of each menu item
 * @param verticalPadding The vertical padding of the context menu
 * @param horizontalPadding The horizontal padding of each menu item
 * @param internalSpacing The spacing between elements within each menu item
 * @param onDismissRequest Optional callback to invoke when the context menu should be dismissed
 * @param icon A composable function that renders the icon for a menu item
 * @param text A composable function that renders the text for a menu item
 * @param modifier Optional modifier for the context menu
 */
@Composable
internal fun KontextMenuBody(
    items: List<KontextMenuItem>,
    shape: Shape,
    containerColor: Color,
    itemHoverColor: Color,
    labelColor: Color,
    shortcutColor: Color,
    height: Dp,
    verticalPadding: Dp,
    horizontalPadding: Dp,
    internalSpacing: Dp,
    onDismissRequest: (() -> Unit)?,
    icon: @Composable (DrawableResource, Boolean) -> Unit,
    text: @Composable (String, Boolean, Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(8.dp)
            .clip(shape)
            .background(containerColor)
            .padding(vertical = verticalPadding)
            .width(IntrinsicSize.Max)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        items.forEach { item ->
            MenuItemContent(
                item = item,
                containerColor = containerColor,
                itemHoverColor = itemHoverColor,
                labelColor = labelColor,
                shortcutColor = shortcutColor,
                height = height,
                horizontalPadding = horizontalPadding,
                internalSpacing = internalSpacing,
                onClick = {
                    item.onClick?.let {
                        onDismissRequest?.invoke()
                        it()
                    }
                },
                icon = icon,
                text = text
            )
        }
    }
}

/**
 * A composable function that creates the content of a menu item.
 *
 * This function creates a row with an icon and text, with customizable appearance and behavior.
 * It handles the layout, styling, and interaction of a single menu item.
 *
 * @param item The menu item to display
 * @param containerColor The background color of the menu item
 * @param itemHoverColor The background color of the menu item when hovered
 * @param labelColor The color of the menu item label
 * @param shortcutColor The color of the menu item shortcut
 * @param height The height of the menu item
 * @param horizontalPadding The horizontal padding of the menu item
 * @param internalSpacing The spacing between elements within the menu item
 * @param onClick Callback to invoke when the menu item is clicked
 * @param icon A composable function that renders the icon for the menu item
 * @param text A composable function that renders the text for the menu item
 */
@Composable
private fun MenuItemContent(
    item: KontextMenuItem,
    containerColor: Color,
    itemHoverColor: Color,
    labelColor: Color,
    shortcutColor: Color,
    height: Dp,
    horizontalPadding: Dp,
    internalSpacing: Dp,
    onClick: () -> Unit,
    icon: @Composable (DrawableResource, Boolean) -> Unit,
    text: @Composable (String, Boolean, Color) -> Unit
) {
    var hovered by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clickable(enabled = item.enabled, onClick = onClick)
            .onHover { hovered = it }
            .background(if (hovered && item.enabled) itemHoverColor else containerColor)
            .fillMaxWidth()
            .widthIn(min = 112.dp, max = 280.dp)
            .height(height)
            .padding(PaddingValues(horizontal = horizontalPadding, vertical = 0.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(internalSpacing, Alignment.Start)
    ) {
        item.leadingIcon?.let { icon(item.leadingIcon, item.enabled) }
        MenuItemTextBody(
            item = item,
            labelColor = labelColor,
            shortcutColor = shortcutColor,
            text = text
        )
    }
}

/**
 * A composable function that creates the text body of a menu item.
 *
 * This function creates a row with the label and shortcut of a menu item,
 * with a customizable appearance.
 *
 * @param item The menu item to display
 * @param labelColor The color of the menu item label
 * @param shortcutColor The color of the menu item shortcut
 * @param text A composable function that renders the text for the menu item
 */
@Composable
private fun MenuItemTextBody(
    item: KontextMenuItem,
    labelColor: Color,
    shortcutColor: Color,
    text: @Composable (String, Boolean, Color) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        text(stringResource(resource = item.label), item.enabled, labelColor)

        Spacer(Modifier.width(36.dp))

        item.shortcut?.let { text(it.getKeyStroke(), item.enabled, shortcutColor) }
    }
}

/**
 * A modifier extension function that adds hover detection to a composable.
 *
 * This function adds a pointer input handler that detects when the pointer enters or exits
 * the composable and invokes the provided callback with the hover state.
 *
 * @param onHover Callback to invoke when the hover state changes
 * @return A modifier with hover detection
 */
fun Modifier.onHover(onHover: (Boolean) -> Unit) = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            when (event.type) {
                PointerEventType.Enter -> onHover(true)
                PointerEventType.Exit -> onHover(false)
            }
        }
    }
}
