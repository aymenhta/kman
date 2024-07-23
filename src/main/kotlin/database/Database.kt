package com.aymenhta.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.TransactionManager

fun setupDb(): Database {

    // use hikari for connections pooling
    val config = HikariConfig().apply {
        jdbcUrl = System.getenv("KMAN_DATABASE_URI")
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = System.getenv("KMAN_DATABASE_USER")
        password = System.getenv("KMAN_DATABASE_PASSWORD")
        maximumPoolSize = 6
        // as of version 0.46.0, if these options are set here, they do not need to be duplicated in DatabaseConfig
        isReadOnly = false
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }

    // connect to the database
    val dataSource = HikariDataSource(config)
    val db = Database.connect(dataSource)
    TransactionManager.defaultDatabase = db

    return db
}

object Websites: Table("websites") {
    val id = long("id").autoIncrement()
    val address = varchar("address", length = 64).uniqueIndex()
    val alias = varchar("alias", length = 16).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}


object Credentials: Table("credentials") {
    val id = long("id").autoIncrement()
    val username = text("username")
//    val password : Column<ByteArray> = binary("password", 1024)
    val password  = text("password")
    val created = datetime("created").defaultExpression(CurrentDateTime)
    val website = reference("websites", Websites.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(id)
}

