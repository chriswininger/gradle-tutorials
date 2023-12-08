Gradle Notes
=============


[Pluralsight Course](https://app.pluralsight.com/course-player?courseId=a61c8be3-b536-44cd-b900-fe33b9c9405e)

The course is using Gradle 7.2 and says right up front it is not applicable to 8 which is what most people are using :-(

Gradle like maven is conventions based, but it's highly configurable

It uses a DSL (domain specific language) rather than xml

Builds many languages, java, kotlin, c++, etc

Supports dependency management

## Build scripts

The first thing you create is a build script which can be done in kotlin or goovy

### Tasks

The build script defines task: build, clean, etc...

Tasks can have a doFirst and a doLast defined

### Plugins

plugins are a way of extending gradle and adding tasks

for example `apply plugin: 'java'` will add many tasks

by convention it will look for files inside source/main

`apply plugin: 'java'` has been superceded by

```
plugins {
  id 'java'
}

```

### Gradle Wrapper

notice when you run gradle tasks, there's a task called wrapper, if you run gradle wrapper it will create all the gradlew stuff which means that someone running your project doesn't need to install gradle globally 

