import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import screens.repository.RepositoryScreen

@Composable
fun ApplicationScope.ProjectWindow(
    state: ProjectWindowState,
) = Window(
    onCloseRequest = state::close,
    title = state.project.name,
) {
    MenuBar {
        Menu("File") {
            Item("Close", onClick = { state.close() })
        }
    }

    RepositoryScreen(state.project)
}
