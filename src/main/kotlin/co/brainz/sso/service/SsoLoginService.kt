/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.sso.service

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceUtil
import java.security.PrivateKey
import javax.servlet.http.HttpServletRequest
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Service
class SsoLoginService(
    private val userDetailsService: AliceUserDetailsService,
    private val aliceCryptoRsa: AliceCryptoRsa
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    fun ssoLogin(request: HttpServletRequest) {
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val session = attr.request.session
        val privateKey = session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey

        val userId = aliceCryptoRsa.decrypt(privateKey, request.getParameter("userId"))

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

        if (!aliceUser.useYn) {
            throw DisabledException("Disabled account.")
        }

        aliceUser = userDetailsService.getAuthInfo(aliceUser)
        aliceUser.avatarPath = userDetailsService.makeAvatarPath(aliceUser)

        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(userId, aliceUser.password, aliceUser.grantedAuthorises)
        usernamePasswordAuthenticationToken.details = AliceUtil().setUserDetails(aliceUser)

        val securityContext = SecurityContextHolder.getContext()
        securityContext.authentication = usernamePasswordAuthenticationToken
    }
}
