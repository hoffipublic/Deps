/** create package dirs under each subprojects src/module/kotlin
 * based on subproject's extra property: projectPackage
 *
 * in rootProject:
 * //=============
 * group = "com.hoffi"
 * version = "1.0-SNAPSHOT"
 * val artifactName by extra { "${rootProject.name.toLowerCase()}-${project.name.toLowerCase()}" }
 * val rootPackage by extra { "${rootProject.group}.${rootProject.name.toLowerCase()}" }
 * val theMainClass by extra { "Main" }
 * application {
 *     mainClass.set("${rootPackage}.${theMainClass}" + "Kt") // + "Kt" if fun main is outside a class
 * }
 *
 * in subprojects:
 * //=============
 * group = "${rootProject.group}"
 * version = "${rootProject.version}"
 * val artifactName by extra { "${rootProject.name.toLowerCase()}-${project.name.toLowerCase()}" }
 * val rootPackage: String by rootProject.extra
 * val projectPackage by extra { "${rootPackage}.${project.name.toLowerCase()}" }
 * val theMainClass by extra { "Main" }
 * application {
 *     mainClass.set("${projectPackage}.${theMainClass}" + "Kt") // + "Kt" if fun main is outside a class
 * }
 */
val createSrcBasePackages = tasks.register("createSrcBasePackages") {
    doLast {
        project.subprojects.forEach { sub ->
            val projectPackage: String by sub.extra
            val projectPackageDirString = projectPackage.split('.').joinToString("/")
            sub.pluginManager.let() { when {
                it.hasPlugin("org.jetbrains.kotlin.jvm") -> {
                    sub.sourceSets.forEach { ss: SourceSet ->
                        val ssDir = File("${sub.name}/src/${ss.name}/kotlin")
                        if (ssDir.exists()) {
                            mkdir("$ssDir/$projectPackageDirString")
                        }
                    }
                }
                it.hasPlugin("org.jetbrains.kotlin.multiplatform") -> {
                    sub.kotlin.sourceSets.forEach { ss: org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet ->
                        val ssDir = File("${sub.name}/src/${ss.name}/kotlin")
                        if (ssDir.exists()) {
                            mkdir("$ssDir/$projectPackageDirString")
                        }
                    }
                }
            }}
        }
    }
}
