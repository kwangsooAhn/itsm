/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.certification.service

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.avatar.service.AliceAvatarService
import co.brainz.framework.certification.dto.AliceOAuthDto
import co.brainz.framework.certification.repository.AliceCertificationRepository
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.user.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDateTime
import java.util.Optional
import java.util.TimeZone
import javax.transaction.Transactional
import org.apache.commons.codec.binary.Base64
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.social.google.connect.GoogleOAuth2Template
import org.springframework.social.oauth2.GrantType
import org.springframework.social.oauth2.OAuth2Parameters
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class OAuthService(
    private val userService: UserService,
    private val userDetailsService: AliceUserDetailsService,
    private val avatarService: AliceAvatarService,
    private val aliceCertificationRepository: AliceCertificationRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    @Transactional
    fun callbackUrl(aliceOAuthDto: AliceOAuthDto) {
        when (isExistUser(aliceOAuthDto)) {
            false -> {
                logger.info("oAuth Save {}", aliceOAuthDto.oauthKey)
                oAuthSave(aliceOAuthDto)
            }
        }
        oAuthLogin(aliceOAuthDto)
    }

    fun oAuthSave(aliceOAuthDto: AliceOAuthDto) {
        val userEntity = AliceUserEntity(
            userKey = "",
            userId = aliceOAuthDto.userId,
            password = "",
            userName = aliceOAuthDto.userName,
            email = aliceOAuthDto.email,
            expiredDt = LocalDateTime.now().plusMonths(AliceUserConstants.USER_EXPIRED_VALUE),
            status = AliceUserConstants.Status.CERTIFIED.code,
            platform = aliceOAuthDto.platform,
            oauthKey = aliceOAuthDto.oauthKey,
            timezone = TimeZone.getDefault().id,
            lang = AliceUserConstants.USER_LOCALE_LANG,
            timeFormat = AliceUserConstants.USER_TIME_FORMAT,
            theme = AliceUserConstants.USER_THEME
        )
        aliceCertificationRepository.save(userEntity)
    }

    fun oAuthLogin(aliceOAuthDto: AliceOAuthDto) {

        var aliceUser: AliceUserAuthDto = userMapper.toAliceUserAuthDto(
            userDetailsService.loadUserByOauthKeyAndPlatform(
                aliceOAuthDto.oauthKey,
                aliceOAuthDto.platform
            )
        )
        aliceUser = userDetailsService.getAuthInfo(aliceUser)
        val avatarPath = avatarService.makeAvatarPath(aliceUser.avatar)
        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(aliceUser.oauthKey, aliceUser.password, aliceUser.grantedAuthorises)
        usernamePasswordAuthenticationToken.details = aliceUser.grantedAuthorises?.let { grantedAuthorises ->
            aliceUser.urls?.let { urls ->
                aliceUser.menus?.let { menus ->
                    AliceUserDto(
                        aliceUser.userKey, aliceUser.userId, aliceUser.userName, aliceUser.email, aliceUser.position,
                        aliceUser.department, aliceUser.officeNumber, aliceUser.mobileNumber, aliceUser.useYn,
                        aliceUser.tryLoginCount, aliceUser.expiredDt, aliceUser.oauthKey, grantedAuthorises,
                        menus, urls, aliceUser.timezone, aliceUser.lang, aliceUser.timeFormat, aliceUser.theme, avatarPath
                    )
                }
            }
        }
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
    }

    fun isExistUser(aliceOAuthDto: AliceOAuthDto): Boolean {
        var isExist = false
        if (aliceOAuthDto.oauthKey.isNotEmpty()) {
            val userDto: Optional<AliceUserEntity> =
                userService.selectByOauthKeyAndPlatform(aliceOAuthDto.oauthKey, aliceOAuthDto.platform)
            if (!userDto.isEmpty) {
                isExist = true
            }
        }
        return isExist
    }
}

@Component
class AliceOAuthServiceGoogle : AliceOAuthServiceIF {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${spring.oauth.google.client.clientId}")
    lateinit var clientId: String

    @Value("\${spring.oauth.google.client.clientSecret}")
    lateinit var secret: String

    @Value("\${spring.oauth.google.client.scope}")
    lateinit var scope: String

    @Value("\${spring.oauth.google.client.redirectUri}")
    lateinit var redirectUri: String

    @Value("\${spring.oauth.google.client.accessTokenUri}")
    lateinit var accessTokenUri: String

    override fun platformUrl(): String {
        return GoogleOAuth2Template(clientId, secret).buildAuthenticateUrl(
            GrantType.AUTHORIZATION_CODE,
            googleOAuth2Parameters()
        )
    }

    private fun googleOAuth2Parameters(): OAuth2Parameters {
        val oAuth2Parameters = OAuth2Parameters()
        oAuth2Parameters.scope = scope
        oAuth2Parameters.redirectUri = redirectUri
        return oAuth2Parameters
    }

    override fun setParameters(code: String): MultiValueMap<String, String> {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters.add("code", code)
        parameters.add("client_id", clientId)
        parameters.add("client_secret", secret)
        parameters.add("redirect_uri", redirectUri)
        parameters.add("grant_type", "authorization_code")
        return parameters
    }

