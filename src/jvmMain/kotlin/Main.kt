import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import screens.ProjectPickerScreen

fun main() = application {
    val applicationState = remember { ApplicationState() }
    var isVisible by remember { mutableStateOf(true) }
    val icon = painterResource("gitstar.svg")

    Tray(
        icon = icon,
        tooltip = Constants.appName,
        onAction = {},
        menu = {
            Item(Constants.projects, onClick = { isVisible = true })
            Item(Constants.exit, onClick = ::exitApplication)
        },
    )

    Window(
        onCloseRequest = { isVisible = false },
        visible = isVisible,
        title = Constants.appName,
        icon = icon,
    ) {
        ProjectPickerScreen(onOpen = { applicationState.openNewWindow(it) })
    }

    for (window in applicationState.windows) {
        key(window) {
            ProjectWindow(window)
        }
    }
}
