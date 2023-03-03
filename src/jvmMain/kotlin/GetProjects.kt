import java.io.File
import java.nio.file.Path

fun getProjects(): List<Project> {
    val devDir = Path.of(System.getProperty("user.home"), "Developer").toFile()

    return devDir
        .walkTopDown()
        .maxDepth(4)
        .onEnter { it.isDirectory }
        .filter { File(it, ".git").exists() }
        .map { Project(path = it) }
        .filter { it.worksProperly() }
        .toList()
}
