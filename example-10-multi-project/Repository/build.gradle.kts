// sets project seetings for Repository project
project(":Repostiory") {}

// specify that the Service project depends on the repository proejct
project(":Service") {
  dependencies {
    "implementation"(project(":Repostiory"))
  }
}

plugins {
  java
}

version = "1.0-SNAPSHOT"
