Kotlin WASM Simple
====================

<!-- TOC -->
* [Kotlin WASM Simple](#kotlin-wasm-simple)
  * [Overview](#overview)
  * [Breaking Down the Example](#breaking-down-the-example)
    * [The Kotlin Multiplatform Plugin](#the-kotlin-multiplatform-plugin)
      * [Directory Structure](#directory-structure)
      * [expect](#expect)
      * [Plugin Settings](#plugin-settings)
      * [index.html](#indexhtml)
      * [gradle.properties](#gradleproperties)
    * [Version Catalogs](#version-catalogs)
    * [Compose UI](#compose-ui)
    * [JetBrains Compose Plugin](#jetbrains-compose-plugin)
  * [Links](#links)
<!-- TOC -->

## Overview

This was adapted from the compose-example found under [the repo kotlin-wasm-examples](https://github.com/kotlin/kotlin-wasm-examples)
as pointed to from the [Kotlin WASM Getting Started Guide](https://kotlinlang.org/docs/wasm-get-started.html#run-the-application)

The original example was building for multiple targets, ios, android, desktop, js, and wasm. I wanted to
understand how to simply build a pure wasm app.

## Breaking Down the Example

There several pieces to understand in the original example

1. [the kotlin multiplatform plugin](#the-kotlin-multiplatform-plugin)
2. [gradle and various gradle features like version catalogs](#version-catalogs)
3. [androidx compose ui libraries](#compose-ui)
4. [the jetbrains compose plugin](#jetbrains-compose-plugin)
 
### The Kotlin Multiplatform Plugin

#### Directory Structure

While the goal is to compile Kotlin to wasm the recommended approach is through
using [the Kotlin Multiplatform Plugin](https://kotlinlang.org/docs/multiplatform-get-started.html)

This plugin does a lot and will dictate much of how the project is laid out. It's often used to support multi-platform
mobile builds, and thus a lot of the tutorials you'll find out there focus on that. WASM comppilation is the new kid on
the block.

One of the best guides I found was this https://www.baeldung.com/kotlin/multiplatform-programming.

You'll notice there is a [commonMain](./app/src/commonMain) and [wasmJsMain](./app/src/wasmJsMain). For my purposes
since I'm building for a single platform this isn't really needed, but the way this is supposed to work is that
code which can be shared across platforms goes in commonMain. For each target platform, such as wasmJs you'll have a
{platform}Main folder. For example, if I was building for the desktop I would have a desktopMain.

#### expect

You'll notice that in my commonMain I have [Platform.kt](./app/src/commonMain/kotlin/Platform.kt) which contains some odd
interface code:

```kotlin
interface Platform {
  val name: String
}

expect fun getPlatform(): Platform
```

This allows me to declare functions and interfaces that should be implemented within each platform. So you'll notice that
under wasmJsMain I implement [it](./app/src/wasmJsMain/kotlin/Platform.wasmJs.kt)

```kotlin
private val platform = object : Platform {
  override val name: String
    get() = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = platform
```

_In my super trimmed down example, I'm not even using Platform anymore, but I'm leaving the files here to show how this works_

#### Plugin Settings

In [app/build.gradle.kts](./app/build.gradle.kts) I configure the plugin under a `kotlin` block

```kotlin
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    moduleName = "app"
    browser {
      commonWebpackConfig {
        outputFileName = "app.js"
      }
    }
    binaries.executable()
  }
```

Most of this is self explanitory, our module is called app and I am giving it some info about how to output the file.
You can see from `@OptIn(ExperimentalWasmDsl::class)` that are being adventurous :-)

_Also notice the outputFileName, you'll need this later :-)_

Another important section is:

```kotlin
  sourceSets {
   commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material)
      implementation(compose.ui)
      @OptIn(ExperimentalComposeLibrary::class)
      implementation(compose.components.resources)
    }
  }
```

This is how I include our dependencies. It's a little differnt than what you'll see with the standard java application
or library plugin.

I can specify different dependencies for each block. In this case I am stuffing everything into commonMain, but
I could have wasmJs.dependencies, or if I were building for desktop desktop.dependencies.

Those dependencies would be isolated to that part of that platform.

#### index.html

Under wasmJsMain resources, it's important that I place an [index.html](./app/src/wasmJsMain/resources/index.html) file.
This was not obvious to me at first. If it's not there all you get rendered is a directory listing. Notice inside this
file that I pull down some js files, in particular

```
    <script type="application/javascript" src="app.js"></script>
```

**This must match outputFileName specified in build.gradle.js under the wasmJs block**

#### gradle.properties

Another important file to make this work is gradle.properites. I mostly copy/pasted it from the orginal project but without
this you won't be able to compile (at least if you are using compose)


### Version Catalogs

Notice how in [build.gradle.kts](./app/build.gradle.kts) when I import the plugins I specify them
without strings using alias `alias(libs.plugins.kotlinMultiplatform)`. As someone new to gradle this was confusing to
me.

This works because the project is making use of a gradle feature called
[version catalogs](https://docs.gradle.org/current/userguide/platforms.html).

This is a useful feature that makes it easy to specify a list of components your modules can choose from and shifts version
management of the modules to central location. The version catalog is defined in the [settings.gradle.kts](./settings.gradle.kts).

```kotlin
dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      val compose = version("compose", "1.5.4")

      plugin("jetbrainsCompose", "org.jetbrains.compose").version("1.6.0-alpha01")
      plugin("kotlinMultiplatform", "org.jetbrains.kotlin.multiplatform").version("1.9.21")
      library("androidx","androidx.compose.ui", "ui").versionRef(compose)
    }
  }
}
```

Confusingly you will not find the above code snippet in the original example. They use a more advanced approach
where the versionCatalog is defined in a file called lib.versions.toml under the gradle directory. This may be a better
way to manage dependencies but to keep things simple I have not duplicated it here.

```
kotlin.code.style=official

#Gradle
org.gradle.jvmargs=-Xmx2048M -Dfile.encoding=UTF-8 -Dkotlin.daemon.jvm.options\="-Xmx2048M"


#Compose for Web is Experimental
org.jetbrains.compose.experimental.wasm.enabled=true

#MPP
kotlin.mpp.androidSourceSetLayoutVersion=2
kotlin.mpp.enableCInteropCommonization=true

#Development
development=true
```

### Compose UI

This sample app uses [Compose UI](https://developer.android.com/jetpack/compose). It turns out there was [a simpler
example](https://github.com/Kotlin/kotlin-wasm-examples/tree/main/browser-example) 
I could have started with that dodges the compose requirement, but this is the path I went down :-)

Our application entry point for wasm is  the `main` function under [main.kt](./app/src/wasmJsMain/kotlin/main.kt)

```kotlin
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
  CanvasBasedWindow(canvasElementId = "ComposeTarget") { App() }
}
```

where CanvasBasedWindow comes from `import androidx.compose.ui.window.CanvasBasedWindow`.

Notice `canvasElementId = "ComposeTarget"`. This needs to match what we use in the
[index.html file](./app/src/wasmJsMain/resources/index.html).

`<canvas id="ComposeTarget"></canvas>`.

Our application will get drawn to a canvas. This is an interesting approach. It means we are simply appending equivalent
elements like buttons to the dom, but rather doing some sort of rendering in our web assembly application and dumping
the resulting pixels to a canvas. I can think of many pros and cons to this approach :-)

The idea of this project is that for each implementation outer later will very, for example if we were handling desktop,
instead of CanvasBasedWindow we'd have something like

```kotlin
fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
```

where Window comes from `import androidx.compose.ui.window.Window`. Most of the actual UI lives inside the App
function defined in commonMain. In this case it's super simple even beyond what was in the original example:

```kotlin
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun App() {
  MaterialTheme {
    val greetingText by remember { mutableStateOf("Hello World!") }

    Text(greetingText)
  }
}
```

### JetBrains Compose Plugin

The [JetBrains Compose Plugin](https://github.com/JetBrains/compose-multiplatform) is part of what allows this magic
to happen, allowing us to make use of the compose ui, which was originally intended for android app develpment to be used
across many different platforms

You can see where we import it into our version catalog in [settings.gradle.kts](./settings.gradle.kts)

`plugin("jetbrainsCompose", "org.jetbrains.compose").version("1.6.0-alpha01")`

## Links

* https://docs.gradle.org/current/userguide/platforms.html
* https://kotlinlang.org/docs/multiplatform-dsl-reference.html#jvm-targets
* https://www.baeldung.com/kotlin/multiplatform-programming
* https://www.baeldung.com/kotlin/multiplatform-programming
