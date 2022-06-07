package co.brainz.framework.encryption

import co.brainz.framework.constants.AliceConstants
import java.math.BigInteger
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.Key
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AliceEncryptionUtil {

    private lateinit var iv: String
    private lateinit var keySpec: Key

    @Value("\${encryption.option.salt}")
    private val salt: String = ""

    init {
        val key = AliceSecurityConstant.keyValue
        this.iv = key.substring(0, 16)
        val keyBytes = ByteArray(16)
        val b = key.toByteArray(charset("UTF-8"))
        var len: Int = b.size
        if (len > keyBytes.size)
            len = keyBytes.size
        System.arraycopy(b, 0, keyBytes, 0, len)
        val keySpec = SecretKeySpec(keyBytes, "AES")
        this.keySpec = keySpec
    }

    fun encryptEncoder(text: String, algorithm: String): String {
        var encryptText = text
        if (text.isNotBlank() && algorithm.isNotBlank()) {
            when (algorithm.toUpperCase()) {
                AliceConstants.EncryptionAlgorithm.BCRYPT.value -> {
                    encryptText = BCryptPasswordEncoder().encode(text)
                }
                AliceConstants.EncryptionAlgorithm.AES256.value -> {
                    encryptText = enCodeAES256(text)
                }
                AliceConstants.EncryptionAlgorithm.SHA256.value -> {
                    var salt = ""
                    if (this.salt.isNotBlank()) {
                        salt = this.salt
                    }
                    encryptText = enCodeSHA256(text, salt)
                }
            }
        }

        return encryptText
    }

    fun encryptDecoder(text: String, algorithm: String): String {
        var targetText = text
        if (text.isNotBlank() && algorithm.isNotBlank()) {
            when (algorithm.toUpperCase()) {
                AliceConstants.EncryptionAlgorithm.AES256.value -> {
                    targetText = deCodeAES256(text)
                }
            }
        }

        return targetText
    }

    // AES 256 암호화
    @Throws(
        java.io.UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun enCodeAES256(text: String): String {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv.toByteArray()))
        val encrypted = c.doFinal(text.toByteArray(charset("UTF-8")))

        return String(Base64.encodeBase64(encrypted))
    }

    // AES 256 복호화
    @Throws(
        java.io.UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun deCodeAES256(text: String): String {
        val c: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv.toByteArray(charset("UTF-8"))))
        val byteStr = Base64.decodeBase64(text.toByteArray())

        return String(c.doFinal(byteStr), charset("UTF-8"))
    }

    // SHA 256 암호화
    private fun enCodeSHA256(text: String, salt: String): String {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        digest.reset()
        digest.update((text.plus(salt)).toByteArray(charset("utf8")))

        return String.format("%064x", BigInteger(1, digest.digest()))
    }
}
