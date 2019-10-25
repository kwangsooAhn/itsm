# 보안(암호화)
---

## 1. 개요

- BWF(Brainz Web Framework)에서는 데이터는 단방향(SHA-512), 양방향(AES-256) 알고리즘을 통해서 암호화 한다.<br/>
다만, 설정 파일 암호화는 Jasypt 라이브러리를 통해서 암호화 하고 PBEWithMD5AndDES 알고리즘을 사용한다.<br/>

## 2. jasypt 사용법

- 설정 파일 : /itsm/src/main/kotlin/co/brainz/framework/security/Jasyptconfig.kt
 ```java
 package co.brainz.framework.security

 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.jasypt.encryption.StringEncryptor;
 import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
 import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
 import co.brainz.framework.security.SecurityConstant;

 @Configuration
 public open class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    public open fun StringEncryptor(): StringEncryptor {

        var key = SecurityConstant.keyValue
        var encryptor: PooledPBEStringEncryptor = PooledPBEStringEncryptor()
        var config: SimpleStringPBEConfig = SimpleStringPBEConfig()
        config.setPassword(key)
        // config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256")                    //사용할 알고리즘. openjdk11버전으로 하면 사용 할 수 있다.
        config.setAlgorithm("PBEWithMD5AndDES")
        config.setKeyObtentionIterations("1000")
        config.setPoolSize("1")
        config.setProviderName("SunJCE")                                        //암호화 알고리즘 제공자
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator") //slat 함수
        config.setStringOutputType("base64")                                    //encoding 방법
        encryptor.setConfig(config)

        return encryptor
    }
 }
 ```
 
- 사용 방법 : properties 파일에서 암호화 하고 싶은 문장 암호화 한 후  ENC(암호화)으로 감싸고 해당 암호화문을 풀고 싶을때는 ${....}으로 사용한다. <br/>
  test.siteid=ENC(GBXlZ0zUUQZnTu5F5Vd9o8vr9jpLS/diUl8dUNZCb1U=)<br/>
  ${test.siteid}
- 샘플 소스 : /itsm/src/main/kotlin/co/brainz/framework/sample/encryption/controller/EncryptionConfigController.kt
```java
@PropertySource("classpath:/co/brainz/framework/sample/encryption/controller/Jasyptsample.properties")
@RestController
public class EncryptionConfigController {

    @Value("\${test.siteid}")
    private lateinit var siteid: String

    @GetMapping("sample/encryption/jasypt")
    public fun jasypt(): String {
        return siteid
    }
}
```
- 암호화 방법 <br/>
아래의 주소를 참고하여 인터넷 또는 파일서버로 해당 파일을 다운 받는다. 이후 압축을 풀고 jasypt-1.9.3/bin/ 폴더로 이동한다. <br/>
https://github.com/jasypt/jasypt/releases/download/jasypt-1.9.3/jasypt-1.9.3-dist.zip <br/>
\\fs\부서폴더\연구개발본부\개발2그룹\ITSM팀\Zenius ITSM\02.진행프로젝트\20150122_ITSM_V3.0.0\95.암호화

 - 암호화 사용방법 : encrypt.bat [ARGUMENTS] 또는 encrypt.sh [ARGUMENTS]
   - 샘플 : <br/>
```
     encrypt.sh input="itsm" password=mh+fmWW2XCLwJvoL algorithm=PBEWithMD5AndDES keyObtentionIterations=1000 providerName=SunJCE  saltGeneratorClassName=org.jasypt.salt.RandomSaltGenerator stringOutputType=base64
```
   - 필수 파라미터: <br/>
      input <br/>
      password<br/>
   - 옵션 파라미터 관련해서는 Jasypt 홈페이지(http://www.jasypt.org) 를 참조한다.

 - 복호화 사용방법 : decrypt.bat [ARGUMENTS] 또는 decrypt.sh [ARGUMENTS]
   - 샘플 : <br/>
```
     decrypt.sh input="tENKegJdIi8+77YpahKKLg==" password=mh+fmWW2XCLwJvoL algorithm=PBEWithMD5AndDES keyObtentionIterations=1000 providerName=SunJCE  saltGeneratorClassName=org.jasypt.salt.RandomSaltGenerator stringOutputType=base64
```
   - 필수 파라미터: <br/>
      input <br/>
      password<br/>
   - 옵션 파라미터 관련해서는 Jasypt 홈페이지(http://www.jasypt.org) 를 참조한다.

 - 사용가능한 알고리즘을 확인하려고 한다면  listAlgorithms.bat, listAlgorithms.sh 명령어를 통해서 확인 한다.

- 참고 페이지 <br/>
[Spring Jasypt 설정 ] (https://www.jeejava.com/spring-enableencryptableproperties-with-jasypt/)
   

## 3. Data 암호화 방법

- 양방향 암호화 : 양방향 암호화는 사용자의 이메일처럼 다시 복호화가 필요할 경우 사용한다. - 현재는 [AES-256] 알고리즘을 사용한다. <br/>
   단방향 암호화 : 단방향 암호화는 사용자 패스워드처럼 다시 복호화가 필요 없을 경우 사용한다.- 현재는 [SHA-512] 알고리즘을 사용한다. <br/>
   설정 파일 : /itsm/src/main/kotlin/co/brainz/framework/util/EncryptionUtil.kt

 ```java
 package co.brainz.framework.util

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
 import co.brainz.framework.security.SecurityConstant;

 @Configuration
 public open class EncryptionUtil {

    private lateinit var iv: String
    private lateinit var keySpec: Key
    private val twoWayAlgorithm: String = "aes256"
    private val oneWayAlgorithm: String = "sha512"
    
    init {
        var key = SecurityConstant.keyValue
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

 ```
- 양방향 암호화 사용 방법 : 암호화 : twoWayEnCode(...), 복호화 : twoWayDeCode(...) <br/>
     샘플 파일 : /itsm/src/main/kotlin/co/brainz/framework/sample/encryption/controller/EncryptionConfigController.kt <br/>
```java
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
```
- 단뱡항 암호화 사용 방법 : oneWayEnCode(...) <br/>
     샘플파일 : /itsm/src/main/kotlin/co/brainz/framework/sample/encryption/controller/EncryptionConfigController.kt
```java
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
```

## 4. 이슈사항

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