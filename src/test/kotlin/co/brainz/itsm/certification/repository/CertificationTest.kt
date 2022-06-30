/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.certification.repository

/*
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.certification.dto.AliceCertificationDto
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.certification.service.AliceKeyGeneratorService
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.framework.encryption.AliceEncryptionUtil
import java.time.LocalDateTime
import java.util.TimeZone
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
*/
// @RunWith(SpringRunner::class)
// @SpringBootTest(properties = ["classpath:application.properties"])
class CertificationTest {
/*
    @Autowired
    lateinit var aliceCertificationService: AliceCertificationService

    @Autowired
    lateinit var aliceCertificationMailService: AliceCertificationMailService

    lateinit var mvc: MockMvc
    lateinit var securityContext: SecurityContext

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    val userId: String = "mail"
    val email: String = "brainz.zitsm@brainz.co.kr"

    @Before
    fun init() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        securityContext = SecurityContextHolder.getContext()
        val userDto: AliceUserEntity = aliceCertificationService.findByUserId(userId)
        val aliceUserDto = AliceUserDto(
            userKey = userDto.userKey,
            userId = userDto.userId,
            userName = userDto.userName,
            email = userDto.email,
            position = "",
            department = "",
            officeNumber = "",
            mobileNumber = "",
            useYn = userDto.useYn,
            tryLoginCount = userDto.tryLoginCount,
            expiredDt = LocalDateTime.now(),
            oauthKey = userDto.oauthKey,
            grantedAuthorises = emptySet(),
            menus = emptySet(),
            urls = emptySet(),
            timezone = TimeZone.getDefault().id,
            lang = "en",
            timeFormat = "YYYY-MM-DD HH:MM",
            theme = UserConstants.USER_THEME,
            avatarPath = ""
        )
        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(userDto.userId, userDto.password, emptySet())
        usernamePasswordAuthenticationToken.details = aliceUserDto
        securityContext.authentication = usernamePasswordAuthenticationToken
    }

    fun userStatusInit() {
        // Check user exists.
        val userDto: AliceUserEntity = aliceCertificationService.findByUserId(userId)
        assertThat(userDto.email).isEqualTo(email)

        // User status init.
        val status: String = UserConstants.Status.SIGNUP.code
        val certificationCode = ""
        val aliceCertificationDto = AliceCertificationDto(userId, email, certificationCode, status)
        aliceCertificationService.updateUser(aliceCertificationDto)

        // Check user status init.
        mvc.perform(get("/certification/status"))
            .andExpect(status().isOk)
            .andExpect(model().attribute("validCode", UserConstants.Status.SIGNUP.value))
    }

    // Send Mail
    @Test
    fun sendCertifiedMail() {
        userStatusInit()
        val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        aliceCertificationMailService.sendMail(aliceUserDto.userId, aliceUserDto.email, null, null)
    }

    // Valid CertificationCode
    @Test
    fun userMailValid() {
        userStatusInit()

        val certificationKey: String = AliceKeyGeneratorService().getKey(50, false)
        val aliceCertificationDto = AliceCertificationDto(
            userId,
            email,
            certificationKey,
            UserConstants.Status.SIGNUP.code
        )
        aliceCertificationService.updateUser(aliceCertificationDto)

        val uid: String
        uid = "$certificationKey:$userId:$email"
        val encryptUid: String = AliceEncryptionUtil().twoWayEnCode(uid)
        mvc.perform(get("/certification/valid").param("uid", encryptUid))
            .andExpect(status().isOk)
            .andExpect(model().attribute("validCode", UserConstants.Status.CERTIFIED.value))
    }*/
}
