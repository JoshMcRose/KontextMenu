package io.github.joshmcrose.custom

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class MenuItem(
    val leadingIcon: DrawableResource?,
    val trailingIcon: DrawableResource?,
    val label: StringResource,
    val enabled: Boolean,
    val onClick: (() -> Unit)?
)
