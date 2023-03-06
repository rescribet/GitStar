package com.rescribet.gitstar

import Configuration
import org.eclipse.jgit.lib.CommitBuilder
import org.eclipse.jgit.lib.GpgSigner
import org.eclipse.jgit.lib.PersonIdent
import org.eclipse.jgit.transport.CredentialsProvider

class SystemGPGSigner(val config: Configuration) : GpgSigner() {
    override fun sign(
        commit: CommitBuilder?,
        gpgSigningKey: String?,
        committer: PersonIdent?,
        credentialsProvider: CredentialsProvider?
    ) {
        println(commit)
        println(gpgSigningKey)
        println(committer)
        println(credentialsProvider)
    }

    override fun canLocateSigningKey(
        gpgSigningKey: String?,
        committer: PersonIdent?,
        credentialsProvider: CredentialsProvider?
    ): Boolean {
        println(gpgSigningKey)
        println(committer)
        println(credentialsProvider)
        return true
    }
}
