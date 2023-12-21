plugins {
    application
    kotlin("jvm") version "1.3.31"
}

application {
  mainClass.set("com.pluralsight.MainKt")
}

group = "com.pluralsight"
version = "1.0-SNAPSHOT"


repositories {
  mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}


// To change our target options with Kotlin we have to override tasks
tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
  compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
  }
}