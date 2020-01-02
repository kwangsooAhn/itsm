package co.brainz.framework.configuration

import co.brainz.framework.interceptor.AliceInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor


/**
 * 인터셉터 설정 클래스
 */
@Configuration
class AliceInterceptorConfig(private val interceptor: AliceInterceptor,
                             private val localeChangeInterceptor: LocaleChangeInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**")
                .excludePathPatterns("/assets/**", "'/favicon.ico")
                .excludePathPatterns("/", "/logout", "/certification/**", "/oauth/**", "/portal/**")
                .excludePathPatterns("/layout/**", "/index**") // 임시
                .excludePathPatterns("/process/**", "/document/**", "/exception/**", "/files/**") // 임시
        registry.addInterceptor(localeChangeInterceptor)
    }
}