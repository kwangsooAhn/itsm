/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.certification.service

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.certification.dto.AliceCertificationDto
import co.brainz.framework.certification.dto.AliceSignUpDto
import co.brainz.framework.certification.repository.AliceCertificationRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.encryption.AliceEncryptionUtil
import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.calendar.service.CalendarService
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.constants.UserConstants
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.PrivateKey
import java.time.LocalDateTime
import java.util.TimeZone
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Service
class AliceCertificationService(
    private val aliceCertificationRepository: AliceCertificationRepository,
    private val userRepository: AliceUserRepository,
    private val roleRepository: RoleRepository,
    private val codeService: CodeService,
    private val userRoleMapRepository: AliceUserRoleMapRepository,
    private val aliceResourceProvider: AliceResourceProvider,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val aliceEncryptionUtil: AliceEncryptionUtil,
    private val calendarService: CalendarService
) {
    @Value("\${password.expired.period}")
    private var passwordExpiredPeriod: Long = 90L

    @Value("\${encryption.algorithm}")
    private val algorithm: String = ""

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getDefaultUserRoleList(pRole: String): List<AliceRoleEntity> {
        val roleList = mutableListOf<AliceRoleEntity>()
        val codeList = codeService.selectCodeByParent(pRole)
        val roleIdList = mutableListOf<String>()
        codeList.forEach { codeEntity ->
            codeEntity.codeValue?.let { codeValue -> roleIdList.add(codeValue) }
        }

        roleRepository.findByRoleIdIn(roleIdList).forEach { role ->
            roleList.add(role)
        }

        return roleList
    }

    @Transactional
    fun createUser(aliceSignUpDto: AliceSignUpDto, target: String?): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (signUpValid(aliceSignUpDto)) {
            UserConstants.ResponseStatus.STATUS_VALID_SUCCESS.code -> {
                val user = aliceCertificationRepository.save(this.setUserEntity(aliceSignUpDto, target))
                if (user.uploaded) {
                    this.setAvatarName(user)
                }
                this.setUserDetail(aliceSignUpDto, user, target)
                // 캘린더 생성
                this.calendarService.setCalendar(user)
                logger.info("New user created : $1", user.userName)
            }
            UserConstants.ResponseStatus.STATUS_ERROR_USER_ID_DUPLICATION.code -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
            UserConstants.ResponseStatus.STATUS_ERROR_EMAIL_DUPLICATION.code -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE_EMAIL
            }
        }
        return ZResponse(
            status = status.code
        )
    }

    fun signUpValid(aliceSignUpDto: AliceSignUpDto): String {
        var isContinue = true
        var code: String = UserConstants.ResponseStatus.STATUS_VALID_SUCCESS.code
        if (aliceCertificationRepository.countByUserId(aliceSignUpDto.userId) > 0) {
            code = UserConstants.ResponseStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
            isContinue = false
        }
        when (isContinue) {
            true -> {
                if (aliceCertificationRepository.countByEmail(aliceSignUpDto.email) > 0) {
                    code = UserConstants.ResponseStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
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
        var validCode: Int = UserConstants.UserStatus.SIGNUP.value
        if (userDto.status == UserConstants.UserStatus.CERTIFIED.code) {
            validCode = UserConstants.UserStatus.CERTIFIED.value
        } else if (userDto.status == UserConstants.UserStatus.EDIT.code) {
            validCode = UserConstants.UserStatus.EDIT.value
        }
        return validCode
    }

    @Transactional
    fun valid(uid: String): Int {
        val decryptUid: String = AliceEncryptionUtil().encryptDecoder(uid, AliceConstants.EncryptionAlgorithm.AES256.value)
        val values: List<String> = decryptUid.split(":".toRegex())
        val userDto: AliceUserEntity = findByUserId(values[1])
        var validCode: Int = UserConstants.UserStatus.SIGNUP.value

        when (userDto.status) {
            UserConstants.UserStatus.SIGNUP.code, UserConstants.UserStatus.EDIT.code -> {
                validCode = when (values[0]) {
                    userDto.certificationCode -> {
                        val certificationDto = AliceCertificationDto(
                            userDto.userId,
                            userDto.email,
                            "",
                            UserConstants.UserStatus.CERTIFIED.code,
                            null
                        )
                        updateUser(certificationDto)
                        UserConstants.UserStatus.CERTIFIED.value
                    }
                    else -> {
                        UserConstants.UserStatus.ERROR.value
                    }
                }
            }
            UserConstants.UserStatus.CERTIFIED.code -> validCode = UserConstants.UserStatus.OVER.value
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
            password = aliceEncryptionUtil.encryptEncoder(password.toString(), this.algorithm),
            userName = aliceSignUpDto.userName,
            email = aliceSignUpDto.email,
            position = aliceSignUpDto.position,
            department = aliceSignUpDto.department,
            officeNumber = aliceSignUpDto.officeNumber,
            mobileNumber = aliceSignUpDto.mobileNumber,
            expiredDt = LocalDateTime.now().plusDays(passwordExpiredPeriod),
            status = UserConstants.UserStatus.SIGNUP.code,
            oauthKey = "",
            lang = UserConstants.USER_LOCALE_LANG,
            timezone = TimeZone.getDefault().id,
            timeFormat = UserConstants.USER_TIME_FORMAT,
            theme = UserConstants.USER_THEME,
            createUser = UserConstants.CREATE_USER_ID
        )

        when (target) {
            UserConstants.ADMIN_ID -> {
                user.status = UserConstants.UserStatus.CERTIFIED.code
                user.timezone = aliceSignUpDto.timezone!!
                user.lang = aliceSignUpDto.lang!!
                user.theme = aliceSignUpDto.theme!!
                user.timeFormat = aliceSignUpDto.timeFormat!!
            }
        }
        this.setUploadAvatar(user, aliceSignUpDto.avatarUUID)

        return user
    }

    /**
     * 사용자 상세정보(역할 저장)
     */
    @Transactional
    fun setUserDetail(aliceSignUpDto: AliceSignUpDto, user: AliceUserEntity, target: String?) {
        when (target) {
            UserConstants.USER_ID -> {
                getDefaultUserRoleList(UserConstants.DefaultRole.USER_DEFAULT_ROLE.code).forEach { role ->
                    userRoleMapRepository.save(AliceUserRoleMapEntity(user, role))
                }
            }
            UserConstants.ADMIN_ID -> {
                aliceSignUpDto.roles!!.forEach {
                    userRoleMapRepository.save(AliceUserRoleMapEntity(user, roleRepository.findByRoleId(it)))
                }
            }
        }
    }

    /**
     * 업로드한 아바타 이미지정보를 [userEntity] ,[avatarUUID] 를 받아서 처리한다.
     *
     * @param userEntity
     * @param avatarUUID
     */
    private fun setUploadAvatar(userEntity: AliceUserEntity, avatarUUID: String) {
        when (avatarUUID.isNotBlank()) {
            true -> {
                val tempDir = aliceResourceProvider.getExternalPath(ResourceConstants.FileType.AVATAR_TEMP.code)
                val tempPath = Paths.get(tempDir.toString() + File.separator + avatarUUID)

                if (tempPath.toFile().exists()) {
                    val avatarDir = aliceResourceProvider.getExternalPath(ResourceConstants.FileType.AVATAR.code)
                    val avatarFilePath = Paths.get(avatarDir.toString() + File.separator + avatarUUID)
                    Files.move(tempPath, avatarFilePath, StandardCopyOption.REPLACE_EXISTING)
                    userEntity.avatarValue = avatarUUID
                    userEntity.uploaded = true
                    userEntity.uploadedLocation = avatarFilePath.toString()
                }
            }
            false -> {
                val uploadedFile = Paths.get(userEntity.uploadedLocation)

                if (uploadedFile.toFile().exists()) {
                    Files.delete(uploadedFile)
                }
                userEntity.avatarValue = UserConstants.AVATAR_BASIC_FILE_NAME
                userEntity.uploaded = false
                userEntity.uploadedLocation = UserConstants.AVATAR_BASIC_FILE_PATH
            }
        }
    }

    /**
     * 아바타 이미지명을 uuid 에서 ID 값으로 변경 한다.
     * 신규 사용자 등록 시 avatar_id, user_key 를 구할 수가 없기 때문에
     * 임시적으로 생성한 avatar_uuid 로 파일명을 만든다. avatar_uuid 가 고유 값을 보장 하지 못하기 때문에
     * 사용자, 아바타 정보를 등록 후 다시 한번 파일명 및 아바타 이미지명을 변경한다.
     *
     * @param userEntity
     */
    private fun setAvatarName(userEntity: AliceUserEntity) {
        if (userEntity.avatarType == UserConstants.AvatarType.FILE.code &&
            userEntity.userKey != userEntity.avatarValue
        ) {
            val avatarDir = aliceResourceProvider.getExternalPath(ResourceConstants.FileType.AVATAR.code)
            val avatarFilePath = Paths.get(avatarDir.toString() + File.separator + userEntity.avatarValue)
            val avatarIdFilePath = Paths.get(avatarDir.toString() + File.separator + userEntity.userKey)
            val avatarUploadFile = File(avatarFilePath.toString())
            if (avatarUploadFile.exists()) {
                Files.move(avatarFilePath, avatarIdFilePath, StandardCopyOption.REPLACE_EXISTING)
                userEntity.avatarValue = userEntity.userKey
                userEntity.uploadedLocation = avatarIdFilePath.toString()
                userRepository.save(userEntity)
            }
        }
    }
}
