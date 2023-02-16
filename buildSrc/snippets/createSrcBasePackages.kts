/** create package dirs under each subprojects src/module/kotlin
 * based on subproject's extra property: projectPackage
 *
 * val rootPackage: String by rootProject.extra
 * val projectPackage by extra { "${rootPackage}.${project.name.toLowerCase()}" }
 */
based on subproject
tasks.register("createSrcBasePackages") {
    doLast {
        project.subprojects.forEach { sub ->
            val projectPackage: String by sub.extra
            val projectPackageDirString = projectPackage.split('.').joinToString("/")
            sub.pluginManager.let() { when {
                it.hasPlugin("org.jetbrains.kotlin.jvm") -> {
                    sub.sourceSets.forEach { ss: SourceSet ->
                        val ssDir = File("src/${ss.name}/kotlin")
                        if (ssDir.exists()) {
                            mkdir("$ssDir/$projectPackageDirString")
                        }
                    }
                }
                it.hasPlugin("org.jetbrains.kotlin.multiplatform") -> {
                    File(sub.name, "ZZ__${sub.name.toUpperCase()}").createNewFile()
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
