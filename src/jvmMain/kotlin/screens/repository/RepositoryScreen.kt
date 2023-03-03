package screens.repository

import Commit
import Project
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import io.kamel.core.Resource
import io.kamel.image.lazyPainterResource
import java.security.MessageDigest

@Composable
@Preview
fun RepositoryScreen(project: Project) {
    Column(
        Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize(),
    ) {
        var selectedBranch by remember { mutableStateOf(project.currentBranch()) }
        var selectedCommit by remember { mutableStateOf<Commit?>(null) }
        val selectBranch = { it: String -> selectedBranch = it }
        val selectCommit = { it: Commit -> selectedCommit = it }

        Row {
            BranchList(project, selectedBranch, selectBranch)

            Divider(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
            )
            Column {
                val commitListWeight = if (selectedCommit == null) 1.0f else 0.6f
                val commitPanelWeight = 1.0f - commitListWeight

                Box(Modifier.weight(commitListWeight)) {
                    CommitList(project, selectedBranch, selectCommit)
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
    }
}

@Composable
fun Gravatar(commit: Commit) {
    val gravatarUrl = "https://www.gravatar.com/avatar/${gravatarId(commit.author, commit.email)}"

    when (val resource = lazyPainterResource(gravatarUrl)) {
        is Resource.Loading -> Text("Loading")
        is Resource.Failure -> Text("Failed")
        is Resource.Success -> Image(
            painter = resource.value,
            contentDescription = "Avatar",
        )
    }
}

fun gravatarId(name: String, email: String?): String {
    val md = MessageDigest.getInstance("MD5")
    val input = (email ?: name).lowercase().trim().toByteArray()

    return md
        .digest(input)
        .joinToString(separator = "") { byte -> "%02x".format(byte) }
}
