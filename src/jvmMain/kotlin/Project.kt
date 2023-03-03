import com.github.syari.kgit.KGit
import kotlinx.datetime.Instant
import org.eclipse.jgit.api.ListBranchCommand
import org.eclipse.jgit.api.errors.NoHeadException
import screens.openRepo
import java.io.File

data class Commit(
    val message: String,
    val author: String,
    val email: String?,
    val commitDate: Instant,
    val ref: String,
)

data class Project(
    val path: File,
    val name: String = path.name,
) {
    private lateinit var git: KGit

    init {
        try {
            git = KGit.wrap(openRepo(path))
        } catch (e: Exception) {
            println("Error opening ${path.path}")
            e.printStackTrace()
        }
    }

    fun currentBranch(): String = git.repository.branch ?: "-"

    fun branches(): List<String> = git
        .branchList()
        .map { it.name.removePrefix("refs/heads/") }

    fun remoteBranches(): List<String> = git
        .branchList { setListMode(ListBranchCommand.ListMode.REMOTE) }
        .map { it.name.removePrefix("refs/remotes/") }

    fun worksProperly(): Boolean = currentBranch() != "-"

    fun commits(): List<Commit> = try {
        git
            .log { setMaxCount(100) }
            .map {
                Commit(
                    message = it.shortMessage,
                    author = it.authorIdent.name,
                    email = it.authorIdent.emailAddress,
                    commitDate = Instant.fromEpochSeconds(it.commitTime.toLong(), 0),
                    ref = it.id.name
                )
            }
    } catch (e: NoHeadException) {
        emptyList()
    }
}
