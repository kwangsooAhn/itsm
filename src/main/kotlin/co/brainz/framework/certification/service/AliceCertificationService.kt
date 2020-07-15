/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.certification.service

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.avatar.entity.AliceAvatarEntity
import co.brainz.framework.certification.dto.AliceCertificationDto
import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.repository.AliceCertificationRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.encryption.AliceEncryptionUtil
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.repository.RoleRepository
import java.security.PrivateKey
import java.time.LocalDateTime
import java.util.TimeZone
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Service
class AliceCertificationService(
    private val aliceCertificationRepository: AliceCertificationRepository,
    private val roleRepository: RoleRepository,
    private val codeService: CodeService,
    private val userRoleMapRepository: AliceUserRoleMapRepository,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val aliceFileService: AliceFileService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

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
                if (user.avatar.uploaded) {
                    this.avatarFileNameMod(user.avatar)
                }
                this.setUserDetail(aliceSignUpDto, user, target)
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
    fun updateUser(aliceCertificationDto: AliceCertificationDto) {
        return aliceCertificationRepository.saveCertification(
            aliceCertificationDto.userId,
            aliceCertificationDto.certificationCode,
            aliceCertificationDto.status
        )
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
        val avatarEntity = this.setUserAvatar(aliceSignUpDto)
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
            theme = AliceUserConstants.USER_THEME,
            avatar = avatarEntity
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
     * 사용자 상세정보(역할 저장)
     */
    private fun setUserDetail(aliceSignUpDto: AliceSignUpDto, user: AliceUserEntity, target: String?) {
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

    /**
     * 사용자 아바타 저장
     */
    private fun setUserAvatar(aliceSignUpDto: AliceSignUpDto): AliceAvatarEntity {
        return aliceFileService.uploadAvatar(
            "",
            aliceSignUpDto.avatarUUId,
            AliceUserConstants.USER_AVATAR_TYPE_FILE
        )
    }

    /**
     * 아바타 이미지명 변경
     */
    private fun avatarFileNameMod(avatarEntity: AliceAvatarEntity) {
        aliceFileService.avatarFileNameMod(avatarEntity)
    }
}
