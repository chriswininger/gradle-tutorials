import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
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
}

compose.experimental {
  web.application {}
}
