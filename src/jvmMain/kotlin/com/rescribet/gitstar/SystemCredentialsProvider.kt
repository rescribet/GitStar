package com.rescribet.gitstar

import Configuration
import org.eclipse.jgit.transport.CredentialItem
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.URIish

class SystemCredentialsProvider(val config: Configuration) : CredentialsProvider() {
    override fun isInteractive(): Boolean {
        return false
    }

    override fun supports(vararg items: CredentialItem?): Boolean {
        return true
    }

    override fun get(uri: URIish?, vararg items: CredentialItem?): Boolean {
        return true
    }
}
