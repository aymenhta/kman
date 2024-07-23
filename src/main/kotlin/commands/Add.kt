package com.aymenhta.commands

import com.aymenhta.database.Credentials
import com.aymenhta.database.Websites
import com.aymenhta.encryption.EncryptionUtils
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import org.jetbrains.exposed.sql.insert

class Add : CliktCommand(name = "add", help = "add a new record to the database") {
    override fun run() = Unit
}


class AddWebsite : CliktCommand(name = "w", help = "add a new website") {
    val nawAddress: String by option().prompt("address").help("the website url (https://www.example.com)")
    val newAlias: String by option().prompt("alias").help("the website alias ('facebook' becomes 'fb')")

    override fun run() {
        Websites.insert {
            it[address] = nawAddress
            it[alias] = newAlias
        }

        echo("saved")
    }
}

class AddPassword : CliktCommand(name = "p", help = "add a new credentials") {
    val newUsername: String by option().prompt("username").help("the account username")
    val newPassword: String by option().prompt("password").help("the account password")
    val websiteAlias: String by option().prompt("website").help("website alias")

    override fun run() {
        val resultRow = Websites.select(Websites.id).where { Websites.alias eq websiteAlias }.firstOrNull()
        when (resultRow) {
            null -> {
                echo("website with the alias $websiteAlias does not exist")
                return
            }
            else -> {
                val encryptedUsername = EncryptionUtils.encrypt(newUsername, EncryptionUtils.getPassword())
                val encryptedPassword = EncryptionUtils.encrypt(newPassword, EncryptionUtils.getPassword())
                Credentials.insert {
                    it[username] = encryptedUsername
                    it[password] = encryptedPassword
                    it[website] = resultRow[Websites.id]
                }
                echo("credentials were added successfully")
            }
        }
    }
}