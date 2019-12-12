package co.brainz.itsm.certification.serivce

import co.brainz.framework.util.EncryptionUtil
import co.brainz.itsm.certification.CertificationDto
import co.brainz.itsm.certification.CertificationEnum
import co.brainz.itsm.certification.repository.CertificationRepository
import co.brainz.itsm.settings.user.UserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.net.Inet4Address
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.mail.internet.MimeMessage

@Service
public open class CertificationService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var javaMailSender: JavaMailSender

    @Autowired
    lateinit var springTemplateEngine: SpringTemplateEngine

    @Autowired
    lateinit var certificationRepository: CertificationRepository

    @Value("\${spring.mail.username}")
    lateinit var senderEmail: String

    @Value("\${spring.mail.titleName}")
    lateinit var senderName: String

    @Value("\${server.protocol}")
    lateinit var senderProtocol: String

    @Value("\${server.port}")
    lateinit var senderPort: String

    val host: String = Inet4Address.getLocalHost().hostAddress

    @Transactional
    fun sendMail(userId: String, email: String) {
        val certificationKey: String = KeyGenerator().getKey(50, false)
        val certificationDto: CertificationDto = CertificationDto(userId, email, certificationKey, CertificationEnum.SIGNUP.code)
        updateUser(certificationDto)
        sendCertificationMail(certificationDto)
    }

    @Transactional
    fun updateUser(certificationDto: CertificationDto) {
        return certificationRepository.saveCertification(certificationDto.userId, certificationDto.certificationCode, certificationDto.status)
    }

    fun sendCertificationMail(certificationDto: CertificationDto) {
        val uid: String = "${certificationDto.certificationCode}:${certificationDto.userId}:${certificationDto.email}"
        val encryptUid: String = EncryptionUtil().twoWayEnCode(uid)
        val urlEncryptUid: String = URLEncoder.encode(encryptUid, StandardCharsets.UTF_8)

        val subject: String = "[Alice Project] 인증메일"
        val context: Context = Context()
        context.setVariable("intro", "계정을 사용하기 위해 인증 작업이 필요합니다.")
        context.setVariable("message", "아래의 링크를 클릭하여 인증을 진행해주세요.")
        context.setVariable("link", "${senderProtocol}://${host}:${senderPort}/certification/valid?uid=${urlEncryptUid}")
        val content: String = springTemplateEngine.process("certification/emailTemplate", context)
        val message: MimeMessage = javaMailSender.createMimeMessage()
        val mimeMessageHelper: MimeMessageHelper = MimeMessageHelper(message, true)
        mimeMessageHelper.setFrom(senderEmail, senderName)
        mimeMessageHelper.setTo(certificationDto.email)
        mimeMessageHelper.setSubject(subject)
        mimeMessageHelper.setText(content, true)
        javaMailSender.send(message)
    }

    fun findByUserId(userId: String): UserEntity {
        return certificationRepository.findByUserId(userId)
    }
}