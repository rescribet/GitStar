package screens

import Project
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import getProjects
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File

fun openRepo(dir: File): Repository = FileRepositoryBuilder()
    .setGitDir(File(dir, ".git"))
    .readEnvironment()
    .build()

@Composable
@Preview
fun ProjectPickerScreen(onOpen: (project: Project) -> Unit) {
    val projects = getProjects()

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            val stateVertical = rememberScrollState(0)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(stateVertical)
                    .padding(end = 12.dp, bottom = 12.dp),
            ) {
                Column {
                    for (item in projects) {
                        ProjectRow(item, onClick = onOpen)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(stateVertical),
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun ProjectRow(project: Project, onClick: (project: Project) -> Unit) {
    val branchIcon = painterResource("account_tree_black.svg")
    val text = project.name

    val inlineContentMap = mapOf(
        "branchIcon" to InlineTextContent(
            Placeholder(20.sp, 20.sp, PlaceholderVerticalAlign.TextCenter),
        ) {
            Icon(
                painter = branchIcon,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "branch",
            )
        },
    )

    val annotatedString = buildAnnotatedString {
        appendInlineContent("branchIcon")
        append(project.currentBranch())
    }

    Box(
        modifier = Modifier
            .heightIn(32.dp, 64.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .pointerHoverIcon(PointerIconDefaults.Hand)
            .clickable { onClick(project) }
            .padding(all = 10.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(text = text)

        Box(
            modifier = Modifier
                .border(BorderStroke(2.dp, MaterialTheme.colors.secondary), shape = RoundedCornerShape(10.dp))
                .padding(5.dp)
                .align(Alignment.CenterEnd),
        ) {
            Text(annotatedString, inlineContent = inlineContentMap)
        }
    }
}
