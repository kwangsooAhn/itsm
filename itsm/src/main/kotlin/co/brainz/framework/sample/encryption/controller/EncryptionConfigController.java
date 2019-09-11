package co.brainz.framework.sample.encryption.controller;

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

@PropertySource({ "classpath:/co/brainz/framework/sample/encryption/controller/Jasyptsample.properties" })
@RestController
public class EncryptionConfigController {
	@Value("${test.siteid}")
	private String siteid;

	@GetMapping("/sample/encryption/jasypt")
	public String jasypt() {

		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES"); // 사용할 알고리즘
		pbeEnc.setPassword("mh+fmWW2XCLwJvoL"); // AliceE15001 암호화해서 16자리까지 출력해서 가져옴.
		pbeEnc.setProviderName("SunJCE");
		pbeEnc.setStringOutputType("base64");

		// 사용 샘플
		String enc = pbeEnc.encrypt("itsm"); // 암호화 할 내용
		System.out.println("enc = " + enc); // 암호화 한 내용을 출력
		String des = pbeEnc.decrypt(enc);
		System.out.println("des = " + des);

		String value = "암호화 문 : ENC(GBXlZ0zUUQZnTu5F5Vd9o8vr9jpLS/diUl8dUNZCb1U=)    복호화 문: " + siteid;
		return value;
	}
	
	@GetMapping("/sample/encryption/aes256")
	public String aes256() throws KeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, EncoderException {
		String enCodeValue = "";
		String deCodeValue = "";
		try {
			EncryptionUtil aes256 = new EncryptionUtil();

			enCodeValue = aes256.aesEncode("김!@#$%^&*()_+Abc1");
			deCodeValue = aes256.aesDecode(enCodeValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "암호화 문 : " + enCodeValue + " 복호화 문 :" + deCodeValue;
	}
	
	
	@GetMapping("/sample/encryption/sha512")
	public String sha256() throws KeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, EncoderException {
		String enCodeValue = "";
		
		try {
			EncryptionUtil sha512 = new EncryptionUtil();

			enCodeValue = sha512.shaEncode("김!@#$%^&*()_+Abc1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "평문 : 김!@#$%^&*()_+Abc1  암호화 문 :" + enCodeValue;
	}
}
