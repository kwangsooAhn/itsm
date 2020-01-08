package co.brainz.itsm.certification.service

import co.brainz.framework.auth.entity.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.service.AliceAuthProvider
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.itsm.certification.constants.CertificationConstants
import co.brainz.itsm.certification.OAuthDto
import co.brainz.itsm.certification.repository.CertificationRepository
import co.brainz.itsm.common.Constants
import co.brainz.itsm.user.UserEntity
import co.brainz.itsm.user.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.binary.Base64
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
import java.time.LocalDateTime
import java.util.Optional
import javax.transaction.Transactional

@Service
class OAuthService(private val userService: UserService,
                   private val userDetailsService: AliceUserDetailsService,
                   private val certificationService: CertificationService,
                   private val certificationRepository: CertificationRepository,
                   private val aliceAuthProvider: AliceAuthProvider) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun callbackUrl(oAuthDto: OAuthDto) {
        when (isExistUser(oAuthDto)) {
            false -> {
                logger.info("oAuth Save {}", oAuthDto.userid)
                oAuthSave(oAuthDto)
            }
        }
        oAuthLogin(oAuthDto)
    }

    fun oAuthSave(oAuthDto: OAuthDto) {
        val userEntity = UserEntity(
                userKey = "",
                userId = oAuthDto.userid,
                password = "",
                userName = oAuthDto.email,
                email = oAuthDto.email,
                createUserkey = Constants.CREATE_USER_ID,
                createDt = LocalDateTime.now(),
                expiredDt = LocalDateTime.now().plusMonths(Constants.USER_EXPIRED_VALUE),
                //roleEntities = certificationService.roleEntityList(CertificationConstants.DefaultRole.USER_DEFAULT_ROLE.code),
                roleEntities = certificationService.roleEntityList(""),
                status = CertificationConstants.UserStatus.CERTIFIED.code,
                platform = oAuthDto.platform
        )
        certificationRepository.save(userEntity)
    }

    fun oAuthLogin(oAuthDto: OAuthDto) {
        val aliceUser: AliceUserEntity = userDetailsService.loadUserByUserIdAndPlatform(oAuthDto.userid, oAuthDto.platform)
        val authorities = aliceAuthProvider.authorities(aliceUser)
        val authList = aliceAuthProvider.authList(aliceUser, authorities)
        val menuList = aliceAuthProvider.menuList(authList)
        val urlList = aliceAuthProvider.urlList(authList)
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(aliceUser.userId, aliceUser.password, authorities)
        usernamePasswordAuthenticationToken.details = AliceUserDto(aliceUser.userKey, aliceUser.userId, aliceUser.userName, aliceUser.email, aliceUser.useYn,
                aliceUser.tryLoginCount, aliceUser.expiredDt, authorities, menuList, urlList)
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
    }

    fun isExistUser(oAuthDto: OAuthDto): Boolean {
        var isExist = false
        if (oAuthDto.userid.isNotEmpty()) {
            val userDto: Optional<UserEntity> = userService.selectByUserIdAndPlatform(oAuthDto.userid, oAuthDto.platform)
            if (!userDto.isEmpty) {
                isExist = true
            }
        }
        return isExist
    }

}

@Component
class OAuthServiceGoogle: OAuthServiceIF {

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
        return GoogleOAuth2Template(clientId, secret).buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters())
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

    override fun callback(parameters: MultiValueMap<String, String>, platformValue: String): OAuthDto {
        val responseMap: Map<String, Any>? = responseData(parameters)
        return makeOAuthDto(responseMap, platformValue)
    }

    fun makeOAuthDto(responseMap: Map<String, Any>?, platformValue: String): OAuthDto {
        val idToken: String = responseMap!!["id_token"] as String
        val tokens: List<String> = idToken.split("\\.".toRegex())
        val base64 = Base64(true)
        val body = String(base64.decode(tokens[1]), charset = Charsets.UTF_8)
        val mapper = ObjectMapper()
        val result: MutableMap<*, *>? = mapper.readValue(body, MutableMap::class.java)
        val oAuthDto = OAuthDto(platform = platformValue)
        if (result != null) {
            if (result["email"] != null) {
                oAuthDto.userid = result["email"] as String
                oAuthDto.email = result["email"] as String
            }
        }
        return oAuthDto
    }

    fun responseData(parameters: MultiValueMap<String, String>): Map<String, Any>? {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val requestEntity = HttpEntity(parameters, headers)
        val responseEntity: ResponseEntity<Map<String, Any>> = restTemplate.exchange<Map<String, Any>>(accessTokenUri, HttpMethod.POST, requestEntity, MutableMap::class.java)
        return responseEntity.body
    }

}

@Component
class OAuthServiceKakao: OAuthServiceIF {

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
        return "${authorizeUri}?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=${responseType}"
    }

    override fun setParameters(code: String): MultiValueMap<String, String> {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters.add("code", code)
        parameters.add("client_id", clientId)
        parameters.add("redirect_uri", redirectUri)
        parameters.add("grant_type", "authorization_code")
        return parameters
    }

    override fun callback(parameters: MultiValueMap<String, String>, platformValue: String): OAuthDto {
        val accessTokenInfo = requestAccessTokenInfo(parameters)
        val oAuthDto = OAuthDto(platform = platformValue)
        if (accessTokenInfo.isNotEmpty()) {
            val accessToken = jsonToMap(accessTokenInfo, "access_token")
            val profileInfo = requestProfile(accessToken)
            if (profileInfo.isNotEmpty()) {
                oAuthDto.userid = jsonToMap(profileInfo, "id")
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
        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(accessTokenUri, requestEntity, String::class.java)
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
            val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(profileUri, requestEntity, String::class.java)
            if (responseEntity.statusCode == HttpStatus.OK) {
                profile = responseEntity.body
            }
        }
        return profile ?: ""
    }

}
