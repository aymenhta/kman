package com.aymenhta

import com.aymenhta.commands.*
import com.aymenhta.commands.List
import com.aymenhta.database.Credentials
import com.aymenhta.database.Websites
import com.aymenhta.database.setupDb
import com.github.ajalt.clikt.core.subcommands
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


fun main(args: Array<String>) {
    // set up the database
    val db = setupDb()
    // create schemas
    transaction(db) {
        SchemaUtils.createMissingTablesAndColumns(Websites, Credentials)
        // Initialize the cli application
        KmanCLI()
            .subcommands(
                Generate().subcommands(
                    GeneratePassword(),
                    GenerateKey(),
                ),
                Add().subcommands(
                    AddWebsite(),
                    AddPassword()
                ),
                Delete().subcommands(
                    DeleteWebsite(),
                    DeletePassword()
                ),
                List().subcommands(
                    ListWebsites(),
                    ListCredentials()
                ),
                Export()
            )
            .main(args)
    }
}