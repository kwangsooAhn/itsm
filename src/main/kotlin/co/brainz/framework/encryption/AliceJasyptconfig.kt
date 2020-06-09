package co.brainz.framework.encryption

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AliceJasyptconfig {
    @Bean("jasyptStringEncryptor")
    fun StringEncryptor(): StringEncryptor {

        val key = AliceSecurityConstant.keyValue
        val encryptor: PooledPBEStringEncryptor = PooledPBEStringEncryptor()
        val config: SimpleStringPBEConfig = SimpleStringPBEConfig()
        config.password = key

        // config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        // 사용할 알고리즘. openjdk11버전으로 하면 사용 할 수 있다.
        config.algorithm = "PBEWithMD5AndDES"
        config.setKeyObtentionIterations("1000")
        config.setPoolSize("1")
        config.providerName = "SunJCE" // 암호화 알고리즘 제공자
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator") // slat 함수
        config.stringOutputType = "base64" // encoding 방법
        encryptor.setConfig(config)

        return encryptor
    }
}
