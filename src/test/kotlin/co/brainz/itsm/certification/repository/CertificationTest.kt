package co.brainz.itsm.certification.repository

import co.brainz.framework.auth.security.AliceUserDto
import co.brainz.framework.util.EncryptionUtil
import co.brainz.itsm.certification.CertificationDto
import co.brainz.itsm.certification.CertificationEnum
import co.brainz.itsm.certification.serivce.CertificationService
import co.brainz.itsm.common.KeyGenerator
import co.brainz.itsm.settings.user.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
import java.net.Inet4Address

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["classpath:application.properties"])
class CertificationTest {

    @Autowired
    lateinit var certificationService: CertificationService
    lateinit var mvc: MockMvc
    lateinit var securityContext: SecurityContext

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Value("\${spring.mail.username}")
    lateinit var senderEmail: String

    @Value("\${spring.mail.titleName}")
    lateinit var senderName: String

    @Value("\${server.port}")
    lateinit var senderPort: String

    @Value("\${server.protocol}")
    lateinit var senderProtocol: String

    val host: String = Inet4Address.getLocalHost().hostAddress
    val userId: String = "lizeelf"
    val email: String = "phc@brainz.co.kr"

    @Before
    fun init() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        securityContext = SecurityContextHolder.getContext()
        val userDto: UserEntity = certificationService.findByUserId(userId)
        val aliceUserDto: AliceUserDto = AliceUserDto(userDto.userId, userDto.userName,userDto.email, userDto.useYn, userDto.tryLoginCount, userDto.expiredDt, emptySet(), emptySet())
        val usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDto.userId, userDto.password, emptySet())
        usernamePasswordAuthenticationToken.details = aliceUserDto
        securityContext.authentication = usernamePasswordAuthenticationToken
    }

    fun userStatusInit() {
        //Check user exists.
        val userDto: UserEntity = certificationService.findByUserId(userId)
        assertThat(userDto.email).isEqualTo(email)

        //User status init.
        val status : String = CertificationEnum.SIGNUP.code
        val certificationCode: String = ""
        val certificationDto: CertificationDto = CertificationDto(userId, email, certificationCode, status)
        certificationService.updateUser(certificationDto)

        //Check user status init.
        mvc.perform(get("/certification/status"))
                .andExpect(status().isOk)
                .andExpect(model().attribute("validCode", CertificationEnum.SIGNUP.value))
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

        val certificationKey: String = KeyGenerator().getKey(50, false)
        val certificationDto: CertificationDto = CertificationDto(userId, email, certificationKey, CertificationEnum.SIGNUP.code)
        certificationService.updateUser(certificationDto)

        val uid: String = "${certificationKey}:${userId}:${email}"
        val encryptUid: String = EncryptionUtil().twoWayEnCode(uid)
        mvc.perform(get("/certification/valid").param("uid", encryptUid))
                .andExpect(status().isOk)
                .andExpect(model().attribute("validCode", CertificationEnum.CERTIFIED.value))
    }

}