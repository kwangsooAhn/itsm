/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AliceWebMvcConfig : WebMvcConfigurer {
    @Value("\${file.upload.dir}")
    private val resourcesLocation: String? = null

    @Value("\${file.image.uri}")
    private val resourcesUriPath: String? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("$resourcesUriPath/**")
            .addResourceLocations("file:///$resourcesLocation/")
    }
}
