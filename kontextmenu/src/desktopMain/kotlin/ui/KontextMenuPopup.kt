package ui

import androidx.compose.foundation.ContextMenuState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.InputModeManager
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.window.rememberPopupPositionProviderAtPosition
import java.awt.event.KeyEvent.VK_DOWN
import java.awt.event.KeyEvent.VK_UP

/**
 * A composable function that creates a popup for displaying context menu content.
 *
 * This popup is positioned at the center of the provided status rectangle and handles
 * keyboard navigation (up/down arrows) for the menu items. It also provides an onDismiss
 * request handler to close the popup.
 *
 * @param status The open status of the context menu, containing the position information
 * @param onDismissRequest Optional callback to invoke when the popup should be dismissed
 * @param content A composable function that renders the content of the popup
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KontextMenuPopup(
    status: ContextMenuState.Status.Open,
    onDismissRequest: (() -> Unit)? = null,
    content: @Composable ((() -> Unit)?) -> Unit,
) {
    var focusManager: FocusManager? by mutableStateOf(null)
    var inputModeManager: InputModeManager? by mutableStateOf(null)

    Popup(
        properties = PopupProperties(focusable = true),
        onDismissRequest = onDismissRequest,
        popupPositionProvider = rememberPopupPositionProviderAtPosition(positionPx = status.rect.center),
        onKeyEvent = {
            if (it.type == KeyEventType.KeyDown) {
                when (it.key.nativeKeyCode) {
                    VK_DOWN -> {
                        inputModeManager!!.requestInputMode(InputMode.Keyboard)
                        focusManager!!.moveFocus(FocusDirection.Next)
                        true
                    }
                    VK_UP -> {
                        inputModeManager!!.requestInputMode(InputMode.Keyboard)
                        focusManager!!.moveFocus(FocusDirection.Previous)
                        true
                    }
                    else -> false
                }
            } else false
        },
    ) {
        focusManager = LocalFocusManager.current
        inputModeManager = LocalInputModeManager.current
        content(onDismissRequest)
    }
}
