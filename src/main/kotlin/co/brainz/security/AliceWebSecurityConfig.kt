package co.brainz.security

import co.brainz.framework.auth.security.AliceWebSecurityConfigurerAdapter
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Configuration
@EnableWebSecurity
class AliceWebSecurityConfig : AliceWebSecurityConfigurerAdapter() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun ignoreConfigure(web: WebSecurity) {
        web.ignoring().antMatchers("/assets/**", "/favicon.ico")
        logger.debug("> web configure <")

    }

    override fun authorizeRequestConfigure(http: HttpSecurity) {
        http.csrf().disable()
        http.authorizeRequests()
                .antMatchers("/login", "/logout", "/certification/signup", "/certification/regist", "/certification/valid", "/certification/status").permitAll()
                .anyRequest().authenticated()
        logger.debug("> http configure <")
    }
}