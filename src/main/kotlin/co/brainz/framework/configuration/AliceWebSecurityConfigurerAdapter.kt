package co.brainz.framework.configuration

import co.brainz.framework.auth.handler.AliceAuthFailureHandler
import co.brainz.framework.auth.handler.AliceAuthSuccessHandler
import co.brainz.framework.auth.handler.AliceLogoutSuccessHandler
import co.brainz.framework.auth.service.AliceAuthProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

/**
 * spring security 적용을 위한 설정 클래스
 * 하위 모듈에서 상속받아 추상메소드를 구현하여 사용한다.
 */
abstract class AliceWebSecurityConfigurerAdapter(
    private val authProvider: AliceAuthProvider,
    private val authSuccessHandler: AliceAuthSuccessHandler,
    private val authFailureHandler: AliceAuthFailureHandler,
    private val logoutSuccessHandler: AliceLogoutSuccessHandler
) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity) {
        ignoreConfigure(web)
    }

    override fun configure(http: HttpSecurity) {
        authorizeRequestConfigure(http)
        commonConfigure(http)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authProvider)
    }

    /**
     * 보안 사항 설정에 적용받지 않을 대상을 추가한다.
     * 별도의 클래스를 생성하여 상속 후 오버라이딩 해야합니다.
     */
    abstract fun ignoreConfigure(web: WebSecurity)

    /**
     * 보안 사항을 설정한다.
     * 별도의 클래스를 생성하여 상속 후 오버라이딩 해야합니다.
     */
    abstract fun authorizeRequestConfigure(http: HttpSecurity)

    /**
     * 보안사항 설정시 공통으로 사용하는 메소드
     */
    private fun commonConfigure(http: HttpSecurity) {
        http.formLogin()
            .loginPage("/login")
            .usernameParameter("userId")
            .passwordParameter("password")
            .successHandler(authSuccessHandler)
            .failureHandler(authFailureHandler)
            .and()
            .logout()
            .logoutSuccessHandler(logoutSuccessHandler)
            .logoutUrl("/logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .and()
            .csrf()
            .requireCsrfProtectionMatcher(AntPathRequestMatcher("**/login"))
            .and()
            .sessionManagement()
            .invalidSessionStrategy(AliceInvalidSessionStrategy())

        // TODO csrf, 세션만료등 에러 핸들러 구현 요망 .and().exceptionHandling().accessDeniedHandler(AliceAccessDeniedHandler())
    }
}
