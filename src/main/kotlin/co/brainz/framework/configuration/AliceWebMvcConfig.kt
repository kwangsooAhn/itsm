/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.configuration

import co.brainz.framework.interceptor.AliceInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

/**
 * spring mvc 에 대한 구성을 커스텀하는 클래스
 */
@Configuration
class AliceWebMvcConfig(
    private val interceptor: AliceInterceptor,
    private val localeChangeInterceptor: LocaleChangeInterceptor
) : WebMvcConfigurer {
    @Value("\${file.upload.dir}")
    private val resourcesLocation: String? = null

    @Value("\${file.image.uri}")
    private val resourcesUriPath: String? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("$resourcesUriPath/**")
            .addResourceLocations("file:///$resourcesLocation/")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**")
            .excludePathPatterns("/portals/browserGuide")
            .excludePathPatterns("/assets/**")
        registry.addInterceptor(localeChangeInterceptor)
    }
}
