plugins {
  kotlin("jvm") version "1.5.30"
  `kotlin-dsl`
  `java-library`
}

repositories {
  // Use the plugin portal to apply community plugins in convention plugins.
  gradlePluginPortal()
  mavenCentral()
}

dependencies {
  implementation("org.apache.commons:commons-text:1.10.0")
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}