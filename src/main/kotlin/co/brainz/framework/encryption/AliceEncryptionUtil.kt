package co.brainz.framework.util

import co.brainz.framework.encryption.AliceSecurityConstant
import org.apache.commons.codec.binary.Base64
import org.springframework.context.annotation.Configuration
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

@Configuration
public open class AliceEncryptionUtil {

	private lateinit var iv: String
	private lateinit var keySpec: Key
	private val twoWayAlgorithm: String = "aes256"
	private val oneWayAlgorithm: String = "sha512"
	
	init {
		var key = AliceSecurityConstant.keyValue
		this.iv = key.substring(0, 16)
		var keyBytes = ByteArray(16)
		var b = key.toByteArray(charset("UTF-8"))
		var len: Int = b.size
		if (len > keyBytes.size)
			len = keyBytes.size
		System.arraycopy(b, 0, keyBytes, 0, len)
		val keySpec: SecretKeySpec = SecretKeySpec(keyBytes, "AES")
		this.keySpec = keySpec
	}

	//양방향  암호화 
	public fun twoWayEnCode(str: String): String {
		var enStr: String = ""
		if (str != "") {
			if (twoWayAlgorithm == "aes256") {
				enStr = enCodeAES256(str)
			}
		}
		return enStr
	}
	
	//양방향  복호화
	public fun twoWayDeCode(str: String): String {
		var deStr: String = ""
		if (str != "") {
			if (twoWayAlgorithm == "aes256") {
				deStr = deCodeAES256(str)
			}
		}
		return deStr
	}
	
	//단항향 암호화
	public fun oneWayEnCode(str: String): String {
		var enStr: String = ""
		if (str != "") {
			if (oneWayAlgorithm == "sha512") {
				enStr = enCodeSHA512(str)
			}
		}
		return enStr
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
	private fun enCodeAES256(str: String): String {
		
		val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
		c.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv.toByteArray()))
		val encrypted = c.doFinal(str.toByteArray(charset("UTF-8")))
		val enStr = String(Base64.encodeBase64(encrypted))
		
		return enStr
	}

	//AES 256 복호화
	@Throws(
		java.io.UnsupportedEncodingException::class,
		NoSuchAlgorithmException::class,
		NoSuchPaddingException::class,
		InvalidKeyException::class,
		InvalidAlgorithmParameterException::class,
		IllegalBlockSizeException::class,
		BadPaddingException::class
	)
	private fun deCodeAES256(str: String): String {
		
		val c: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
		c.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv.toByteArray(charset("UTF-8"))))
		val byteStr = Base64.decodeBase64(str.toByteArray())
		
		return String(c.doFinal(byteStr), charset("UTF-8"))
	}

	//SHA 512 암호화
	private fun enCodeSHA512(str: String): String {
		
		lateinit var toReturn: String
		try {
			var digest: MessageDigest = MessageDigest.getInstance("SHA-512")
			digest.reset()
			digest.update(str.toByteArray(charset("utf8")))
			toReturn = String.format("%0128x", BigInteger(1, digest.digest()))
		} catch (e: Exception) {
			e.printStackTrace()
		}
		
		return toReturn
	}
}

