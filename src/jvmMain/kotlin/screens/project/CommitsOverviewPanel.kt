package screens.project

import Commit
import StructuralProject
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun CommitsOverviewPanel(project: StructuralProject) {
    var selectedCommit by remember { mutableStateOf<Commit?>(null) }
    val selectCommit = { it: Commit -> selectedCommit = it }

    val commitListWeight = if (selectedCommit == null) 1.0f else 0.6f
    val commitPanelWeight = 1.0f - commitListWeight

    Column {
        Box(Modifier.weight(commitListWeight)) {
            CommitList(project.commits, selectedCommit, selectCommit)
        }

        selectedCommit?.let {
            Box(Modifier.weight(commitPanelWeight)) {
                Divider(
                    color = MaterialTheme.colors.onSurface,
                    thickness = 1.dp,
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                ) {
                    SelectionContainer {
                        Column {
                            Row(
                                modifier = Modifier.padding(bottom = 5.dp),
                            ) {
                                Gravatar(it)
                                Column(
                                    modifier = Modifier.padding(start = 10.dp),
                                ) {
                                    val email = it.email?.let { "<$it>" }
                                    Text("${it.author} $email")
                                    Text("Date: ${it.commitDate}")
                                }
                            }
                            Text("Ref: ${it.ref}")
                            Text(it.message)
                        }
                    }
                }
            }
        }
    }
}
