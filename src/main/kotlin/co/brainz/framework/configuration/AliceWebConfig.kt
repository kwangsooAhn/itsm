package co.brainz.framework.configuration

import net.rakugakibox.util.YamlResourceBundle
import org.apache.http.client.HttpClient
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.core.io.Resource
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle
import javax.net.ssl.SSLContext

@Configuration
class AliceWebConfig{

    @Value("\${server.ssl.key-store-password}")
    private val keyStorePassword = ""

    @Value("\${server.ssl.trust-store-password}")
    private var trustStorePassword = ""

    @Value("classpath:itsm.jks")
    private lateinit var jksFile: Resource

    @Value("classpath:itsm.ts")
    private lateinit var tsFile: Resource

    // 1. LocaleResolver
    @Bean("localeResolver")
    fun localeResolver() : LocaleResolver {
        val slr = SessionLocaleResolver()
        slr.setDefaultLocale(Locale.KOREA)
        return slr
    }

    // 2. LocaleChangInterceptor
    @Bean("localeChangeInterceptor")
    fun localeChangeInterceptor() : LocaleChangeInterceptor {
        val lci = LocaleChangeInterceptor()
        lci.paramName = "lang"
        lci.isIgnoreInvalidLocale = true
        return lci
    }

    @Bean("messageSource")
    fun messageSource() : MessageSource {
        class YamlMessageSource : ResourceBundleMessageSource() {
            @Throws(MissingResourceException::class)
            override fun doGetBundle(basename: String, locale: Locale) : ResourceBundle {
                return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE)
            }
        }

        val ms = YamlMessageSource()
        ms.setBasename("public/message/messages")
        ms.setDefaultEncoding("UTF-8")
        ms.setAlwaysUseMessageFormat(true)
        ms.setUseCodeAsDefaultMessage(true)
        ms.setFallbackToSystemLocale(true)
        return ms
    }

    @Bean
    @Throws(Exception::class)
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate? {
        val sslContext: SSLContext = SSLContextBuilder
                .create()
                .loadKeyMaterial(jksFile.file, keyStorePassword.toCharArray(), keyStorePassword.toCharArray())
                .loadTrustMaterial(tsFile.file, trustStorePassword.toCharArray())
                .build()
        val client: HttpClient = HttpClients.custom().setSSLContext(sslContext).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build()
        return builder.requestFactory { HttpComponentsClientHttpRequestFactory(client) }.build()
    }

}
