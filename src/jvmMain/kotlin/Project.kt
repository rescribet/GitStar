import com.github.syari.kgit.KGit
import com.rescribet.gitstar.SystemCredentialsProvider
import com.rescribet.gitstar.SystemGPGSigner
import kotlinx.datetime.Instant
import org.eclipse.jgit.api.ListBranchCommand
import org.eclipse.jgit.api.errors.NoHeadException
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.lib.ProgressMonitor
import org.eclipse.jgit.treewalk.filter.PathFilter
import screens.openRepo
import java.io.ByteArrayOutputStream
import java.io.File

data class Commit(
    val message: String,
    val author: String,
    val email: String?,
    val commitDate: Instant,
    val ref: String,
)

fun String.prettyName() = this
    .removePrefix("refs/heads/")
    .removePrefix("refs/remotes/")

data class Configuration(
    val gpgProgram: String? = null,
)

data class Project(
    val path: File,
    val name: String = path.name,
) {
    private lateinit var git: KGit
    private lateinit var config: Configuration

    init {
        try {
            git = KGit.wrap(openRepo(path))
            config()
        } catch (e: Exception) {
//            e.printStackTrace()
            println("Error opening ${path.path}")
        }
    }

    fun config() {
        config = Configuration(
            gpgProgram = git.repository.config.getString("gpg", null, "program"),
        )
    }

    fun currentBranch(): String = try {
        git.repository.fullBranch ?: "-"
    } catch (e: FileSystemException) {
        "err"
    }

    fun branches(): List<String> = git
        .branchList()
        .map { it.name }

    fun push() {
        val monitor = object : ProgressMonitor {
            override fun start(totalTasks: Int) {
                println("total: $totalTasks")
            }

            override fun beginTask(title: String?, totalWork: Int) {
                println("$title, $totalWork")
            }

            override fun update(completed: Int) {
                println("completed: $completed")
            }

            override fun endTask() {
                println("end")
            }

            override fun isCancelled(): Boolean {
                return false
            }
        }

        git.push {
            progressMonitor = monitor
            add(currentBranch())
            setCredentialsProvider(SystemCredentialsProvider(config))
//            call()
        }
    }

    fun remoteBranches(): List<String> = git
        .branchList { setListMode(ListBranchCommand.ListMode.REMOTE) }
        .map { it.name }
        .toList()

    fun switch(branch: String) {
        git.checkout {
            setName(branch)
        }
    }

    fun worksProperly(): Boolean = currentBranch() != "-"

    fun getFileDiff(file: String): Pair<DiffEntry, List<String>> = ByteArrayOutputStream().use { stream ->
        val entry = git.diff {
            setCached(overview().staged.map { it.newPath ?: it.oldPath }.contains(file))
            setContextLines(2)
            setPathFilter(PathFilter.create(file))
            setShowNameAndStatusOnly(false)
            setOutputStream(stream)
        }

        Pair(entry.first(), String(stream.toByteArray()).split("\n"))
    }

    fun overview(): Overview = Overview(
        isClean = git.status().isClean,
        staged = git.diff { setCached(true) },
        unstaged = git.diff { setCached(false) },
    )

    fun commit(message: String, amend: Boolean) {
        git.commit {
            this.message = message
            setGpgSigner(SystemGPGSigner(config))
            setAmend(amend)
        }
    }

    fun commits(): List<Commit> = try {
        git
            .log { setMaxCount(100) }
            .map {
                Commit(
                    message = it.shortMessage,
                    author = it.authorIdent.name,
                    email = it.authorIdent.emailAddress,
                    commitDate = Instant.fromEpochSeconds(it.commitTime.toLong(), 0),
                    ref = it.id.name,
                )
            }
    } catch (e: NoHeadException) {
        emptyList()
    }

    fun stage(it: DiffEntry) = git.add { addFilepattern(it.newPath) }

    fun unstage(it: DiffEntry): Nothing = TODO("")

    fun structural(): StructuralProject = StructuralProject(
        path = path,
        name = name,
        currentBranch = currentBranch(),
        commits = commits(),
        branches = branches(),
        remoteBranches = remoteBranches(),
        overview = overview(),
    )

    override fun hashCode(): Int {
        return structural().hashCode()
    }
}

data class StructuralProject(
    val path: File,
    val name: String,
    val currentBranch: String,
    val commits: List<Commit>,
    val branches: List<String>,
    val remoteBranches: List<String>,
    val overview: Overview,
)

data class Overview(
    val isClean: Boolean,
    val staged: List<DiffEntry>,
    val unstaged: List<DiffEntry>,
)
