package co.brainz.itsm.configuration

import co.brainz.framework.auth.handler.AliceAuthFailureHandler
import co.brainz.framework.auth.handler.AliceAuthSuccessHandler
import co.brainz.framework.auth.service.AliceAuthProvider
import co.brainz.framework.configuration.AliceWebSecurityConfigurerAdapter
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Configuration
@EnableWebSecurity
class AliceWebSecurityConfig(authProvider: AliceAuthProvider,
                             authSuccessHandler: AliceAuthSuccessHandler,
                             authFailureHandler: AliceAuthFailureHandler)
    : AliceWebSecurityConfigurerAdapter(authProvider, authSuccessHandler, authFailureHandler) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun ignoreConfigure(web: WebSecurity) {
        web.ignoring().antMatchers("/assets/**", "/favicon.ico")
        logger.debug("> web configure <")

    }

    override fun authorizeRequestConfigure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/", "/login", "/logout", "/certification/**", "/oauth/**", "/portal/**", "/rest/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().ignoringAntMatchers("/rest/**")
        logger.debug("> http configure <")
    }
}
