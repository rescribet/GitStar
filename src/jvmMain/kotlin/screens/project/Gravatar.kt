package screens.project

import Commit
import androidx.compose.foundation.Image
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import io.kamel.core.Resource
import io.kamel.image.lazyPainterResource
import java.security.MessageDigest

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

private fun gravatarId(name: String, email: String?): String {
    val md = MessageDigest.getInstance("MD5")
    val input = (email ?: name).lowercase().trim().toByteArray()

    return md
        .digest(input)
        .joinToString(separator = "") { byte -> "%02x".format(byte) }
}
