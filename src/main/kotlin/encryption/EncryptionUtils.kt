package com.aymenhta.encryption

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtils {
    private const val PASSWORD_LOOKUP_KEY = "KMAN_KEY"
    private const val SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val AES_ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val KEY_SIZE = 256
    private const val ITERATION_COUNT = 65536
    private const val IV_SIZE = 16

    fun encrypt(data: String, password: String): String {
        val salt = generateSalt()
        val iv = generateIV()
        val secretKey = generateSecretKey(password, salt)

        val cipher = Cipher.getInstance(AES_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))

        val encryptedData = cipher.doFinal(data.toByteArray())
        val encryptedWithSaltAndIv = salt + iv + encryptedData
        return Base64.getEncoder().encodeToString(encryptedWithSaltAndIv)
    }

    fun decrypt(encryptedData: String, password: String): String {
        val decodedData = Base64.getDecoder().decode(encryptedData)

        val salt = decodedData.sliceArray(0 until 16)
        val iv = decodedData.sliceArray(16 until 32)
        val encrypted = decodedData.sliceArray(32 until decodedData.size)

        val secretKey = generateSecretKey(password, salt)

        val cipher = Cipher.getInstance(AES_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))

        val decryptedData = cipher.doFinal(encrypted)
        return String(decryptedData)
    }

    fun getPassword(): String =
        System.getenv(PASSWORD_LOOKUP_KEY) ?: throw IllegalStateException("KEY COULD NOT BE FOUND")

    private fun generateSecretKey(password: String, salt: ByteArray): SecretKeySpec {
        val keySpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE)
        val keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)
        val secretKey = keyFactory.generateSecret(keySpec).encoded
        return SecretKeySpec(secretKey, "AES")
    }

    private fun generateSalt(): ByteArray = ByteArray(16).apply { SecureRandom().nextBytes(this) }
    private fun generateIV(): ByteArray = ByteArray(IV_SIZE).apply { SecureRandom().nextBytes(this) }
}