package screens.repository

import Commit
import Project
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CommitList(
    project: Project,
    branch: String,
    selectCommit: (Commit) -> Unit,
) {
    val scroller = rememberScrollState(0)
    val commits = project.commits()

    Box(
        Modifier
            .verticalScroll(scroller)
            .padding(10.dp),
    ) {
        Column {
            for ((i, commit) in commits.withIndex()) {
                val color = if (i.mod(2) == 0) Color.LightGray else Color.Unspecified

                Box(
                    modifier = Modifier
                        .pointerHoverIcon(PointerIconDefaults.Hand)
                        .clickable { selectCommit(commit) }
                        .fillMaxWidth()
                        .background(color)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                ) {
                    Row {
                        Text(
                            modifier = Modifier.weight(.6f),
                            text = commit.message,
                        )
                        Text(
                            modifier = Modifier.weight(.2f),
                            text = commit.author,
                        )
                        Text(
                            modifier = Modifier.weight(.2f),
                            text = commit.commitDate.toString(),
                        )
                    }
                }
            }
        }
    }

    VerticalScrollbar(
        modifier = Modifier.fillMaxHeight(),
        adapter = rememberScrollbarAdapter(scroller),
    )
}
