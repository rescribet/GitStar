class ProjectWindowState(
    val project: Project,
    private val close: (ProjectWindowState) -> Unit
) {
    fun close() = close(this)
}
