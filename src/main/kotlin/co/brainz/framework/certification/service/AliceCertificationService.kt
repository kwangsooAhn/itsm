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
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.repository.RoleRepository
import java.net.Inet4Address
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.PrivateKey
import java.time.LocalDateTime
import java.util.TimeZone
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

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
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val aliceFileService: AliceFileService
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
                val user = aliceCertificationRepository.save(this.setUserEntity(aliceSignUpDto, target))
                this.setUserAvatar(aliceSignUpDto, user, target)
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
                if (aliceCertificationRepository.countByEmail(aliceSignUpDto.email) > 0) {
                    code = AliceUserConstants.SignUpStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                }
            }
        }
        return code
    }

    @Transactional
    fun sendMail(userId: String, email: String, target: String?, password: String?) {
        var certificationKey: String =
            AliceKeyGeneratorService().getKey(AliceConstants.EMAIL_CERTIFICATION_KEY_SIZE, false)
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
        return "$senderProtocol://$host:$senderPort/certification/valid?uid=$urlEncryptUid"
    }

    fun makeMailInfo(aliceCertificationDto: AliceCertificationDto): AliceMailDto {
        var subject = "[Alice ITSM] 이메일 주소를 인증해주세요"
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
            params["intro"] = "안녕하세요! Alice Project에 가입해주셔서 감사합니다."
            params["message"] = "본 메일은 Alice Project 가입을 완료하기 위한 인증메일입니다.\n" +
                    "만약 인증 메일을 요청하신 적이 없다면 본 메일을 삭제해주시기 바랍니다.\n" +
                    "이메일 인증을 위해 아래 버튼을 클릭해주세요."
            params["link"] = makeLinkUrl(aliceCertificationDto)
            params["text"] = "이메일 인증"
            params["regards"] = "감사합니다."
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

    /**
     * Set userEntity.
     */
    private fun setUserEntity(aliceSignUpDto: AliceSignUpDto, target: String?): AliceUserEntity {
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val privateKey =
            attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
        val password = aliceSignUpDto.password?.let { aliceCryptoRsa.decrypt(privateKey, it) }
        val user = AliceUserEntity(
            userKey = "",
            userId = aliceSignUpDto.userId,
            password = BCryptPasswordEncoder().encode(password),
            userName = aliceSignUpDto.userName,
            email = aliceSignUpDto.email,
            position = aliceSignUpDto.position,
            department = aliceSignUpDto.department,
            officeNumber = aliceSignUpDto.officeNumber,
            mobileNumber = aliceSignUpDto.mobileNumber,
            expiredDt = LocalDateTime.now().plusMonths(AliceConstants.EXPIRED_MONTH_PERIOD.toLong()),
            status = AliceUserConstants.Status.SIGNUP.code,
            oauthKey = "",
            lang = AliceUserConstants.USER_LOCALE_LANG,
            timezone = TimeZone.getDefault().id,
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

        return user
    }

    /**
     * Set user avatar.
     */
    private fun setUserAvatar(aliceSignUpDto: AliceSignUpDto, user: AliceUserEntity, target: String?) {
        aliceFileService.uploadAvatar(
            AliceUserConstants.USER_AVATAR_IMAGE_DIR,
            AliceUserConstants.BASE_DIR,
            user.userKey,
            aliceSignUpDto.avatarUUID
        )
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
    }
}
