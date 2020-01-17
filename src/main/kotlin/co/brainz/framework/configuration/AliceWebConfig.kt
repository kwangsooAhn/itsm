package co.brainz.framework.configuration

import net.rakugakibox.util.YamlResourceBundle
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.*

@Configuration
class AliceWebConfig{

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

}
