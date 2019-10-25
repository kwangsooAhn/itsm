package co.brainz.framework.sample.i18n

import java.util.Locale;

import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.context.support.ResourceBundleMessageSource;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.io.IOException

@Configuration
@ComponentScan("co.brainz.framework.sample.i18n")
public open class I18nConfig : WebMvcConfigurer {
    
    // 1. LocaleResolver
    @Bean("localeResolver")
    public open fun localeResolver() : LocaleResolver {
        var slr: SessionLocaleResolver = SessionLocaleResolver()
        slr.setDefaultLocale(Locale.KOREA)
        return slr;
    }

    // 2. LocaleChangInterceptor
    @Bean("localeChangeInterceptor") 
    public open fun localeChangeInterceptor() : LocaleChangeInterceptor {
        var lci: LocaleChangeInterceptor = LocaleChangeInterceptor()
        lci.setParamName("lang")
        lci.setIgnoreInvalidLocale(true)
        return lci;
    }
    
    override fun addInterceptors(InterceptorRegistry : InterceptorRegistry) {
        InterceptorRegistry.addInterceptor(localeChangeInterceptor())
    }
    
    /**
      [Use *.yml as MessageSource]
      기존의 *.properties를 사용하려면 아래 messageSource와 YamlMessageSource를 주석처리 후,
      application.properties의  "spring.messages.basename" 주석을 해제한다.
    */
    @Bean("messageSource")
    public open fun messageSource() : MessageSource {
        var ms: YamlMessageSource = YamlMessageSource()
        ms.setBasename("public/template/message/yml/messages")
        ms.setDefaultEncoding("UTF-8")
        ms.setAlwaysUseMessageFormat(true)
        ms.setUseCodeAsDefaultMessage(true)
        ms.setFallbackToSystemLocale(true)
        return ms;
    }
    
}

class YamlMessageSource : ResourceBundleMessageSource() {
    @Throws(MissingResourceException::class)
    protected override fun doGetBundle(basename: String, locale: Locale) : ResourceBundle {
        return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE)
    }
}

