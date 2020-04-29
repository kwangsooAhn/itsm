package co.brainz.framework.encryption

import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher

/**
 * RSA key 생성 및 암호화, 복호화하는 클래스
 */
@Component
class AliceCryptoRsa {

    private val keySize = 2048
    private val algorithmName = "RSA"
    private val keyFactory: KeyFactory = KeyFactory.getInstance(algorithmName)
    private val keyPairGenerator = KeyPairGenerator.getInstance(algorithmName)

    private lateinit var rsaPublicKeySpec: RSAPublicKeySpec
    private lateinit var privateKey: PrivateKey
    private lateinit var publicKey: PublicKey

    init {
        keyPairGenerator.initialize(keySize)
        resetKeyPair()
    }

    /**
     * key 생성
     */
    fun resetKeyPair() {
        val keyPair = keyPairGenerator.genKeyPair()
        this.publicKey = keyPair.public
        this.privateKey = keyPair.private
        this.rsaPublicKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec::class.java) as RSAPublicKeySpec
    }

    fun getPublicKeyModulus(): String {
        return rsaPublicKeySpec.modulus.toString(16)
    }

    fun getPublicKeyExponent(): String {
        return rsaPublicKeySpec.publicExponent.toString(16)
    }

    fun getPublicKey(): PublicKey {
        return this.publicKey
    }

    fun getPrivateKey(): PrivateKey {
        return this.privateKey
    }

    /**
     * 문자열 암호화
     */
    fun encrypt(publicKey: PublicKey, inputString: String): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val ba = cipher.doFinal(inputString.toByteArray(Charsets.UTF_8))

        val sb = StringBuilder()
        for (b in ba) {
            sb.append(String.format("%02X", b))
        }

        return sb.toString()
    }

    /**
     * 문자열 복호화
     */
    fun decrypt(privateKey: PrivateKey, encryptString: String): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val encryptedBytes =
            ByteArray(encryptString.length / 2) { encryptString.substring(it * 2, it * 2 + 2).toInt(16).toByte() }
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}
