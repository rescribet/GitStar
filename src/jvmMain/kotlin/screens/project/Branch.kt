package screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import prettyName

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun Branch(
    name: String,
    selected: Boolean,
    checkedOut: Boolean,
    onClick: (name: String) -> Unit,
) {
    val background = if (selected) MaterialTheme.colors.secondary else Color.Unspecified
    val foreground = if (selected) MaterialTheme.colors.onSecondary else Color.Unspecified
    val weight = if (checkedOut) FontWeight.Bold else FontWeight.Normal

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .pointerHoverIcon(PointerIconDefaults.Hand)
            .clickable { onClick(name) }
            .padding(horizontal = 10.dp, vertical = 15.dp),
        text = name.prettyName(),
        color = foreground,
        fontWeight = weight,
    )
}
