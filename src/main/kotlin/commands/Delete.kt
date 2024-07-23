package com.aymenhta.commands

import com.aymenhta.database.Credentials
import com.aymenhta.database.Websites
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.long
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class Delete : CliktCommand(name = "delete", help = "delete a record from the database") {
    override fun run() = Unit
}


class DeleteWebsite : CliktCommand(name = "w", help = "delete a website") {
    val inputAlias: String by option().prompt("alias").help("the website alias ('facebook' becomes 'fb')")

    override fun run() {
        val rowsAffected = Websites.deleteWhere { Websites.alias eq inputAlias }
        if (rowsAffected < 1) {
            echo("WEBSITE $inputAlias COULD NOT BE DELETED")
            return
        }
        echo("WEBSITE $inputAlias HAS BEEN DELETED")
    }
}

class DeletePassword : CliktCommand(name = "p", help = "delete a credentials") {
    val inputID: Long by option().long().prompt("id").help("the credential id")

    override fun run() {
        val rowsAffected = Credentials.deleteWhere { Credentials.id eq inputID }
        if (rowsAffected < 1) {
            echo("CREDENTIAL $inputID COULD NOT BE DELETED")
            return
        }
        echo("CREDENTIAL $inputID HAS BEEN DELETED")
    }
}