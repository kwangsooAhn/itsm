package co.brainz.itsm.certification.service

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.CryptoRsa
import co.brainz.framework.util.EncryptionUtil
import co.brainz.itsm.certification.constants.CertificationConstants
import co.brainz.itsm.certification.CertificationDto
import co.brainz.itsm.certification.MailDto
import co.brainz.itsm.certification.SignUpDto
import co.brainz.itsm.certification.repository.CertificationRepository
import co.brainz.itsm.common.CodeRepository
import co.brainz.itsm.common.Constants
import co.brainz.itsm.common.KeyGenerator
import co.brainz.itsm.role.RoleEntity
import co.brainz.itsm.role.RoleRepository
import co.brainz.itsm.user.UserEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
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

    fun roleEntityList(role: String): Set<RoleEntity>? {
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
            CertificationConstants.SignUpStatus.STATUS_VALID_SUCCESS.code -> {
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey = attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
                val password = cryptoRsa.decrypt(privateKey, signUpDto.password)
                val userEntity = UserEntity(
                        userKey = "",
                        userId = signUpDto.userId,
                        password = BCryptPasswordEncoder().encode(password),
                        userName = signUpDto.userName,
                        email = signUpDto.email,
                        position = signUpDto.position,
                        department = signUpDto.department,
                        extensionNumber = signUpDto.extensionNumber,
                        createUserid = Constants.CREATE_USER_ID,
                        createDt = LocalDateTime.now(),
                        expiredDt = LocalDateTime.now().plusMonths(3),
                        //roleEntities = roleEntityList(CertificationConstants.DefaultRole.USER_DEFAULT_ROLE.code),
                        roleEntities = roleEntityList(""),
                        status = CertificationConstants.UserStatus.SIGNUP.code
                )
                certificationRepository.save(userEntity)
                code = CertificationConstants.SignUpStatus.STATUS_SUCCESS.code
            }
        }
        return code
    }

    fun signUpValid(signUpDto: SignUpDto): String {
        var isContinue = true
        var code: String = CertificationConstants.SignUpStatus.STATUS_VALID_SUCCESS.code
        if (certificationRepository.findByIdOrNull(signUpDto.userId) != null) {
            code = CertificationConstants.SignUpStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
            isContinue = false
        }
        when (isContinue) {
            true -> {
                try {
                    if (certificationRepository.countByEmail(signUpDto.email) > 0) {
                        code = CertificationConstants.SignUpStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                    }
                } catch (e: EmptyResultDataAccessException) {
                }
            }
        }
        return code
    }

    @Transactional
    fun sendMail(userId: String, email: String) {
        val certificationKey: String = KeyGenerator().getKey(50, false)
        val certificationDto: CertificationDto = CertificationDto(userId, email, certificationKey, CertificationConstants.UserStatus.SIGNUP.code)
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
        var validCode: Int = CertificationConstants.UserStatus.SIGNUP.value
        if (userDto.status == CertificationConstants.UserStatus.CERTIFIED.code) {
            validCode = CertificationConstants.UserStatus.CERTIFIED.value
        }
        return validCode
    }

    @Transactional
    fun valid(uid: String): Int {
        val decryptUid: String = EncryptionUtil().twoWayDeCode(uid)
        val values: List<String> = decryptUid.split(":".toRegex())
        val userDto: UserEntity = findByUserId(values[1])
        var validCode: Int = CertificationConstants.UserStatus.SIGNUP.value

        when (userDto.status) {
            CertificationConstants.UserStatus.SIGNUP.code -> {
                validCode = when (values[0]) {
                    userDto.certificationCode -> {
                        val certificationDto = CertificationDto(userDto.userId, userDto.email, "", CertificationConstants.UserStatus.CERTIFIED.code)
                        updateUser(certificationDto)
                        CertificationConstants.UserStatus.CERTIFIED.value
                    }
                    else -> {
                        CertificationConstants.UserStatus.ERROR.value
                    }
                }
            }
            CertificationConstants.UserStatus.CERTIFIED.code -> validCode = CertificationConstants.UserStatus.OVER.value
        }
        return validCode
    }
}
