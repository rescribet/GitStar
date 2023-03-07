package screens.project

import Overview
import Project
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.eclipse.jgit.diff.DiffEntry
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.VerticalSplitPane
import org.jetbrains.skiko.Cursor

@Composable
fun CommitPanel(projectFull: Project) {
    var project by remember { mutableStateOf(projectFull.structural()) }
    var message by remember { mutableStateOf(TextFieldValue("")) }
    var file by remember { mutableStateOf<String?>(null) }
    var amend by remember { mutableStateOf<Boolean>(false) }
    var diff by remember { mutableStateOf(file?.let { projectFull.getFileDiff(it) } ?: emptyList()) }
    val updateFile = { it: DiffEntry ->
        file = it.newPath
        diff = projectFull.getFileDiff(it.newPath)
    }
    val changeStaged: (Boolean, DiffEntry) -> Unit = { checked, it ->
        if (checked) {
            projectFull.stage(it)
        } else {
            projectFull.unstage(it)
        }
        project = projectFull.structural()
    }
    val commit = {
        projectFull.commit(message.text, amend)
        project = projectFull.structural()
    }

    Column {
        Box(
            modifier = Modifier.weight(1f),
        ) {
            Row {
                FileSelector(project.overview, updateFile, changeStaged)
                FileViewer(
                    modifier = Modifier.weight(.8f),
                    file,
                    diff,
                )
            }
        }

        Row(
            modifier = Modifier.weight(.4f),
        ) {
            Column {
                Row(modifier = Modifier.weight(1f)) {
                    TextField(
                        modifier = Modifier.weight(1f, true).fillMaxHeight(),
                        value = message,
                        onValueChange = { value -> message = value },
                        label = { Text("Commit message") },
                    )
                }
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = amend,
                        onCheckedChange = { amend = it },
                        colors = CheckboxDefaults.colors(),
                    )
                    Text("Amend")

                    Spacer(modifier = Modifier.weight(1f))

                    Button(commit) {
                        Text("Commit")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalSplitPaneApi::class)
@Composable
fun FileSelector(overview: Overview, onSelect: (it: DiffEntry) -> Unit, onChangeStaged: (Boolean, DiffEntry) -> Unit) {
    Box(modifier = Modifier.widthIn(100.dp, 500.dp)) {
        if (overview.isClean) {
            Text("Nothing to commit")
        } else {
            VerticalSplitPane {
                first(200.dp) {
                    LazyColumn {
                        stickyHeader {
                            Text(
                                "Staged",
                                modifier = Modifier
                                    .background(MaterialTheme.colors.background)
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                            )
                        }

                        for (change in overview.staged) {
                            val label = when (change.changeType) {
                                DiffEntry.ChangeType.DELETE -> "➖ ${change.oldPath}"
                                DiffEntry.ChangeType.ADD -> "➕ ${change.newPath}"
                                else -> change.newPath
                            }

                            item {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = true,
                                        onCheckedChange = { onChangeStaged(it, change) },
                                        colors = CheckboxDefaults.colors(),
                                    )

                                    Text(
                                        modifier = Modifier
                                            .clickable { onSelect(change) }
                                            .padding(10.dp)
                                            .fillMaxSize(),
                                        text = label,
                                    )
                                }
                            }
                        }
                    }
                }

                second {
                    LazyColumn {
                        stickyHeader {
                            Text(
                                "Unstaged",
                                modifier = Modifier
                                    .background(MaterialTheme.colors.background)
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                            )
                        }

                        for (change in overview.unstaged) {
                            val label = when (change.changeType) {
                                DiffEntry.ChangeType.DELETE -> "➖ ${change.oldPath}"
                                DiffEntry.ChangeType.ADD -> "➕ ${change.newPath}"
                                else -> change.newPath
                            }

                            item {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = false,
                                        onCheckedChange = { onChangeStaged(it, change) },
                                        colors = CheckboxDefaults.colors(),
                                    )

                                    Text(
                                        modifier = Modifier
                                            .clickable { onSelect(change) }
                                            .padding(10.dp)
                                            .fillMaxSize(),
                                        text = label,
                                    )
                                }
                            }
                        }
                    }
                }

                splitter {
                    handle {
                        Divider(
                            modifier = Modifier
                                .markAsHandle()
                                .fillMaxWidth()
                                .pointerHoverIcon(PointerIcon(Cursor(Cursor.S_RESIZE_CURSOR))),
                        )
                    }

                    visiblePart {
                        Divider(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
fun FileViewer(modifier: Modifier? = null, file: String?, diff: List<DiffEntry>) {
    Column(
        modifier = Modifier
            .heightIn(100.dp, 500.dp)
            .widthIn(100.dp, 500.dp),
    ) {
        Text(file ?: "Select a file")

        if (file != null) {
            LazyColumn {
                for (line in diff) {
                    item {
                        Text(line.changeType.name)
                        Text(line.score.toString(10))
                        Text(line.diffAttribute?.key ?: "-")
                        Text(line.diffAttribute?.value ?: "-")
                    }
                }
            }
        }
    }
}
