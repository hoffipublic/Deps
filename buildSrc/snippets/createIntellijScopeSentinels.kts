/** creating `01__PRJNAME/.gitkeep` and `ZZ__PRJNAME` files in each kotlin mpp project
 * as well as `_srcModule_PRJNAME/.gitkeep` and `ZZsrcModule_PRJNAME` files in each main sourceSets of these
 *
 * .gitignore:
 * <block>
 *  .idea/
 *  !.idea/scopes/
 *  !.idea/fileColors.xml
 * </block>
 *
 * if you had .idea/ ignored before, try
 * <block>
 * git rm --cached .idea/filename
 * git add --forced .idea/filename
 * </block>
 *
 * e.g. define scopes (in Settings... `Scopes`):
 * - scope 00__ (scope with all folders where the name starts with: 0[0-3]__, meaning the first folder
 * - scope src with _src.../ or ZZsrc... (scope with all folders where the name starts with _src)
 * - scope buildfiles (e.g. build.gradle.kts)
 *
 * and then in Settings ... `File Colors` add the scope(s) and give them a color .
 *
 * If you _then_ add folders / files matching the above scope names
 * you can see more clearly which "area" of code in the folder structure you are just looking at the moment .
 */
tasks.register("createIntellijScopeSentinels") {
    doLast {
        project.subprojects.forEach { sub ->
            sub.pluginManager.let() { when {
                it.hasPlugin("org.jetbrains.kotlin.jvm") -> {
                    sub.sourceSets.forEach { ss: SourceSet ->
                        val ssDir = File("src/${ss.name}")
                        if (ssDir.exists()) {
                            println("jvmSubProject: " + sub.name + " -> " + ssDir)
                        }
                    }
                }
                it.hasPlugin("org.jetbrains.kotlin.multiplatform") -> {
                    var d = mkdir("${sub.name}/01__${sub.name.toUpperCase()}")
                    File(d, ".gitkeep").createNewFile()
                    File(sub.name, "ZZ__${sub.name.toUpperCase()}").createNewFile()
                    sub.kotlin.sourceSets.forEach { ss: org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet ->
                        val ssDir = File("${sub.name}/src/${ss.name}")
                        if (ssDir.exists()) {
                            if (ss.name.endsWith("Main")) {
                                val mName = ss.name.removeSuffix("Main")
                                d = mkdir("$ssDir/_src${mName.capitalize()}_${sub.name.toUpperCase()}")
                                File(d, ".gitkeep").createNewFile()
                                File(ssDir, "ZZsrc${mName.capitalize()}_${sub.name.toUpperCase()}").createNewFile()
                            }
                        }
                    }
                }
            }}
        }
    }
}
