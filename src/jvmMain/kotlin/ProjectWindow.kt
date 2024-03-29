import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import screens.project.ProjectScreen

@Composable
fun ApplicationScope.ProjectWindow(
    state: ProjectWindowState,
) = Window(
    state = rememberWindowState(width = Dp.Unspecified, height = Dp.Unspecified),
    onCloseRequest = state::close,
    title = "${state.project.name} - ${Constants.appName}",
) {
//    var project by remember { mutableStateOf(state.project, neverEqualPolicy()) }
//    val refresh = { project = state.project }

    MenuBar {
        Menu("File") {
            Item("Close", onClick = { state.close() })
        }
    }

    ProjectScreen(state.project)
}
