package screens.project

import Constants
import StructuralProject
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun BranchList(
    project: StructuralProject,
    selected: String,
    handleSelect: (branch: String) -> Unit,
    handleCommit: () -> Unit,
    handlePush: () -> Unit,
) {
    val scroller = rememberScrollState(0)

    Box(
        Modifier
            .verticalScroll(scroller)
            .padding(10.dp)
            .widthIn(30.dp, 200.dp),
    ) {
        Column {
            Row {
                Button(onClick = handleCommit) {
                    Text("Commit")
                }
                Button(onClick = handlePush) {
                    Text("Push")
                }
            }
            Text(Constants.local, fontSize = 25.sp)
            for (branch in project.branches) {
                Branch(
                    branch,
                    branch == selected,
                    branch == project.currentBranch,
                    handleSelect,
                )
            }
            Divider(modifier = Modifier.padding(vertical = 10.dp))
            Text(Constants.remote, fontSize = 25.sp)
            for (branch in project.remoteBranches) {
                Branch(
                    branch,
                    branch == selected,
                    branch == project.currentBranch,
                    handleSelect,
                )
            }
        }
    }

    VerticalScrollbar(
        modifier = Modifier.fillMaxHeight(),
        adapter = rememberScrollbarAdapter(scroller),
    )
}
