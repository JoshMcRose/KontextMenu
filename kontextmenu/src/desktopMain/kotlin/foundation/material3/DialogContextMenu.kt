package foundation.material3

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
import foundation.TextContextMenu
import foundation.TextContextMenuArea
import foundation.composable.onHover
import foundation.representation.DialogTextContextMenuRepresentation
import foundation.representation.model.DefaultTextContextMenuItem
import foundation.representation.model.defaultTextContextMenuItems
import kontextmenu.kontextmenu.generated.resources.Res
import kontextmenu.kontextmenu.generated.resources.*
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
fun DialogContextMenu(
    cut: (() -> Unit)?,
    copy: (() -> Unit)?,
    paste: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    TextContextMenu(
        content = content,
        textContextMenuRepresentation = DialogTextContextMenuRepresentation(),
        textContextMenuArea = TextContextMenuArea(
            menuContent = { items, onDismissRequest -> DialogContextMenuBody(items, onDismissRequest) },
            builder = { defaultTextContextMenuItems(cut, copy, paste).addAll() }
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
private fun DialogContextMenuBody(
    items: List<DefaultTextContextMenuItem>,
    onDismiss: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf<DefaultTextContextMenuItem?>(null) }

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
    items: List<DefaultTextContextMenuItem>,
    selected: DefaultTextContextMenuItem?,
    modifier: Modifier = Modifier,
    onSelection: (DefaultTextContextMenuItem) -> Unit
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
    item: DefaultTextContextMenuItem,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: (DefaultTextContextMenuItem) -> Unit
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
