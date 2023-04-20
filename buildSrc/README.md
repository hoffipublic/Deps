# Deps buildSrc/README.md

structure of build.gradle.kts


in rootProject:

```kotlin
group = "com.hoffi"
version = "1.0-SNAPSHOT"
val artifactName: String by extra { "${rootProject.name.lowercase()}-${project.name.lowercase()}" }
val rootPackage: String by extra { "${rootProject.group}.${rootProject.name.replace("[-_]".toRegex(), "").lowercase()}" }
val projectPackage: String by extra { rootPackage }
val theMainClass: String by extra { "Main" }
application {
    mainClass.set("${rootPackage}.${theMainClass}" + "Kt") // + "Kt" if fun main is outside a class
}
```

in subprojects:

```kotlin
group = "${rootProject.group}"
version = "${rootProject.version}"
val artifactName: String by extra { "${rootProject.name.lowercase()}-${project.name.lowercase()}" }
val rootPackage: String by rootProject.extra
val projectPackage: String by extra { "${rootPackage}.${project.name.lowercase()}" }
val theMainClass: String by extra { "Main" }
application {
    mainClass.set("${projectPackage}.${theMainClass}" + "Kt") // + "Kt" if fun main is outside a class
}
```
