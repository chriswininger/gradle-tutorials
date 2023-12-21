val log4j_version: String by project
val jaxb_version: String by project
val junit_version: String by project

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
    implementation("log4j:log4j:$log4j_version")
    implementation("javax.xml.bind:jaxb-api:$jaxb_version")
    testImplementation("junit:junit:$junit_version")
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
