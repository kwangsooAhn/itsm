# 보안(암호화)
---

## 1. 개요

BWF(Brainz Web Framework)에서는 데이터는 단방향(SHA-512), 양방향(AES-256) 암호화를 통해서 암호화 하고
설정 파일 암호화는 Jasypt를 통해서 암호화 한다.


## 2. 관련 설정

- 설정 파일 암호화 설정
 - 설정 파일 설정에서는 jasypt 사용한다.
 - /itsm/build.gradle.kts
```
compile("com.github.ulisesbocchio:jasypt-spring-boot-starter:2.1.1")
```
 - /itsm/src/main/resources/application.properties
```
jasypt.encryptor.bean=jasyptStringEncryptor
```
 - /itsm/src/main/kotlin/co/brainz/framework/util/JasyptConfig.java
```
package co.brainz.framework.util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
@Configuration
public class JasyptConfig {	
	@Bean("jasyptStringEncryptor")
	public StringEncryptor stringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("mh+fmWW2XCLwJvoL");  //AliceE15001 암호화해서 16자리까지 출력해서 가져옴.
		// config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256"); //사용할 알고리즘. openjdk11버전으로
		// 하면 사용 할 수 있다.
		config.setAlgorithm("PBEWithMD5AndDES"); // 사용할 알고리즘
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}
}
```
 - 샘플 페이지 <a href="../../../sample/encryption/jasypt" target="_blank">jasypt 샘플 페이지</a>
  - /itsm/src/main/kotlin/co/brainz/framework/sample/encryption/controller/EncryptionConfigController.java
```
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
```
  - /itsm/src/main/kotlin/co/brainz/framework/sample/encryption/controller/Jasyptsample.properties
```
test.siteid=ENC(GBXlZ0zUUQZnTu5F5Vd9o8vr9jpLS/diUl8dUNZCb1U=)
``` 
 - 참고 페이지
   - [Jasypt 설정 페이지] (http://www.jasypt.org)
   - [Spring Jasypt 설정 ] (https://www.jeejava.com/spring-enableencryptableproperties-with-jasypt/)
   
- Data 암호화 설정
  - 단방향 암호화 : 단방향 암호화는 사용자 패스워드처럼 다시 복호화가 필요 없을 경우 사용한다.- [SHA-512]
  - 양방향 암호화 : 양방향 암호화는 사용자의 이메일처럼 다시 복호화가 필요할 경우 사용한다. - [AES-256]
  - /itsm/build.gradle.kts
```
compile("commons-codec:commons-codec:1.13")
```
  - /itsm/src/main/kotlin/co/brainz/framework/util/EncryptionUtil.java
```
package co.brainz.framework.util;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Configuration;
@Configuration
public class EncryptionUtil {
	private String iv;
	private Key keySpec;
	private String password = "mh+fmWW2XCLwJvoL";
	public EncryptionUtil() throws UnsupportedEncodingException {
		this.iv = password.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = password.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length)
			len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		this.keySpec = keySpec;
	}
	// AES 256 암호화
	public String aesEncode(String str)
			throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}
	//AES 256 복호화
	public String aesDecode(String str)
			throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes("UTF-8")));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());
		return new String(c.doFinal(byteStr), "UTF-8");
	}
	//SHA 512 암화
	public String shaEncode(String str) {
		String toReturn = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.reset();
			digest.update(str.getBytes("utf8"));
			toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toReturn;
	}
}
```
  - aes256 암호화 샘플 페이지 <a href="../../../sample/encryption/aes256" target="_blank">aes256 암호화 샘플 페이지</a>
```
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
```
  - sha512 암호화 샘플 페이지 <a href="../../../sample/encryption/sha512" target="_blank">sha512 암호화 샘플 페이지</a>
```
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
```

## 3. 이슈사항

- 암호화 시 암호화 Key 값을 어떻게 설정 할 것인가?
 - 소스상에 암호화 Key 암호화 키를 넣는다. (현재 적용 함.)
    - 장점 : 키 값 관리가 편하다.
    - 단점 : 키 값 노출이 쉽다.
 - 암호화 key 값을 Vm argumentsd에 넣어서 사용하는 방법
    - 장점 : 소스에 암호화 키 값이 남지 않는다.
    - 단점 : 암호화키 값을 잃어 버릴때에 대한 Risk가 크다.
   - 적용 방법 :
    - EncryptionUtil.java
```
String password = System.getProperty("jasypt.encryptor.password");
```
    - JasyptConfig.java
```
@Value("${jasypt.encryptor.password}") 
private String password;
```
    - VM arguments 
```
'-Djasypt.encryptor.password=암호화키
```