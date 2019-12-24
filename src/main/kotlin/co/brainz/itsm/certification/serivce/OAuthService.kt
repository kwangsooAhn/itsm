package co.brainz.itsm.certification.serivce

import co.brainz.framework.auth.entity.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.service.AliceAuthProvider
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.itsm.certification.DefaultRole
import co.brainz.itsm.certification.OAuthDto
import co.brainz.itsm.certification.ServiceTypeEnum
import co.brainz.itsm.certification.UserStatus
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
import java.util.UUID
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
                logger.info("oAuth Save {}", oAuthDto.email)
                oAuthSave(oAuthDto)
            }
        }
        oAuthLogin(oAuthDto.email)
    }

    fun oAuthSave(oAuthDto: OAuthDto) {
        val userEntity = UserEntity(
                userId = UUID.randomUUID().toString(),
                password = "",
                userName = oAuthDto.email,
                email = oAuthDto.email,
                createUserid = Constants.CREATE_USER_ID,
                createDt = LocalDateTime.now(),
                expiredDt = LocalDateTime.now().plusMonths(3),
                roleEntities = certificationService.roleEntityList(DefaultRole.USER_DEFAULT_ROLE.code),
                status = UserStatus.CERTIFIED.code,
                serviceType = oAuthDto.serviceType?.toUpperCase()?.let { ServiceTypeEnum.valueOf(it).code }
        )
        certificationRepository.save(userEntity)
    }

    fun oAuthLogin(email: String) {
        val aliceUser: AliceUserEntity = userDetailsService.loadUserByEmail(email)
        val authorities = aliceAuthProvider.authorities(aliceUser)
        val menuList = aliceAuthProvider.menuList(aliceUser, authorities)
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(aliceUser.userId, aliceUser.password, authorities)
        usernamePasswordAuthenticationToken.details = AliceUserDto(aliceUser.userId, aliceUser.userName, aliceUser.email, aliceUser.useYn,
                aliceUser.tryLoginCount, aliceUser.expiredDt, authorities, menuList)
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
    }

    fun isExistUser(oAuthDto: OAuthDto): Boolean {
        var isExist = false
        val userDto: Optional<UserEntity> = userService.selectByEmail(oAuthDto.email)
        when (userDto.isEmpty) {
            false -> isExist = true
        }
        return isExist
    }

}

@Component
class OAuthServiceGoogle(): OAuthServiceIF {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${google.client.clientId}")
    lateinit var googleClientId: String

    @Value("\${google.client.clientSecret}")
    lateinit var googleClientSecret: String

    @Value("\${google.client.scope}")
    lateinit var googleClientScope: String

    @Value("\${google.client.redirectUri}")
    lateinit var googleClientRedirectUri: String

    @Value("\${google.client.accessTokenUri}")
    lateinit var googleClientAccessTokenUri: String

    override fun serviceUrl(): String {
        return GoogleOAuth2Template(googleClientId, googleClientSecret).buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters())
    }

    private fun googleOAuth2Parameters(): OAuth2Parameters {
        val oAuth2Parameters = OAuth2Parameters()
        oAuth2Parameters.scope = googleClientScope
        oAuth2Parameters.redirectUri = googleClientRedirectUri
        return oAuth2Parameters
    }

    override fun setParameters(code: String): MultiValueMap<String, String> {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters.add("code", code)
        parameters.add("client_id", googleClientId)
        parameters.add("client_secret", googleClientSecret)
        parameters.add("redirect_uri", googleClientRedirectUri)
        parameters.add("grant_type", "authorization_code")
        return parameters
    }

    override fun callback(parameters: MultiValueMap<String, String>, service: String): OAuthDto {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val requestEntity = HttpEntity(parameters, headers)
        val responseEntity: ResponseEntity<Map<String, Any>> = restTemplate.exchange<Map<String, Any>>(googleClientAccessTokenUri, HttpMethod.POST, requestEntity, MutableMap::class.java)
        val responseMap: Map<String, Any>? = responseEntity.body
        val idToken: String = responseMap!!["id_token"] as String
        val tokens: List<String> = idToken.split("\\.".toRegex())
        val base64 = Base64(true)
        val body = String(base64.decode(tokens[1]), charset = Charsets.UTF_8)
        val mapper = ObjectMapper()
        val result: MutableMap<*, *>? = mapper.readValue(body, MutableMap::class.java)
        return OAuthDto(result?.get("email") as String, result?.get("name") as String, service)
    }

}

@Component
class OAuthServiceFacebook(): OAuthServiceIF {

    override fun serviceUrl(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun oAuth2Parameters(): OAuth2Parameters {
        val oAuth2Parameters = OAuth2Parameters()
        oAuth2Parameters.scope = "email"
        oAuth2Parameters.redirectUri = "https://localhost:80/oauth/facebook/callback"
        return oAuth2Parameters
    }

    override fun setParameters(code: String): MultiValueMap<String, String> {
        return LinkedMultiValueMap()
    }

    override fun callback(parameters: MultiValueMap<String, String>, service: String): OAuthDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}