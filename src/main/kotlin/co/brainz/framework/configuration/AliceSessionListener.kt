/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.configuration

import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AliceSessionListener : HttpSessionListener {

    @Value("\${server.servlet.session.timeout}")
    private val sessionTimeout: Int = 600

    override fun sessionCreated(se: HttpSessionEvent) {
        se.session.maxInactiveInterval = sessionTimeout
    }

    override fun sessionDestroyed(se: HttpSessionEvent?) {}
}
