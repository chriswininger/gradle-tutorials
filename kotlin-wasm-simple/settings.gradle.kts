rootProject.name = "kotlin-wasm-simple"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

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

include(":app")
