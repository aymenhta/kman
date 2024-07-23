package encryption

import com.aymenhta.encryption.EncryptionUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EncryptionUtilsTest {

    @Test
    fun `Given text when encrypted and decrypted should return the original text`() {
        val originalText = """
            This is shawarma manifesto.
            
            With this we are going to demonstrate how one would
            make a traditional shawarma wrap (beef/chicken).
            
            We will also explain why we don't put ketchup on the shawarma. YA ABBAS!!
            
            Stay safe my friend; and don't forget the Bebzi!!
        """.trimIndent()

        val password = "OJZUwvdDqNw2Ll1Y0jqugfiaOHcTHH93mWTuHcpW9zY"

        val encryptedData = EncryptionUtils.encrypt(originalText, password)

        val decryptedData = EncryptionUtils.decrypt(encryptedData, password)

        assertEquals(originalText, decryptedData, "The decrypted text does not match the original")
    }
}