package com.aymenhta.commands

import com.aymenhta.database.Credentials
import com.aymenhta.database.Websites
import com.aymenhta.encryption.EncryptionUtils
import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.selectAll
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.List

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDateTime::class)
object DateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}


class Export : CliktCommand(help = "export database entries to json") {
    @Serializable
    data class CredentialsJson(
        val id: Long,
        val username: String,
        val password: String,
        @Serializable(with = DateTimeSerializer::class)
        val created: LocalDateTime
    )

    @Serializable
    data class WebsitesJson(
        val id: Long,
        val address: String,
        val alias: String,
        var credentials: List<CredentialsJson> = listOf()
    )
    @Serializable
    data class JsonDocument(
        val websites: List<WebsitesJson>
    )

    override fun run() {
        val document = fetchDatabase()
        val jsonified = Json.encodeToString<JsonDocument>(document)
        echo(jsonified)
    }

    private fun fetchDatabase(): JsonDocument {
        // fetch all the websites, alongside their
        val jsonifiedWebsites = Websites
            .selectAll()
            .map {
                WebsitesJson(
                    id = it[Websites.id],
                    address = it[Websites.address],
                    alias = it[Websites.alias]
                )
            }

        // get the password
        val password = EncryptionUtils.getPassword()

        jsonifiedWebsites.forEach { w ->
            // get all the correspondent credentials
            w.credentials = Credentials
                .select(
                    Credentials.id,
                    Credentials.username,
                    Credentials.password,
                    Credentials.created
                    )
                .where { Credentials.website eq w.id }
                .map {
                    CredentialsJson(
                        id = it[Credentials.id],
                        username = EncryptionUtils.decrypt(it[Credentials.username], password),
                        password = EncryptionUtils.decrypt(it[Credentials.password], password),
                        created = it[Credentials.created]
                    )
                }
        }

        return JsonDocument(websites = jsonifiedWebsites)
    }
}