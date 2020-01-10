package co.brainz.itsm.certification.repository

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.UserConstants
import co.brainz.framework.util.EncryptionUtil
import co.brainz.itsm.certification.dto.CertificationDto
import co.brainz.itsm.certification.constants.CertificationConstants
import co.brainz.itsm.certification.service.CertificationService
import co.brainz.itsm.certification.service.KeyGeneratorService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["classpath:application.properties"])
class CertificationTest {

    @Autowired
    lateinit var certificationService: CertificationService
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
        val userDto: AliceUserEntity = certificationService.findByUserId(userId)
        val aliceUserDto: AliceUserDto = AliceUserDto(userDto.userKey, userDto.userId, userDto.userName, userDto.email, userDto.useYn, userDto.tryLoginCount, LocalDateTime.now(), emptySet(), emptySet(), emptySet())
        val usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDto.userId, userDto.password, emptySet())
        usernamePasswordAuthenticationToken.details = aliceUserDto
        securityContext.authentication = usernamePasswordAuthenticationToken
    }

    fun userStatusInit() {
        //Check user exists.
        val userDto: AliceUserEntity = certificationService.findByUserId(userId)
        assertThat(userDto.email).isEqualTo(email)

        //User status init.
        val status : String = CertificationConstants.Status.SIGNUP.code
        val certificationCode: String = ""
        val certificationDto: CertificationDto = CertificationDto(userId, email, certificationCode, status)
        certificationService.updateUser(certificationDto)

        //Check user status init.
        mvc.perform(get("/certification/status"))
                .andExpect(status().isOk)
                .andExpect(model().attribute("validCode", CertificationConstants.Status.SIGNUP.value))
    }

    //Send Mail
    @Test
    fun sendCertifiedMail() {
        userStatusInit()
        val aliceUserDto: AliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        certificationService.sendMail(aliceUserDto.userId, aliceUserDto.email)
    }

    //Valid CertificationCode
    @Test
    fun userMailValid() {
        userStatusInit()

        val certificationKey: String = KeyGeneratorService().getKey(50, false)
        val certificationDto: CertificationDto = CertificationDto(userId, email, certificationKey, CertificationConstants.Status.SIGNUP.code)
        certificationService.updateUser(certificationDto)

        val uid: String = "${certificationKey}:${userId}:${email}"
        val encryptUid: String = EncryptionUtil().twoWayEnCode(uid)
        mvc.perform(get("/certification/valid").param("uid", encryptUid))
                .andExpect(status().isOk)
                .andExpect(model().attribute("validCode", UserConstants.UserEnum.Status.CERTIFIED.value))
    }

}
