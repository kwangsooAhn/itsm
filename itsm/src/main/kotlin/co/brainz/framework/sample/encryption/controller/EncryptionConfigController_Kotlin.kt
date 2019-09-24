/*
package co.brainz.framework.sample.encryption.controller

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.EncoderException;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.brainz.framework.util.EncryptionUtil;

@PropertySource("classpath:/co/brainz/framework/sample/encryption/controller/Jasyptsample.properties")
@RestController

public class EncryptionConfigController_Kotlin {
	
	@Value("\${test.siteid}")
    private lateinit var siteid : String
	
	@GetMapping("sample/encryption/jasypt")
    public fun jasypt() : String{
		
		val pbeEnc = StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES"); // 사용할 알고리즘
		pbeEnc.setPassword("mh+fmWW2XCLwJvoL"); // AliceE15001 암호화해서 16자리까지 출력해서 가져옴.
		pbeEnc.setProviderName("SunJCE");
		pbeEnc.setStringOutputType("base64");
		
		var enc : String = pbeEnc.encrypt("itsm");
		println("enc = ${enc}")
		var des : String = pbeEnc.decrypt(enc);
		println("des = ${des}")
		
		var value : String = "암호화 문 : ENC(GBXlZ0zUUQZnTu5F5Vd9o8vr9jpLS/diUl8dUNZCb1U=)    복호화 문: ${siteid} based on Kotlin;"
		return value
	}
	
	// 코틀린은 예외체크 처리를 하지 않는다.
	// 예외 처리를 하고 싶다면 @Throws
	@Throws(KeyException::class,NoSuchAlgorithmException::class,NoSuchPaddingException::class,InvalidAlgorithmParameterException::class,
	       IllegalBlockSizeException::class, BadPaddingException::class,EncoderException::class)
	@GetMapping("/sample/encryption/aes256")
    public fun aes256( ) : String {
		var enCodeValue : String? = null
		var deCodeValue : String? = null
		try{
			val aes256 = EncryptionUtil();
			enCodeValue = aes256.aesEncode("김!@#$%^&*()_+Abc1");
			deCodeValue = aes256.aesDecode(enCodeValue);
		} catch(e : UnsupportedEncodingException){
			e.printStackTrace();
		}
		return "암호화 문 : $enCodeValue  복호화 문 : $deCodeValue based on Kotlin"
	}
	
	@Throws(KeyException::class,NoSuchAlgorithmException::class,NoSuchPaddingException::class,InvalidAlgorithmParameterException::class,
	       IllegalBlockSizeException::class, BadPaddingException::class,EncoderException::class)
    @GetMapping("/sample/encryption/sha512")
    public fun sha256() : String {
		var enCodeValue : String? = null
		
		try {
			val sha512 = EncryptionUtil()
			enCodeValue = sha512.shaEncode("김!@#$%^&*()_+Abc1")
		}catch(e: UnsupportedEncodingException){
			e.printStackTrace()
		}
			return "평문 : 김!@#$%^&*()_+Abc1  암호화 문 : $enCodeValue based on Kotlin";
	}
}
*/