package com.aymenhta.commands

import com.github.ajalt.clikt.core.CliktCommand
import java.security.SecureRandom
import java.util.Base64

class Generate : CliktCommand(name = "generate", help = "generate a random password/encryption key") {
    override fun run() = Unit
}

class GeneratePassword : CliktCommand(name = "p", help = "generate a random password") {
    override fun run() {
        val randomPwd = generateRandomPassword()
        echo(randomPwd)
    }
}

class GenerateKey : CliktCommand(name = "k", help = "generate a random encryption key") {
    override fun run() {
        val randomPwd = generateRandomPassword()
        echo(randomPwd)
    }
}


fun generateRandomPassword(): String {
    val bytes = generateRandomBytes()
    val encodedPassword = encodeToBase32(bytes)
    return encodedPassword
}

fun generateRandomBytes(): ByteArray {
    val randomBytes = ByteArray(32)
    val secureRandom = SecureRandom.getInstance("SHA1PRNG")
    secureRandom.setSeed(System.currentTimeMillis())
    secureRandom.nextBytes(randomBytes)
    return randomBytes
}

fun encodeToBase32(bytes: ByteArray): String {
    val base32Encoder = Base64.getEncoder().withoutPadding()
    return base32Encoder.encodeToString(bytes)
}