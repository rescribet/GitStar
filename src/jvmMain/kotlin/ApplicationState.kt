import androidx.compose.runtime.mutableStateListOf

class ApplicationState {
    val windows = mutableStateListOf<ProjectWindowState>()

    fun openNewWindow(project: Project) {
        windows += projectWindowState(project)
    }

    private fun projectWindowState(project: Project) = ProjectWindowState(
        project,
        windows::remove,
    )
}
