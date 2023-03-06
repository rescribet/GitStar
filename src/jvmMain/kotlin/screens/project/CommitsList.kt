package screens.project

import Commit
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun CommitList(
    commits: List<Commit>,
    selected: Commit?,
    selectCommit: (Commit) -> Unit,
) {
    val highlightColor = MaterialTheme.colors.secondaryVariant
    val highlightFontColor = MaterialTheme.colors.onPrimary

    Box(Modifier.padding(10.dp)) {
        LazyColumn {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .heightIn(10.dp, 50.dp),
                ) {
                    Text(
                        modifier = Modifier.weight(.6f),
                        text = "Message",
                    )
                    Divider(
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .heightIn(5.dp, 20.dp)
                            .fillMaxHeight()
                            .padding(horizontal = 2.dp)
                            .width(2.dp),
                    )
                    Text(
                        modifier = Modifier.weight(.2f),
                        text = "Author",
                    )
                    Divider(
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .heightIn(5.dp, 20.dp)
                            .fillMaxHeight()
                            .padding(horizontal = 5.dp)
                            .width(2.dp),
                    )
                    Text(
                        modifier = Modifier.weight(.2f),
                        text = "commit date",
                    )
                }
            }

            for (commit in commits) {
                val background = if (commit == selected) highlightColor else Color.Unspecified
                val color = if (commit == selected) highlightFontColor else Color.Unspecified

                item {
                    Box(
                        modifier = Modifier
                            .pointerHoverIcon(PointerIconDefaults.Hand)
                            .clickable { selectCommit(commit) }
                            .fillMaxWidth()
                            .heightIn(10.dp, 50.dp)
                            .background(background)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                    ) {
                        Row {
                            Text(
                                modifier = Modifier.weight(.6f),
                                text = commit.message,
                                color = color,
                            )
                            Text(
                                modifier = Modifier.weight(.2f),
                                text = commit.author,
                                color = color,
                            )
                            Text(
                                modifier = Modifier.weight(.2f),
                                text = commit.commitDate.toString(),
                                color = color,
                            )
                        }
                    }
                }
            }
        }
    }
}
