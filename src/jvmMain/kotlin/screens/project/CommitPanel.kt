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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.eclipse.jgit.diff.DiffEntry
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.VerticalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import org.jetbrains.skiko.Cursor

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun CommitPanel(projectFull: Project) {
    var project by remember { mutableStateOf(projectFull.structural()) }
    var message by remember { mutableStateOf(TextFieldValue("")) }
    var file by remember { mutableStateOf<String?>(null) }
    var amend by remember { mutableStateOf(false) }
    var diff by remember { mutableStateOf(file?.let { projectFull.getFileDiff(it) }) }
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
    val splitterState = rememberSplitPaneState(initialPositionPercentage = .3f)

    Column {
        Box(
            modifier = Modifier.weight(1f),
        ) {
            HorizontalSplitPane(splitPaneState = splitterState) {
                first {
                    FileSelector(project.overview, updateFile, changeStaged)
                }

                second {
                    FileViewer(
                        file,
                        diff,
                    )
                }

                splitter {
                    visiblePart {
                        Box(
                            Modifier
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colors.background),
                        )
                    }

                    handle {
                        Box(
                            Modifier
                                .markAsHandle()
                                .pointerHoverIcon(PointerIcon(Cursor(Cursor.W_RESIZE_CURSOR)))
                                .background(SolidColor(Color.Gray), alpha = 0.50f)
                                .width(3.dp)
                                .fillMaxHeight(),
                        )
                    }
                }
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
    Box(modifier = Modifier.widthIn(100.dp, Dp.Infinity)) {
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
fun FileViewer(
    file: String?,
    diff: Pair<DiffEntry, List<String>>?,
) {
    SelectionContainer {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            val added = diff?.second?.count { it.startsWith('+') } ?: 0
            val deleted = diff?.second?.count { it.startsWith('-') } ?: 0
            Text(file?.let { "$it (+$added -$deleted)\n" } ?: "Select a file\n")

            if (diff != null && diff.first.changeType != DiffEntry.ChangeType.DELETE) {
                if (diff.second.size <= 4) {
                    Text("No diff available")
                    return@Column
                }

                LazyColumn {
                    for (line in diff.second.subList(4, diff.second.lastIndex)) {
                        item {
                            Row {
                                Text(line)
                            }
                        }
                    }
                }
            }
        }
    }
}
