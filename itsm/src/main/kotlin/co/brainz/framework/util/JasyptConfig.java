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
