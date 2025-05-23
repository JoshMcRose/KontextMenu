<img src="/assets/banner-image.svg" alt="KontextMenu">

<a href="https://search.maven.org/artifact/io.github.joshmcrose/kontextmenu" target="_blank">![Maven Central](https://img.shields.io/maven-central/v/io.github.joshmcrose/kontextmenu?label=Maven%20Central&style=plastic&labelColor=3C4D00&color=B7D167)</a>
![Static Badge](https://img.shields.io/badge/JVM-desktop?style=plastic&label=Desktop&labelColor=3C4D00&color=B7D167)

A flexible, customizable context menu library for Compose for Desktop applications.

## Overview

KontextMenu is a Kotlin library that provides a simple way to create and customize context menus in text fields for Compose for Desktop applications. It offers a clean API with sensible defaults while allowing for extensive customization.

## Features

- 🎨 **Multiple UI Styles**: Ready-to-use implementations for Material and Material3 design systems
- 🧩 **Flexible Architecture**: Create your own context menu representations with a simple interface
- 🔧 **Customizable**: Control every aspect of your context menus from appearance to behavior
- 📝 **Text-Focused**: Specifically designed for text fields
- 🖱️ **Desktop-Ready**: Built for Compose for Desktop with keyboard and mouse support

## Installation

Add the dependency to your `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("io.github.joshmcrose:kontextmenu:0.1.2")
}
```

## Basic Usage

Here's a simple example of how to use KontextMenu with Material3 styling:

```kotlin
@Composable
fun MyTextEditor() {
    var text by remember { mutableStateOf("Select this text to see the context menu") }

    KontextMenu(
        cut = { /* Handle cut operation */ },
        copy = { /* Handle copy operation */ },
        paste = { /* Handle paste operation */ }
    ) {
        TextField(
            value = text,
            onValueChange = { text = it }
        )
    }
}
```

## Components

### KontextMenuArea

The core component that creates the context menu area. It handles the detection of right-clicks and displays the context menu.

```kotlin
KontextMenuArea(
    menuContent = { items, onDismissRequest ->
        // Your custom menu UI here
    },
    builder = { 
        // Add your menu items here
        customItem.add()
        anotherItem.add()
        listOfItems.addAll()
    }
)
```

<img src="/assets/EndUserImpl.png" alt="User Implementation">

### KontextMenuRepresentation

An interface that defines how the context menu is displayed. KontextMenu comes with two implementations:

1. **ContainerizedKontextMenuRepresentation**: A menu representation with a built-in `Popup` container.
2. **UncontainerizedKontextMenuRepresentation**: A menu representation with no containing composable, leaving the menu structure fully up to the end user.

## Pre-built Context Menus

KontextMenu provides ready-to-use context menu implementations:

### Material Design

```kotlin
KontextMenu(
    cut = { /* Handle cut */ },
    copy = { /* Handle copy */ },
    paste = { /* Handle paste */ }
) {
    // Your text field here
}
```

<img src="/assets/MaterialImpl.png" alt="Material Menu">

### Material3 Design

```kotlin
KontextMenu(
    cut = { /* Handle cut */ },
    copy = { /* Handle copy */ },
    paste = { /* Handle paste */ }
) {
    // Your text field here
}
```

<img src="/assets/Material3Impl.png" alt="Material3 Menu">

### Dialog-based Context Menu (Material3)

```kotlin
DialogKontextMenu(
    cut = { /* Handle cut */ },
    copy = { /* Handle copy */ },
    paste = { /* Handle paste */ }
) {
    // Your text field here
}
```

<img src="/assets/DialogImpl.gif" alt="Dialog Menu">

## Advanced Customization

Create your own context menu representation by implementing the `KontextMenuRepresentation` interface:

```kotlin
class MyCustomKontextMenuRepresentation : KontextMenuRepresentation {
    @Composable
    override fun <T> Representation(
        state: ContextMenuState,
        menuContent: @Composable (items: List<T>, onDismissRequest: (() -> Unit)?) -> Unit,
        items: List<T>
    ) {
        // Your custom implementation here
    }
}
```

Then use it with the `KontextMenu` composable:

```kotlin
KontextMenu(
    content = { /* Your content */ },
    textContextMenuRepresentation = MyCustomKontextMenuRepresentation(),
    textContextMenuArea = kontextMenuArea(
        menuContent = { items, onDismissRequest -> 
            // Your custom menu UI 
        },
        builder = { /* Add your items */ }
    )
)
```

<img src="/assets/CustomImpl.png" alt="Custom Menu">

## Documentation

<a href="https://joshmcrose.github.io/KontextMenu/" target="_blank">KontextMenu Docs powered by Dokka</a>

## License

This library is released under the MIT License. See the LICENSE file for details.
