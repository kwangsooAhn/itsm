package co.brainz.framework.auth.service

import co.brainz.framework.auth.entity.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.CryptoRsa
import co.brainz.framework.menu.AliceMenuEntity
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
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

        val aliceUser: AliceUserEntity
        try {
            aliceUser = userDetailsService.loadUserByUsername(userId)
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

        val authorities = authorities(aliceUser)
        val menuList = menuList(aliceUser, authorities)
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userId, password, authorities)
        usernamePasswordAuthenticationToken.details = AliceUserDto(aliceUser.userId, aliceUser.userName, aliceUser.email, aliceUser.useYn,
                aliceUser.tryLoginCount, aliceUser.expiredDt, authorities, menuList)

        return usernamePasswordAuthenticationToken
    }

    fun authorities(aliceUser: AliceUserEntity): MutableSet<GrantedAuthority> {
        val authorities = mutableSetOf<GrantedAuthority>()
        authorities.run {
            aliceUser.getAuthorities().forEach {
                authorities.add(SimpleGrantedAuthority(it.authority))
            }
        }
        return authorities
    }

    fun menuList(aliceUser: AliceUserEntity, authorities: MutableSet<GrantedAuthority>): Set<AliceMenuEntity> {
        val menuList = mutableSetOf<AliceMenuEntity>()
        authorities.run {
            val authId = mutableSetOf<String>()
            aliceUser.getAuthorities().forEach {
                authId.add(it.authority)
            }

            val authList = userDetailsService.getAuthList(authId)
            authList.forEach {
                menuList.addAll(it.aliceMenuList)
            }
        }

        if (logger.isDebugEnabled) {
            menuList.forEach {
                logger.debug(">>> Get menu {} <<<", it.menuId)
            }
        }
        return menuList
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

}