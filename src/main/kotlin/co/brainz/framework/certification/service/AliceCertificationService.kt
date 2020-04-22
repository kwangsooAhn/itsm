package co.brainz.framework.certification.service

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.certification.dto.AliceCertificationDto
import co.brainz.framework.certification.dto.AliceMailDto
import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.repository.AliceCertificationRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.encryption.AliceEncryptionUtil
import co.brainz.itsm.code.service.CodeService
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

/**
 * @since 1.0
 */
@Service
class AliceCertificationService(
    private val aliceCertificationRepository: AliceCertificationRepository,
    private val roleRepository: RoleRepository,
    private val codeService: CodeService,
    private val userRoleMapRepository: AliceUserRoleMapRepository,
    private val aliceMailService: AliceMailService,
    private val aliceCryptoRsa: AliceCryptoRsa
) {

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

    fun getDefaultUserRoleList(pRole: String): List<AliceRoleEntity> {
        val roleList = mutableListOf<AliceRoleEntity>()
        val codeEntityList = codeService.selectCodeByParent(pRole)
        val roleIdList = mutableListOf<String>()
        codeEntityList.forEach { codeEntity ->
            codeEntity.codeValue?.let { codeValue -> roleIdList.add(codeValue) }
        }

        roleRepository.findByRoleIdIn(roleIdList).forEach { role ->
            roleList.add(role)
        }

        return roleList
    }

    fun createUser(aliceSignUpDto: AliceSignUpDto, target: String?): String {
        var code: String = signUpValid(aliceSignUpDto)
        when (code) {
            AliceUserConstants.SignUpStatus.STATUS_VALID_SUCCESS.code -> {
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey =
                    attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
                val password = aliceSignUpDto.password?.let { aliceCryptoRsa.decrypt(privateKey, it) }
                var user = AliceUserEntity(
                    userKey = "",
                    userId = aliceSignUpDto.userId,
                    password = BCryptPasswordEncoder().encode(password),
                    userName = aliceSignUpDto.userName,
                    email = aliceSignUpDto.email,
                    position = aliceSignUpDto.position,
                    department = aliceSignUpDto.department,
                    officeNumber = aliceSignUpDto.officeNumber,
                    mobileNumber = aliceSignUpDto.mobileNumber,
                    expiredDt = LocalDateTime.now().plusMonths(3),
                    status = AliceUserConstants.Status.SIGNUP.code,
                    oauthKey = "",
                    timezone = TimeZone.getDefault().id,
                    lang = AliceUserConstants.USER_LOCALE_LANG,
                    timeFormat = AliceUserConstants.USER_TIME_FORMAT,
                    theme = AliceUserConstants.USER_THEME
                )

                when (target) {
                    AliceUserConstants.ADMIN_ID -> {
                        user.status = AliceUserConstants.Status.CERTIFIED.code
                        user.timezone = aliceSignUpDto.timezone!!
                        user.lang = aliceSignUpDto.lang!!
                        user.theme = aliceSignUpDto.theme!!
                        user.timeFormat = aliceSignUpDto.timeFormat!!
                    }
                }

                user = aliceCertificationRepository.save(user)

                when (target) {
                    AliceUserConstants.USER_ID -> {
                        getDefaultUserRoleList(AliceUserConstants.DefaultRole.USER_DEFAULT_ROLE.code).forEach { role ->
                            userRoleMapRepository.save(AliceUserRoleMapEntity(user, role))
                        }
                    }
                    AliceUserConstants.ADMIN_ID -> {
                        aliceSignUpDto.roles!!.forEach {
                            userRoleMapRepository.save(AliceUserRoleMapEntity(user, roleRepository.findByRoleId(it)))
                        }
                    }
                }

                code = AliceUserConstants.SignUpStatus.STATUS_SUCCESS.code
                logger.info("New user created : $1", user.userName)
            }
        }
        return code
    }

    fun signUpValid(aliceSignUpDto: AliceSignUpDto): String {
        var isContinue = true
        var code: String = AliceUserConstants.SignUpStatus.STATUS_VALID_SUCCESS.code
        if (aliceCertificationRepository.countByUserId(aliceSignUpDto.userId) > 0) {
            code = AliceUserConstants.SignUpStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
            isContinue = false
        }
        when (isContinue) {
            true -> {
                try {
                    if (aliceCertificationRepository.countByEmail(aliceSignUpDto.email) > 0) {
                        code = AliceUserConstants.SignUpStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                    }
                } catch (e: EmptyResultDataAccessException) {
                }
            }
        }
        return code
    }

    @Transactional
    fun sendMail(userId: String, email: String, target: String?, password: String?) {
        var certificationKey: String = AliceKeyGeneratorService().getKey(50, false)
        var statusCode = AliceUserConstants.Status.SIGNUP.code

        when (target) {
            AliceUserConstants.SendMailStatus.CREATE_USER.code -> {
                statusCode = AliceUserConstants.Status.SIGNUP.code
            }
            AliceUserConstants.SendMailStatus.UPDATE_USER_EMAIL.code -> {
                statusCode = AliceUserConstants.Status.EDIT.code
            }
            AliceUserConstants.SendMailStatus.UPDATE_USER.code, AliceUserConstants.SendMailStatus.CREATE_USER_ADMIN.code -> {
                statusCode = AliceUserConstants.Status.CERTIFIED.code
                certificationKey = ""
            }
        }

        val certificationDto = AliceCertificationDto(userId, email, certificationKey, statusCode, password)
        updateUser(certificationDto)
        when (target) {
            AliceUserConstants.SendMailStatus.CREATE_USER.code, AliceUserConstants.SendMailStatus.UPDATE_USER_EMAIL.code,
            AliceUserConstants.SendMailStatus.CREATE_USER_ADMIN.code -> sendCertificationMail(certificationDto)
        }
    }

    @Transactional
    fun updateUser(aliceCertificationDto: AliceCertificationDto) {
        return aliceCertificationRepository.saveCertification(
            aliceCertificationDto.userId,
            aliceCertificationDto.certificationCode,
            aliceCertificationDto.status
        )
    }

    fun makeLinkUrl(aliceCertificationDto: AliceCertificationDto): String {
        val uid =
            "${aliceCertificationDto.certificationCode}:${aliceCertificationDto.userId}:${aliceCertificationDto.email}"
        val encryptUid: String = AliceEncryptionUtil().twoWayEnCode(uid)
        val urlEncryptUid: String = URLEncoder.encode(encryptUid, StandardCharsets.UTF_8)
        return "${senderProtocol}://${host}:${senderPort}/certification/valid?uid=${urlEncryptUid}"
    }

    fun makeMailInfo(aliceCertificationDto: AliceCertificationDto): AliceMailDto {
        var subject = "[Alice Project] 인증메일"
        if (aliceCertificationDto.password != null) {
            subject = "[Alice Project] 비밀번호 안내메일"
        }
        val content: String = aliceMailService.content
        val to: ArrayList<String> = arrayListOf(aliceCertificationDto.email)
        return AliceMailDto(subject, content, senderEmail, senderName, to, arrayListOf(), arrayListOf())
    }

    fun sendCertificationMail(aliceCertificationDto: AliceCertificationDto) {
        aliceMailService.makeContext(makeContextValues(aliceCertificationDto))
        aliceMailService.makeTemplateEngine("certification/emailTemplate")
        val aliceMailDto: AliceMailDto = makeMailInfo(aliceCertificationDto)
        aliceMailService.makeMimeMessageHelper(aliceMailDto)
        aliceMailService.send()
    }

    fun makeContextValues(aliceCertificationDto: AliceCertificationDto): Map<String, Any> {
        val params: MutableMap<String, Any> = HashMap()
        if (aliceCertificationDto.password != null) {
            params["intro"] = "사용자 계정이 생성되었습니다."
            params["message"] = "비밀번호 :" + aliceCertificationDto.password
        } else {
            params["intro"] = "계정을 사용하기 위해 인증 작업이 필요합니다."
            params["message"] = "아래의 링크를 클릭하여 인증을 진행해주세요."
            params["link"] = makeLinkUrl(aliceCertificationDto)
            params["text"] = "이메일 인증"
        }

        return params
    }

    fun findByUserId(userId: String): AliceUserEntity {
        return aliceCertificationRepository.findByUserId(userId)
    }

    fun status(): Int {
        val userId: String = SecurityContextHolder.getContext().authentication.principal as String
        val userDto: AliceUserEntity = findByUserId(userId)
        var validCode: Int = AliceUserConstants.Status.SIGNUP.value
        if (userDto.status == AliceUserConstants.Status.CERTIFIED.code) {
            validCode = AliceUserConstants.Status.CERTIFIED.value
        } else if (userDto.status == AliceUserConstants.Status.EDIT.code) {
            validCode = AliceUserConstants.Status.EDIT.value
        }
        return validCode
    }

    @Transactional
    fun valid(uid: String): Int {
        val decryptUid: String = AliceEncryptionUtil().twoWayDeCode(uid)
        val values: List<String> = decryptUid.split(":".toRegex())
        val userDto: AliceUserEntity = findByUserId(values[1])
        var validCode: Int = AliceUserConstants.Status.SIGNUP.value

        when (userDto.status) {
            AliceUserConstants.Status.SIGNUP.code, AliceUserConstants.Status.EDIT.code -> {
                validCode = when (values[0]) {
                    userDto.certificationCode -> {
                        val certificationDto = AliceCertificationDto(
                            userDto.userId,
                            userDto.email,
                            "",
                            AliceUserConstants.Status.CERTIFIED.code,
                            null
                        )
                        updateUser(certificationDto)
                        AliceUserConstants.Status.CERTIFIED.value
                    }
                    else -> {
                        AliceUserConstants.Status.ERROR.value
                    }
                }
            }
            AliceUserConstants.Status.CERTIFIED.code -> validCode = AliceUserConstants.Status.OVER.value
        }
        return validCode
    }
}
