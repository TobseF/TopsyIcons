import java.io.File

/**
 * Just a Kotlin script which generates the HTML preview and the Markdown table of all icons in the resources folder.
 */

fun main() {
    val icons = File("src/main/resources").walk().filter { it.isPngIcon() }
        .toList()

    assertExistingIconSVG(icons)

    generatePreviewHTML(icons)

    generatePreviewMarkdown(icons)

    generateTagList(icons)
}

private fun generatePreviewMarkdown(icons: List<File>) {
    val columns = 7
    icons.chunked(columns).forEach { row ->
        generateTableLine(row)
    }
}

private fun generatePreviewHTML(icons: List<File>) {
    icons.forEach { image ->
        val imageHtml = "<img src=\"${image.resource()}\" alt=\"${image.asTag()} Icon\">";
        println(imageHtml)
    }
}

private fun assertExistingIconSVG(icons: List<File>) {
    icons.forEach {
        if (!it.getIconSVG().exists()) {
            throw IllegalStateException("Missing svg file: " + it.absoluteFile)
        }
    }
}

var firstRow = true
private fun generateTableLine(row: List<File>) {
    if (firstRow){
        val header = "|TopsyIcons|"
        val separator = row.joinToString("|") { "---" }.wrap()
        println(header)
        println(separator)
        firstRow = false
    }
    val tags = row.joinToString("|") { it.asTag() }.wrap()
    val iconImages = row.joinToString("|") { "![${it.asTag()} Icon](${it.resource()})" }.wrap()

    println(iconImages)
    println(tags)
}

private fun generateTagList(icons: List<File>) {
    println("# Tags")
    val tags = icons.joinToString(", ") { it.asTag()+" Icon" }
    println(tags)
}


fun File.resource() = "src/main/resources/${this.name}"

fun File.getIconSVG() = File(this.absolutePath.replaceAfter(".", "svg"))

fun File.asTag() = this.nameWithoutExtension.replace("_", " ")

fun String.wrap() = "|$this|"

fun File.isPngIcon(): Boolean {
    return this.isFile && this.name.lowercase().endsWith(".png")
}

