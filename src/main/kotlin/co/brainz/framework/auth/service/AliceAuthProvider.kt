/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.service

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.encryption.AliceEncryptionUtil
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceUtil
import java.security.PrivateKey
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 로그인 요청을 인증하는 클래스
 *
 * rsa 복화화 된 값으로 사용자 정보를 조회하고 비밀번호를 대조한다.
 * 권한을 저장 후 사용자 토큰을 생성한다.
 * 정상적으로 처리가 완료되면 successHandler로 이동하고 실패시 failureHandler 로 이동한다.
 */
@Component
class AliceAuthProvider(
    private val userDetailsService: AliceUserDetailsService,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val aliceEncryptionUtil: AliceEncryptionUtil
) : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    @Value("\${encryption.algorithm}")
    private val algorithm: String = ""

    @Transactional
    override fun authenticate(authentication: Authentication): Authentication {
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val session = attr.request.session
        val privateKey = session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey

        val userId = aliceCryptoRsa.decrypt(privateKey, authentication.principal.toString())
        val password = aliceCryptoRsa.decrypt(privateKey, authentication.credentials.toString())

        var aliceUser: AliceUserAuthDto
        try {
            aliceUser = userMapper.toAliceUserAuthDto(userDetailsService.loadUserByUsername(userId))
        } catch (e: EmptyResultDataAccessException) {
            logger.error("{}", e.message)
            throw UsernameNotFoundException("Not registered User Data.")
        } catch (e: Exception) {
            logger.error("{}", e.message)
            throw AliceException(AliceErrorConstants.ERR, "Unknown error.")
        }

        this.validatePassword(password, aliceUser.password)

        if (!aliceUser.useYn) {
            throw DisabledException("Disabled account.")
        }

        aliceUser = userDetailsService.getAuthInfo(aliceUser)
        aliceUser.avatarPath = userDetailsService.makeAvatarPath(aliceUser)
        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(userId, password, aliceUser.grantedAuthorises)
        usernamePasswordAuthenticationToken.details = AliceUtil().setUserDetails(aliceUser)
        return usernamePasswordAuthenticationToken
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }

    private fun validatePassword(targetPassword: String, userPassword: String) {
        when (this.algorithm.toUpperCase()) {
            AliceConstants.EncryptionAlgorithm.BCRYPT.value -> {
                val bcryptPasswordEncoder = BCryptPasswordEncoder()
                if (!bcryptPasswordEncoder.matches(targetPassword, userPassword)) {
                    throw BadCredentialsException(null)
                }
            }
            AliceConstants.EncryptionAlgorithm.AES256.value, AliceConstants.EncryptionAlgorithm.SHA256.value -> {
                val encryptPassword = aliceEncryptionUtil.encryptEncoder(targetPassword, this.algorithm)
                if (encryptPassword != userPassword) {
                    throw BadCredentialsException(null)
                }
            }
            else -> {
                throw BadCredentialsException(null)
            }
        }
    }
}
