package screens.project

import Project
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class ProjectScreens {
    CommitOverview,
    Commit,
}

@Composable
@Preview
fun ProjectScreen(projectFull: Project) {
    Column(
        Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize(),
    ) {
        var activePanel by remember { mutableStateOf(ProjectScreens.CommitOverview) }
        var project by remember { mutableStateOf(projectFull.structural()) }
        var selectedBranch by remember { mutableStateOf(project.currentBranch) }
        val selectBranch = { it: String ->
            activePanel = ProjectScreens.CommitOverview
            if (selectedBranch == it) {
                projectFull.switch(it)
                project = projectFull.structural()
            } else {
                selectedBranch = it
            }
        }

        Row {
            BranchList(
                project,
                selectedBranch,
                selectBranch,
                handleCommit = { activePanel = ProjectScreens.Commit },
                handlePush = { projectFull.push() },
            )

            Divider(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
            )

            when (activePanel) {
                ProjectScreens.CommitOverview -> CommitsOverviewPanel(project)
                ProjectScreens.Commit -> CommitPanel(projectFull)
            }
        }
    }
}
