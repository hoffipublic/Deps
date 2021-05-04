import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.serviceOf
import org.gradle.internal.logging.text.StyledTextOutput

object Global {
    val jvmTarget = JavaVersion.VERSION_11
}

const val ESCAPE = '\u001B'

enum class ConsoleColor(baseCode: Int) {
    BLACK(30),
    RED(31),
    GREEN(32),
    YELLOW(33),
    BLUE(34),
    MAGENTA(35),
    CYAN(36),
    LIGHT_GRAY(37),

    DARK_GRAY(90),
    LIGHT_RED(91),
    LIGHT_GREEN(92),
    LIGHT_YELLOW(93),
    LIGHT_BLUE(94),
    LIGHT_MAGENTA(95),
    LIGHT_CYAN(96),
    WHITE(97),

    DEFAULT(-1);

    /** ANSI modifier string to apply the color to the text itself */
    val foreground: String = "$ESCAPE[${baseCode}m"
    /** ANSI modifier string to apply the color the text's background */
    val background: String = "$ESCAPE[${baseCode + 10}m"
}

internal object Color {
    const val RESET = "$ESCAPE[0m"

    fun foreground(string: String, color: ConsoleColor) = color(string, color.foreground)
    fun background(string: String, color: ConsoleColor) = color(string, color.background)
    private fun color(string: String, ansiString: String) = "$ansiString$string$RESET"
}
/** appearing on Solarized color theme like:
 * style Normal:
 * style Header:         white
 * style UserInput:      white
 * style Identifier:     yellow
 * style Description:    orange
 * style ProgressStatus: orange
 * style Success:        yellow
 * style SuccessHeader:  DarkGray
 * style Failure:        red
 * style FailureHeader:  LightRed
 * style Info:           orange
 *
 *         printlnColor(project, org.gradle.internal.logging.text.StyledTextOutput.Style.Failure, "some given output in Failure color")
*/
fun printlnColor(project: Project, style: StyledTextOutput.Style, s: String) {
    // import org.gradle.configurationcache.extensions.serviceOf
    // import org.gradle.internal.logging.text.StyledTextOutput
    val out = project.serviceOf<org.gradle.internal.logging.text.StyledTextOutputFactory>().create("an-output")
    out.style(style).println(s)
}

/**
 * printlnColor(ConsoleColor.GREEN, "some given colored output")
 */
fun printlnColor(color: ConsoleColor, s: String, backgroundColor: ConsoleColor = ConsoleColor.DEFAULT) = printColor(color, "$s\n", backgroundColor)
fun printColor(color: ConsoleColor, s: String, backgroundColor: ConsoleColor = ConsoleColor.DEFAULT) {
    print(colorString(color, s, backgroundColor))
}
fun colorString(color: ConsoleColor, s: String, backgroundColor: ConsoleColor = ConsoleColor.DEFAULT) : String {
    return when (backgroundColor) {
        ConsoleColor.DEFAULT -> Color.foreground(s, color)
        else -> Color.foreground(Color.background(s, backgroundColor), color)
    }
}

