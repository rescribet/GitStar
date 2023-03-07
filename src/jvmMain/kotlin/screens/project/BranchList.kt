package screens.project

import Constants
import StructuralProject
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.VerticalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import org.jetbrains.skiko.Cursor

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
internal fun BranchList(
    project: StructuralProject,
    selected: String,
    handleSelect: (branch: String) -> Unit,
    handleCommit: () -> Unit,
    handlePush: () -> Unit,
) {
    val splitterState = rememberSplitPaneState()

    VerticalSplitPane(
        splitPaneState = splitterState,
    ) {
        first(400.dp) {
            Column {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Button(onClick = handleCommit) {
                        Text("Commit")
                    }
                    Button(onClick = handlePush) {
                        Text("Push")
                    }
                }

                BranchesList(
                    Constants.local,
                    project,
                    project.branches,
                    selected,
                    handleSelect,
                )
            }
        }

        second {
            BranchesList(
                Constants.remote,
                project,
                project.remoteBranches,
                selected,
                handleSelect,
            )
        }

        splitter {
            visiblePart {
                Box(
                    Modifier
                        .height(3.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background),
                )
            }
            handle {
                Box(
                    Modifier
                        .markAsHandle()
                        .pointerHoverIcon(PointerIcon(Cursor(Cursor.S_RESIZE_CURSOR)))
                        .background(SolidColor(Color.Gray), alpha = 0.50f)
                        .height(3.dp)
                        .fillMaxWidth(),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BranchesList(
    header: String,
    project: StructuralProject,
    branches: List<String>,
    selected: String,
    handleSelect: (branch: String) -> Unit,
) {
    LazyColumn {
        stickyHeader {
            Text(
                header,
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .background(MaterialTheme.colors.background)
                    .fillMaxWidth(),
            )
        }

        for (branch in branches) {
            item {
                Branch(
                    branch,
                    branch == selected,
                    branch == project.currentBranch,
                    handleSelect,
                )
            }
        }
    }
}
