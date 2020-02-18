package co.brainz.framework.auth.service

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.CryptoRsa
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.security.PrivateKey


/**
 * 로그인 요청을 인증하는 클래스
 *
 * rsa 복화화 된 값으로 사용자 정보를 조회하고 비밀번호를 대조한다.
 * 권한을 저장 후 사용자 토큰을 생성한다.
 * 정상적으로 처리가 완료되면 successHandler로 이동하고 실패시 failureHandler 로 이동한다.
 */
@Component
class AliceAuthProvider(private val userDetailsService: AliceUserDetailsService,
                        private val cryptoRsa: CryptoRsa) : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    @Transactional
    override fun authenticate(authentication: Authentication): Authentication {
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val session = attr.request.session
        val privateKey = session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey

        logger.debug(">>> Login id: {} <<<", authentication.principal.toString())
        logger.debug(">>> Login password: {} <<<", authentication.credentials.toString())

        val userId = cryptoRsa.decrypt(privateKey, authentication.principal.toString())
        val password = cryptoRsa.decrypt(privateKey, authentication.credentials.toString())
        val passwordEncoder = BCryptPasswordEncoder()

        logger.debug(">>> Decrypt id: {}", userId)
        logger.debug(">>> Decrypt password: {}", password)
        logger.debug(">>> password BCryptEncode: {}", passwordEncoder.encode(password))

        var aliceUser: AliceUserAuthDto
        try {
            aliceUser = userMapper.toAliceUserAuthDto(userDetailsService.loadUserByUsername(userId))
        } catch (e: EmptyResultDataAccessException) {
            logger.error("{}", e.message)
            throw UsernameNotFoundException("Empty access data")
        }

        if (!passwordEncoder.matches(password, aliceUser.password)) {
            logger.error(">>> Password not matched <<<")
            throw BadCredentialsException(userId)
        } else {
            logger.info(">>> Password matched <<<")
        }

        if (!aliceUser.useYn) {
            throw BadCredentialsException(userId)
        }

        aliceUser = userDetailsService.getAuthInfo(aliceUser)
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userId, password, aliceUser.grantedAuthorises)
        usernamePasswordAuthenticationToken.details = aliceUser.grantedAuthorises?.let { grantedAuthorises ->
            aliceUser.urls?.let { urls ->
                aliceUser.menus?.let { menus ->
                    AliceUserDto(
                            aliceUser.userKey, aliceUser.userId, aliceUser.userName, aliceUser.email, aliceUser.useYn,
                            aliceUser.tryLoginCount, aliceUser.expiredDt, aliceUser.oauthKey, grantedAuthorises,
                            menus, urls, aliceUser.timezone, aliceUser.lang, aliceUser.timeFormat
                    )
                }
            }
        }
        return usernamePasswordAuthenticationToken
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

}
