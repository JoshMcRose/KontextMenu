package io.github.joshmcrose.material3

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.joshmcrose.KontextMenu
import io.github.joshmcrose.kontextMenuArea
import io.github.joshmcrose.kontextmenu.generated.resources.*
import io.github.joshmcrose.representation.UncontainerizedKontextMenuRepresentation
import io.github.joshmcrose.representation.model.KontextMenuItem
import io.github.joshmcrose.representation.model.kontextMenuItems
import io.github.joshmcrose.ui.onHover
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * A Material 3 implementation of a dialog-based context menu.
 *
 * This composable function creates a context menu using a Material 3 dialog.
 * It provides cut, copy, and paste functionality with a dialog interface.
 *
 * @param cut Optional callback for the cut operation
 * @param copy Optional callback for the copy operation
 * @param paste Optional callback for the paste operation
 * @param content The content to display in the context menu area
 */
@Suppress("unused")
@Composable
fun DialogKontextMenu(
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    KontextMenu(
        content = content,
        kontextMenuRepresentation = UncontainerizedKontextMenuRepresentation(),
        kontextMenuArea = kontextMenuArea(
            menuContent = { items, onDismissRequest -> DialogKontextMenuBody(items, onDismissRequest) },
            builder = { kontextMenuItems(cut, copy, paste).addAll() }
        )
    )
}

/**
 * The body of the dialog-based context menu.
 *
 * This composable function creates a Material 3 dialog with a list of actions
 * and OK/Cancel buttons.
 *
 * @param items The list of menu items to display
 * @param onDismiss Optional callback to invoke when the dialog is dismissed
 * @param modifier Optional modifier for the dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogKontextMenuBody(
    items: List<KontextMenuItem>,
    onDismiss: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf<KontextMenuItem?>(null) }

    BasicAlertDialog(
        onDismissRequest = { onDismiss?.invoke() },
        modifier = modifier
            .widthIn(280.dp, 560.dp)
            .shadow(elevation = 6.dp, shape = shapes.extraLarge)
            .clip(shapes.extraLarge)
            .background(colorScheme.surfaceContainerHigh)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(Res.string.context_menu),
                style = typography.headlineSmall,
                color = colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.select_action),
                style = typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))

            ActionBox(
                items = items,
                selected = selectedItem
            ) { selectedItem = it }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                TextButton(onClick = { onDismiss?.invoke() }) {
                    Text(
                        text = stringResource(Res.string.cancel),
                        style = typography.labelLarge,
                        color = colorScheme.primary
                    )
                }

                TextButton(
                    onClick = {
                        selectedItem?.onClick?.invoke()
                        onDismiss?.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.ok),
                        style = typography.labelLarge,
                        color = colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * A box containing a list of actions for the dialog-based context menu.
 *
 * This composable function creates a column with a list of action rows,
 * with a border and background.
 *
 * @param items The list of menu items to display
 * @param selected The currently selected menu item, if any
 * @param modifier Optional modifier for the box
 * @param onSelection Callback to invoke when a menu item is selected
 */
@Composable
private fun ActionBox(
    items: List<KontextMenuItem>,
    selected: KontextMenuItem?,
    modifier: Modifier = Modifier,
    onSelection: (KontextMenuItem) -> Unit
) {
    Column(
        modifier = modifier
            .background(colorScheme.surfaceContainerLowest)
            .border(width = 1.dp, color = colorScheme.outline)
    ) {
        items.forEach { item ->
            ActionRow(
                item = item,
                selected = item == selected,
                onSelect = onSelection
            )
        }
    }
}

/**
 * A row representing an action in the dialog-based context menu.
 *
 * This composable function creates a row with an icon and text,
 * with hover and selection effects.
 *
 * @param item The menu item to display
 * @param selected Whether this menu item is currently selected
 * @param modifier Optional modifier for the row
 * @param onSelect Callback to invoke when this menu item is selected
 */
@Composable
private fun ActionRow(
    item: KontextMenuItem,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: (KontextMenuItem) -> Unit
) {
    var hovered by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .clickable(enabled = item.enabled, onClick = { onSelect(item) })
            .onHover { hovered = it }
            .fillMaxWidth()
            .height(56.dp)
            .background(
                if (item.enabled) {
                    when {
                        selected && hovered -> colorScheme.secondaryContainer.copy(alpha = 0.9F)
                        selected -> colorScheme.secondaryContainer
                        hovered -> colorScheme.onSurface.copy(alpha = 0.08F)
                        else -> Color.Transparent
                    }
                } else colorScheme.onSurface.copy(alpha = 0.38F)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start)
    ) {
        item.leadingIcon?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = when {
                    item.enabled && selected -> colorScheme.onSurface
                    item.enabled -> colorScheme.onSurfaceVariant
                    else -> colorScheme.onSurface.copy(alpha = 0.38F)
                },
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = stringResource(item.label),
            style = typography.bodyLarge,
            color = when {
                selected && item.enabled -> colorScheme.onSecondaryContainer
                item.enabled -> colorScheme.onSurface
                else -> colorScheme.onSurface.copy(alpha = 0.38F)
            }
        )
    }
}
