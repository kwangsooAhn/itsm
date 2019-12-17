package co.brainz.itsm.certification.serivce

import co.brainz.framework.util.EncryptionUtil
import co.brainz.itsm.certification.CertificationDto
import co.brainz.itsm.certification.CertificationEnum
import co.brainz.itsm.certification.MailDto
import co.brainz.itsm.certification.repository.CertificationRepository
import co.brainz.itsm.common.KeyGenerator
import co.brainz.itsm.settings.user.UserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.Inet4Address
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
public open class CertificationService(private val certificationRepository: CertificationRepository,
                                       private val mailService: MailService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${server.protocol}")
    lateinit var senderProtocol: String

    @Value("\${server.port}")
    lateinit var senderPort: String

    @Value("\${spring.mail.username}")
    lateinit var senderEmail: String

    @Value("\${spring.mail.titleName}")
    lateinit var senderName: String

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

    fun makeLinkUrl(certificationDto: CertificationDto): String {
        val uid: String = "${certificationDto.certificationCode}:${certificationDto.userId}:${certificationDto.email}"
        val encryptUid: String = EncryptionUtil().twoWayEnCode(uid)
        val urlEncryptUid: String = URLEncoder.encode(encryptUid, StandardCharsets.UTF_8)
        return "${senderProtocol}://${host}:${senderPort}/certification/valid?uid=${urlEncryptUid}"
    }

    fun makeMailInfo(certificationDto: CertificationDto): MailDto {
        val subject: String = "[Alice Project] 인증메일"
        val content: String = mailService.content
        val to: ArrayList<String> = arrayListOf(certificationDto.email)
        return MailDto(subject, content, senderEmail, senderName, to, arrayListOf(), arrayListOf())
    }

    fun sendCertificationMail(certificationDto: CertificationDto) {
        mailService.makeContext(makeContextValues(certificationDto))
        mailService.makeTemplateEngine("certification/emailTemplate")
        val mailDto: MailDto = makeMailInfo(certificationDto)
        mailService.makeMimeMessageHelper(mailDto)
        mailService.send()
    }

    fun makeContextValues(certificationDto: CertificationDto): Map<String, Any> {
        val params: MutableMap<String, Any> = HashMap()
        params["intro"] = "계정을 사용하기 위해 인증 작업이 필요합니다."
        params["message"] = "아래의 링크를 클릭하여 인증을 진행해주세요."
        params["link"] = makeLinkUrl(certificationDto)
        return params
    }

    fun findByUserId(userId: String): UserEntity {
        return certificationRepository.findByUserId(userId)
    }

    fun status(): Int {
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: UserEntity = findByUserId(userId)
        var validCode: Int = CertificationEnum.SIGNUP.value
        if (userDto.status == CertificationEnum.CERTIFIED.code) {
            validCode = CertificationEnum.CERTIFIED.value
        }
        return validCode
    }

    @Transactional
    fun valid(uid: String): Int {
        val decryptUid: String = EncryptionUtil().twoWayDeCode(uid)
        val values: List<String> = decryptUid.split(":".toRegex())
        val userDto: UserEntity = findByUserId(values[1])
        var validCode: Int = CertificationEnum.SIGNUP.value

        when (userDto.status) {
            CertificationEnum.SIGNUP.code -> {
                validCode = when (values[0]) {
                    userDto.certificationCode -> {
                        val certificationDto: CertificationDto = CertificationDto(userDto.userId, userDto.email, "", CertificationEnum.CERTIFIED.code)
                        updateUser(certificationDto)
                        CertificationEnum.CERTIFIED.value
                    }
                    else -> {
                        CertificationEnum.ERROR.value
                    }
                }
            }
            CertificationEnum.CERTIFIED.code -> validCode = CertificationEnum.OVER.value
        }
        return validCode
    }
}