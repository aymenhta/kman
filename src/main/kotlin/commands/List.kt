package com.aymenhta.commands

import com.aymenhta.database.Credentials
import com.aymenhta.database.Websites
import com.aymenhta.database.toCredentialsInfo
import com.aymenhta.database.toWebsiteInfo
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll


class List : CliktCommand(name = "list", help = "list records from the database") {
    override fun run() = Unit
}

class ListWebsites : CliktCommand(name = "w", help = "list all existing websites") {
    override fun run() = Websites
        .selectAll()
        .map(::toWebsiteInfo)
        .forEach { w -> echo(w) }
}

class ListCredentials : CliktCommand(name = "p", help = "list all existing credentials") {
    val alias: String by option().default("all").help("show all passwords for a single website")

    override fun run() {
        when (alias) {
            "all" -> Credentials
                        .selectAll()
                        .orderBy(Credentials.created, SortOrder.DESC)
                        .map(::toCredentialsInfo)
                        .forEach { e -> echo(e) }
            else -> {
                echo("listing '$alias' credentials")
                val resultRow = Websites
                                    .select(Websites.id)
                                    .where { Websites.alias eq alias }
                                    .firstOrNull()
                if (resultRow == null) {
                    echo("website '$alias' could not be found")
                    return
                }

                Credentials
                    .selectAll()
                    .where { Credentials.website eq resultRow[Websites.id] }
                    .orderBy(Credentials.created, SortOrder.DESC)
                    .map(::toCredentialsInfo)
                    .forEach { echo(it) }
            }
        }

    }
}
