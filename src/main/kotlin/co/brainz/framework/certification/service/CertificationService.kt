package co.brainz.framework.certification.service

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.UserConstants
import co.brainz.framework.encryption.CryptoRsa
import co.brainz.framework.util.EncryptionUtil
import co.brainz.framework.certification.dto.CertificationDto
import co.brainz.framework.certification.dto.MailDto
import co.brainz.framework.certification.dto.SignUpDto
import co.brainz.framework.certification.repository.CertificationRepository
import co.brainz.itsm.code.repository.CodeRepository
import co.brainz.itsm.role.repository.RoleRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.Inet4Address
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.PrivateKey
import java.time.LocalDateTime
import java.util.TimeZone

@Service
public open class CertificationService(private val certificationRepository: CertificationRepository,
                                       private val roleRepository: RoleRepository,
                                       private val codeRepository: CodeRepository,
                                       private val mailService: MailService,
                                       private val cryptoRsa: CryptoRsa) {

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

    fun roleEntityList(role: String): Set<AliceRoleEntity>? {
        val codeEntityList = codeRepository.findByPCode(role)
        val roleIdList = mutableListOf<String>()
        codeEntityList.forEach {
            it.codeValue?.let { codeValue -> roleIdList.add(codeValue) }
        }
        return roleRepository.findByRoleIdIn(roleIdList)
    }

    fun insertUser(signUpDto: SignUpDto): String {
        var code: String = signUpValid(signUpDto)
        when (code) {
            UserConstants.SignUpStatus.STATUS_VALID_SUCCESS.code -> {
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey = attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
                val password = cryptoRsa.decrypt(privateKey, signUpDto.password)
                val userEntity = AliceUserEntity(
                        userKey = "",
                        userId = signUpDto.userId,
                        password = BCryptPasswordEncoder().encode(password),
                        userName = signUpDto.userName,
                        email = signUpDto.email,
                        position = signUpDto.position,
                        department = signUpDto.department,
                        officeNumber = signUpDto.officeNumber,
                        mobileNumber = signUpDto.mobileNumber,
                        expiredDt = LocalDateTime.now().plusMonths(3),
                        roleEntities = roleEntityList(UserConstants.DefaultRole.USER_DEFAULT_ROLE.code),
                        status = UserConstants.Status.SIGNUP.code,
                        oauthKey = "",
                        timezone = TimeZone.getDefault().id,
                        lang = UserConstants.USER_LOCALE_LANG,
                        timeformat = UserConstants.USER_TIME_FORMAT
                )
                certificationRepository.save(userEntity)
                code = UserConstants.SignUpStatus.STATUS_SUCCESS.code
            }
        }
        return code
    }

    fun signUpValid(signUpDto: SignUpDto): String {
        var isContinue = true
        var code: String = UserConstants.SignUpStatus.STATUS_VALID_SUCCESS.code
        if (certificationRepository.countByUserId(signUpDto.userId) > 0) {
            code = UserConstants.SignUpStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
            isContinue = false
        }
        when (isContinue) {
            true -> {
                try {
                    if (certificationRepository.countByEmail(signUpDto.email) > 0) {
                        code = UserConstants.SignUpStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                    }
                } catch (e: EmptyResultDataAccessException) {
                }
            }
        }
        return code
    }

    @Transactional
    fun sendMail(userId: String, email: String, target: String?) {
        var certificationKey: String = KeyGeneratorService().getKey(50, false)
        var statusCode = UserConstants.Status.SIGNUP.code

        when (target) {
            UserConstants.SendMailStatus.CREATE_USER.code -> {
                statusCode = UserConstants.Status.SIGNUP.code
            }
            UserConstants.SendMailStatus.UPDATE_USER_EMAIL.code -> {
                statusCode = UserConstants.Status.EDIT.code
            }
            UserConstants.SendMailStatus.UPDATE_USER.code -> {
                statusCode = UserConstants.Status.CERTIFIED.code
                certificationKey = ""
            }
        }

        val certificationDto = CertificationDto(userId, email, certificationKey, statusCode)
        updateUser(certificationDto)
        when (target) {
            UserConstants.SendMailStatus.CREATE_USER.code, UserConstants.SendMailStatus.UPDATE_USER_EMAIL.code -> sendCertificationMail(certificationDto)
        }
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

    fun findByUserId(userId: String): AliceUserEntity {
        return certificationRepository.findByUserId(userId)
    }

    fun status(): Int {
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: AliceUserEntity = findByUserId(userId)
        var validCode: Int = UserConstants.Status.SIGNUP.value
        if (userDto.status == UserConstants.Status.CERTIFIED.code) {
            validCode = UserConstants.Status.CERTIFIED.value
        } else if (userDto.status == UserConstants.Status.EDIT.code) {
            validCode = UserConstants.Status.EDIT.value
        }
        return validCode
    }

    @Transactional
    fun valid(uid: String): Int {
        val decryptUid: String = EncryptionUtil().twoWayDeCode(uid)
        val values: List<String> = decryptUid.split(":".toRegex())
        val userDto: AliceUserEntity = findByUserId(values[1])
        var validCode: Int = UserConstants.Status.SIGNUP.value

        when (userDto.status) {
            UserConstants.Status.SIGNUP.code, UserConstants.Status.EDIT.code -> {
                validCode = when (values[0]) {
                    userDto.certificationCode -> {
                        val certificationDto = CertificationDto(userDto.userId, userDto.email, "", UserConstants.Status.CERTIFIED.code)
                        updateUser(certificationDto)
                        UserConstants.Status.CERTIFIED.value
                    }
                    else -> {
                        UserConstants.Status.ERROR.value
                    }
                }
            }
            UserConstants.Status.CERTIFIED.code -> validCode = UserConstants.Status.OVER.value
        }
        return validCode
    }
}
