package co.brainz.itsm.certification.repository

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.certification.dto.AliceCertificationDto
import co.brainz.framework.certification.service.AliceCertificationService
import co.brainz.framework.certification.service.AliceKeyGeneratorService
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.util.AliceEncryptionUtil
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
import java.time.LocalDateTime
import java.util.TimeZone

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["classpath:application.properties"])
class CertificationTest {

    @Autowired
    lateinit var aliceCertificationService: AliceCertificationService
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
        val aliceUserDto: AliceUserDto = AliceUserDto(userDto.userKey, userDto.userId, userDto.userName, userDto.email, "", "", "", "", userDto.useYn, userDto.tryLoginCount, LocalDateTime.now(), userDto.oauthKey, emptySet(), emptySet(), emptySet(), TimeZone.getDefault().id, "en","YYYY-MM-DD HH:MM", "defualt")
        val usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDto.userId, userDto.password, emptySet())
        usernamePasswordAuthenticationToken.details = aliceUserDto
        securityContext.authentication = usernamePasswordAuthenticationToken
    }

    fun userStatusInit() {
        //Check user exists.
        val userDto: AliceUserEntity = aliceCertificationService.findByUserId(userId)
        assertThat(userDto.email).isEqualTo(email)

        //User status init.
        val status : String = AliceUserConstants.Status.SIGNUP.code
        val certificationCode: String = ""
        val aliceCertificationDto: AliceCertificationDto = AliceCertificationDto(userId, email, certificationCode, status)
        aliceCertificationService.updateUser(aliceCertificationDto)

        //Check user status init.
        mvc.perform(get("/certification/status"))
                .andExpect(status().isOk)
                .andExpect(model().attribute("validCode", AliceUserConstants.Status.SIGNUP.value))
    }

    //Send Mail
    @Test
    fun sendCertifiedMail() {
        userStatusInit()
        val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        aliceCertificationService.sendMail(aliceUserDto.userId, aliceUserDto.email, null, null)
    }

    //Valid CertificationCode
    @Test
    fun userMailValid() {
        userStatusInit()

        val certificationKey: String = AliceKeyGeneratorService().getKey(50, false)
        val aliceCertificationDto: AliceCertificationDto = AliceCertificationDto(userId, email, certificationKey, AliceUserConstants.Status.SIGNUP.code)
        aliceCertificationService.updateUser(aliceCertificationDto)

        val uid: String = "${certificationKey}:${userId}:${email}"
        val encryptUid: String = AliceEncryptionUtil().twoWayEnCode(uid)
        mvc.perform(get("/certification/valid").param("uid", encryptUid))
                .andExpect(status().isOk)
                .andExpect(model().attribute("validCode", AliceUserConstants.Status.CERTIFIED.value))
    }

}
