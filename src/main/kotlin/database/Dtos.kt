package com.aymenhta.database

import com.aymenhta.encryption.EncryptionUtils
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDateTime

data class WebsiteInfo(
    val id: Long,
    val address: String,
    val alias: String,
)

fun toWebsiteInfo(result: ResultRow): WebsiteInfo {
    return WebsiteInfo(
        id = result[Websites.id],
        address = result[Websites.address],
        alias = result[Websites.alias]
    )
}

data class CredentialsInfo(
    val id: Long,
    val username: String,
    val password: String,
    val created: LocalDateTime
)

fun toCredentialsInfo(result: ResultRow): CredentialsInfo {
    val decryptedUsername = EncryptionUtils.decrypt(result[Credentials.username], EncryptionUtils.getPassword())
    val decryptedPwd = EncryptionUtils.decrypt(result[Credentials.password], EncryptionUtils.getPassword())

    return CredentialsInfo(
        id = result[Credentials.id],
        username = decryptedUsername,
        password = decryptedPwd,
        created = result[Credentials.created]
    )
}