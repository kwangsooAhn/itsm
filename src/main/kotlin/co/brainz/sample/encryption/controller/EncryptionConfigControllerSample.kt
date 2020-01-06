package co.brainz.sample.encryption.controller

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import co.brainz.framework.util.EncryptionUtil

@PropertySource("classpath:/public/template/sample/jasypt.sample.properties")
@RestController
public class EncryptionConfigControllerSample {

	@Value("\${test.siteid}")
	private lateinit var siteid: String
	
	@GetMapping("/sample/encryption/jasypt")
	public fun jasypt(): String {
		return siteid
	}

	// 코틀린은 예외체크 처리를 하지 않는다.
	// 예외 처리를 하고 싶다면 @Throws
	@Throws(
		KeyException::class,
		NoSuchAlgorithmException::class,
		NoSuchPaddingException::class,
		InvalidAlgorithmParameterException::class,
		IllegalBlockSizeException::class,
		BadPaddingException::class,
		EncoderException::class
	)
	@GetMapping("/sample/encryption/twoWay")
	public fun encryptionTwoWay(): String {
		var enCodeValue: String? = null
		var deCodeValue: String? = null
		var plainText = "김!@#$%^&*()_+Abc1"

		try {
			val encryption = EncryptionUtil()
			enCodeValue = encryption.twoWayEnCode(plainText)
			deCodeValue = encryption.twoWayDeCode(enCodeValue)
		} catch (e: UnsupportedEncodingException) {
			e.printStackTrace();
		}

		return "암호화 문 : $enCodeValue  복호화 문 : $deCodeValue"
	}

	@Throws(
		KeyException::class,
		NoSuchAlgorithmException::class,
		NoSuchPaddingException::class,
		InvalidAlgorithmParameterException::class,
		IllegalBlockSizeException::class,
		BadPaddingException::class,
		EncoderException::class
	)
	@GetMapping("/sample/encryption/oneWay")
	public fun encryptionOneWay(): String {
		var enCodeValue: String? = null
		var plainText = "김!@#$%^&*()_+Abc1"

		try {
			val encryption = EncryptionUtil()
			enCodeValue = encryption.oneWayEnCode(plainText)
		} catch (e: UnsupportedEncodingException) {
			e.printStackTrace()
		}

		return "평문 : $plainText  암호화 문 : $enCodeValue"
	}
}
