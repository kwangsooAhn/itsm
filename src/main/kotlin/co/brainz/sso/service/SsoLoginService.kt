/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.sso.service

import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.repository.AliceMenuRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.util.AliceUtil
import java.security.PrivateKey
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.LocaleResolver

@Service
class SsoLoginService(
    private val userDetailsService: AliceUserDetailsService,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val localeResolver: LocaleResolver,
    private val aliceMenuRepository: AliceMenuRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)
    private val layoutPage: String = "/layout"

    fun ssoLogin(request: HttpServletRequest, response: HttpServletResponse) {
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val session = attr.request.session
        val privateKey = session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey

        val userId = aliceCryptoRsa.decrypt(privateKey, request.getParameter("userId"))
        var aliceUser = userMapper.toAliceUserAuthDto(userDetailsService.loadUserByUsername(userId))
        aliceUser = userDetailsService.getAuthInfo(aliceUser)
        aliceUser.avatarPath = userDetailsService.makeAvatarPath(aliceUser)

        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(userId, aliceUser.password, aliceUser.grantedAuthorises)
        usernamePasswordAuthenticationToken.details = AliceUtil().setUserDetails(aliceUser)

    }
}
