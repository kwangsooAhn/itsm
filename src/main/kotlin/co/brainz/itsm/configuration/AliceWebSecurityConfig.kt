/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.configuration

import co.brainz.framework.auth.handler.AliceAuthFailureHandler
import co.brainz.framework.auth.handler.AliceAuthSuccessHandler
import co.brainz.framework.auth.handler.AliceLogoutSuccessHandler
import co.brainz.framework.auth.service.AliceAuthProvider
import co.brainz.framework.configuration.AliceWebSecurityConfigurerAdapter
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Configuration
@EnableWebSecurity
class AliceWebSecurityConfig(
    authProvider: AliceAuthProvider,
    authSuccessHandler: AliceAuthSuccessHandler,
    authFailureHandler: AliceAuthFailureHandler,
    logoutSuccessHandler: AliceLogoutSuccessHandler
) : AliceWebSecurityConfigurerAdapter(authProvider, authSuccessHandler, authFailureHandler, logoutSuccessHandler) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun ignoreConfigure(web: WebSecurity) {
        web.ignoring().antMatchers("/assets/**", "/favicon.ico", "/fileImages/**")
        logger.debug("> web configure <")
    }

    override fun authorizeRequestConfigure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(
                "/",
                "/login",
                "/logout",
                "/sessionInValid",
                "/certification/**",
                "/oauth/**",
                "/portal/**",
                "/rest/**",
                "/i18n/**"
            ).permitAll()
            .anyRequest().authenticated()
            .and().csrf().ignoringAntMatchers("/rest/**")
        logger.debug("> http configure <")
    }
}