    override fun callback(parameters: MultiValueMap<String, String>, platformValue: String): AliceOAuthDto {
        val responseMap: Map<String, Any>? = responseData(parameters)
        return makeOAuthDto(responseMap, platformValue)
    }

    fun makeOAuthDto(responseMap: Map<String, Any>?, platformValue: String): AliceOAuthDto {
        val idToken: String = responseMap!!["id_token"] as String
        val tokens: List<String> = idToken.split("\\.".toRegex())
        val base64 = Base64(true)
        val body = String(base64.decode(tokens[1]), charset = Charsets.UTF_8)
        val mapper = ObjectMapper()
        val result: MutableMap<*, *>? = mapper.readValue(body, MutableMap::class.java)
        val oAuthDto = AliceOAuthDto(platform = platformValue)
        if (result != null) {
            if (result["email"] != null) {
                oAuthDto.userId = result["email"] as String
                oAuthDto.email = result["email"] as String
                oAuthDto.oauthKey = result["email"] as String
            }
            if (result["name"] != null) {
                oAuthDto.userName = result["name"] as String
            }
        }
        return oAuthDto
    }

    fun responseData(parameters: MultiValueMap<String, String>): Map<String, Any>? {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val requestEntity = HttpEntity(parameters, headers)
        val responseEntity: ResponseEntity<Map<String, Any>> = restTemplate.exchange<Map<String, Any>>(
            accessTokenUri,
            HttpMethod.POST,
            requestEntity,
            MutableMap::class.java
        )
        return responseEntity.body
    }
}

@Component
class AliceOAuthServiceKakao : AliceOAuthServiceIF {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${spring.oauth.kakao.client.clientId}")
    lateinit var clientId: String

    @Value("\${spring.oauth.kakao.client.redirectUri}")
    lateinit var redirectUri: String

    @Value("\${spring.oauth.kakao.client.authorizeUri}")
    lateinit var authorizeUri: String

    @Value("\${spring.oauth.kakao.client.accessTokenUri}")
    lateinit var accessTokenUri: String

    @Value("\${spring.oauth.kakao.client.profileUri}")
    lateinit var profileUri: String

    override fun platformUrl(): String {
        val responseType = "code"
        return "$authorizeUri?client_id=$clientId&redirect_uri=$redirectUri&response_type=$responseType"
    }

    override fun setParameters(code: String): MultiValueMap<String, String> {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters.add("code", code)
        parameters.add("client_id", clientId)
        parameters.add("redirect_uri", redirectUri)
        parameters.add("grant_type", "authorization_code")
        return parameters
    }

    override fun callback(parameters: MultiValueMap<String, String>, platformValue: String): AliceOAuthDto {
        val accessTokenInfo = requestAccessTokenInfo(parameters)
        val oAuthDto = AliceOAuthDto(platform = platformValue)
        if (accessTokenInfo.isNotEmpty()) {
            val accessToken = jsonToMap(accessTokenInfo, "access_token")
            val profileInfo = requestProfile(accessToken)
            val mapper = ObjectMapper()
            val result: MutableMap<*, *> = mapper.readValue(profileInfo, MutableMap::class.java)
            val propertyMap = result["properties"] as MutableMap<*, *>
            val userName = propertyMap["nickname"] as String

            if (profileInfo.isNotEmpty()) {
                oAuthDto.userId = jsonToMap(profileInfo, "id")
                oAuthDto.oauthKey = jsonToMap(profileInfo, "id")
                oAuthDto.userName = userName
            }
        }
        return oAuthDto
    }

    fun jsonToMap(jsonStr: String, key: String): String {
        val mapper = ObjectMapper()
        val result: MutableMap<*, *>? = mapper.readValue(jsonStr, MutableMap::class.java)
        return getMapValue(result, key)
    }

    fun requestAccessTokenInfo(parameters: MultiValueMap<String, String>): String {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers["Authorization"] = "Bearer " + parameters["code"]
        val requestEntity = HttpEntity(parameters, headers)
        val responseEntity: ResponseEntity<String> =
            restTemplate.postForEntity(accessTokenUri, requestEntity, String::class.java)
        var token: String? = ""
        if (responseEntity.statusCode == HttpStatus.OK) {
            token = responseEntity.body
        }
        return token ?: ""
    }

    fun getMapValue(map: MutableMap<*, *>?, key: String): String {
        var returnValue: String? = ""
        if (map != null) {
            if (map[key] != null) {
                returnValue = map[key].toString()
            }
        }
        return returnValue ?: ""
    }

    fun requestProfile(accessToken: String): String {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        var profile: String? = ""
        if (accessToken.isNotEmpty()) {
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
            headers["Authorization"] = "Bearer $accessToken"
            val requestEntity = HttpEntity(null, headers)
            val responseEntity: ResponseEntity<String> =
                restTemplate.postForEntity(profileUri, requestEntity, String::class.java)
            if (responseEntity.statusCode == HttpStatus.OK) {
                profile = responseEntity.body
            }
        }
        return profile ?: ""
    }
}
