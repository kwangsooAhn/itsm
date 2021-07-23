/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapPk
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.certification.repository.AliceCertificationRepository
import co.brainz.framework.certification.service.AliceCertificationMailService
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.fileTransaction.service.AliceFileAvatarService
import co.brainz.framework.timezone.AliceTimezoneEntity
import co.brainz.framework.timezone.AliceTimezoneRepository
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.dto.*
import co.brainz.itsm.user.entity.UserCustomEntity
import co.brainz.itsm.user.mapper.UserMapper
import co.brainz.itsm.user.repository.UserCustomRepository
import co.brainz.itsm.user.repository.UserRepository
import java.nio.file.Paths
import java.security.PrivateKey
import java.util.Optional
import kotlin.random.Random
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 사용자 관리 서비스
 */
@Service
class UserService(
    private val aliceCertificationMailService: AliceCertificationMailService,
    private val aliceCertificationRepository: AliceCertificationRepository,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val codeService: CodeService,
    private val userAliceTimezoneRepository: AliceTimezoneRepository,
    private val userCustomRepository: UserCustomRepository,
    private val userRepository: UserRepository,
    private val userRoleMapRepository: AliceUserRoleMapRepository,
    private val roleRepository: RoleRepository,
    private val userDetailsService: AliceUserDetailsService,
    private val aliceFileAvatarService: AliceFileAvatarService
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    @Value("\${user.default.profile}")
    private val userDefaultProfile: String = ""

    /**
     * 사용자 목록을 조회한다.
     */
    fun selectUserList(search: String, offset: Long): UserListReturnDto {
        val queryResult = userRepository.findAliceUserEntityList(search, offset)
        val userList: MutableList<UserListDataDto> = mutableListOf()

        for (user in queryResult.data) {
            val avatarPath = userDetailsService.makeAvatarPath(user)
            user.avatarPath = avatarPath
            userList.add(user)
        }
        queryResult.data = userList
        return queryResult
    }

    /**
     * 사용자 ID로 해당 정보를 1건 조회한다.
     */
    fun selectUser(userId: String): AliceUserEntity {
        return userRepository.findByUserId(userId)
    }

    /**
     * 사용자 oauthKey, 플랫폼으로 해당 정보를 조회한다.
     */
    fun selectByOauthKeyAndPlatform(oauthKey: String, platform: String): Optional<AliceUserEntity> {
        return userRepository.findByOauthKeyAndPlatform(oauthKey, platform)
    }

    /**
     * 사용자 KEY로 정보를 수정한다.
     */
    @Transactional
    fun updateUser(userUpdateDto: UserUpdateDto): AliceUserEntity {
        val targetEntity = updateDataInput(userUpdateDto)
        return userRepository.save(targetEntity)
    }

    /**
     * 사용자의 KEY로 해당 정보 1건을 조회한다.
     */
    fun selectUserKey(userKey: String): AliceUserEntity {
        return userDetailsService.selectUserKey(userKey)
    }

    /**
     * 사용자의 정보를 수정한다.
     *
     * @param userEditType
     */
    @Transactional
    fun updateUserEdit(userUpdateDto: UserUpdateDto, userEditType: String): String {
        var code: String = userEditValid(userUpdateDto)
        when (code) {
            AliceUserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code -> {
                val userEntity = userDetailsService.selectUserKey(userUpdateDto.userKey)
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey =
                    attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
                val targetEntity = updateDataInput(userUpdateDto)

                when (userUpdateDto.password != null) {
                    targetEntity.password != userUpdateDto.password -> {
                        val password = aliceCryptoRsa.decrypt(privateKey, userUpdateDto.password!!)
                        userUpdateDto.password.let { targetEntity.password = BCryptPasswordEncoder().encode(password) }
                    }
                }

                aliceFileAvatarService.uploadAvatarFile(targetEntity, userUpdateDto.avatarUUID)

                logger.debug("targetEntity {}, update {}", targetEntity, userUpdateDto)
                userRepository.save(targetEntity)

                if (targetEntity.uploaded) {
                    aliceFileAvatarService.avatarFileNameMod(targetEntity)
                }

                when (userEditType == AliceUserConstants.UserEditType.ADMIN_USER_EDIT.code) {
                    true -> {
                        userEntity.userRoleMapEntities.forEach {
                            userRoleMapRepository.deleteById(AliceUserRoleMapPk(userUpdateDto.userKey, it.role.roleId))
                        }
                        userUpdateDto.roles!!.forEach {
                            userRoleMapRepository.save(
                                AliceUserRoleMapEntity(
                                    targetEntity,
                                    roleRepository.findByRoleId(it)
                                )
                            )
                        }
                    }
                }

                code = when (targetEntity.email == userEntity.email) {
                    true -> {
                        when (userEditType) {
                            AliceUserConstants.UserEditType.ADMIN_USER_EDIT.code ->
                                AliceUserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_ADMIN.code
                            AliceUserConstants.UserEditType.SELF_USER_EDIT.code ->
                                AliceUserConstants.UserEditStatus.STATUS_SUCCESS.code
                            else -> AliceUserConstants.UserEditStatus.STATUS_SUCCESS.code
                        }
                    }
                    false -> AliceUserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_EMAIL.code
                }
            }
        }
        return code
    }

    /**
     * 자기정보 수정 시, 이메일 및 ID의 중복을 검사한다.
     */
    fun userEditValid(userUpdateDto: UserUpdateDto): String {
        val targetEntity = userDetailsService.selectUserKey(userUpdateDto.userKey)
        var code: String = AliceUserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code

        when (true) {
            targetEntity.userId != userUpdateDto.userId -> {
                if (userRepository.countByUserId(userUpdateDto.userId) > 0) {
                    code = AliceUserConstants.SignUpStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
                }
            }
            targetEntity.email != userUpdateDto.email -> {
                if (aliceCertificationRepository.countByEmail(userUpdateDto.email!!) > 0) {
                    code = AliceUserConstants.SignUpStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                }
            }
        }
        return code
    }

    /**
     * 사용자 수정 관련 데이터 저장 공통화
     */
    fun updateDataInput(userUpdateDto: UserUpdateDto): AliceUserEntity {
        val targetEntity = userDetailsService.selectUserKey(userUpdateDto.userKey)
        userUpdateDto.userId.let { targetEntity.userId = userUpdateDto.userId }
        userUpdateDto.userName?.let { targetEntity.userName = userUpdateDto.userName!! }
        userUpdateDto.email?.let { targetEntity.email = userUpdateDto.email!! }
        userUpdateDto.position?.let { targetEntity.position = userUpdateDto.position!! }
        userUpdateDto.department?.let { targetEntity.department = userUpdateDto.department }
        userUpdateDto.officeNumber?.let { targetEntity.officeNumber = userUpdateDto.officeNumber }
        userUpdateDto.mobileNumber?.let { targetEntity.mobileNumber = userUpdateDto.mobileNumber }
        userUpdateDto.timezone?.let { targetEntity.timezone = userUpdateDto.timezone!! }
        userUpdateDto.lang?.let { targetEntity.lang = userUpdateDto.lang!! }
        userUpdateDto.timeFormat?.let { targetEntity.timeFormat = userUpdateDto.timeFormat!! }
        userUpdateDto.theme?.let { targetEntity.theme = userUpdateDto.theme!! }
        userUpdateDto.useYn?.let { targetEntity.useYn = userUpdateDto.useYn!! }

        return targetEntity
    }

    /**
     * 자기정보 수정 시, 타임존의 데이터를 가져온다.
     */
    fun selectTimezoneList(): MutableList<AliceTimezoneEntity> {
        return userAliceTimezoneRepository.findAllByOrderByTimezoneIdAsc()
    }

    /**
     * 모든 사용자 정보를 조회한다.
     * (selectbox 용으로 key, id, name 조회)
     */
    fun selectUserListOrderByName(): MutableList<UserSelectListDto> {
        val userList = userRepository.findByOrderByUserNameAsc()
        val userDtoList = mutableListOf<UserSelectListDto>()
        for (userEntity in userList) {
            userDtoList.add(
                UserSelectListDto(
                    userKey = userEntity.userKey,
                    userId = userEntity.userId,
                    userName = userEntity.userName
                )
            )
        }
        return userDtoList
    }

    /**
     * 사용자의 비밀번호를 생성한다.
     */
    fun makePassword(): String {
        var password = StringBuffer()
        for (i in 0..9) {
            val randomIndex = Random.nextInt(3)
            when (randomIndex) {
                0 -> password.append(((Random.nextInt(26)) + 97).toChar())
                1 -> password.append(((Random.nextInt(26)) + 65).toChar())
                2 -> password.append((Random.nextInt(10)))
            }
        }
        return password.toString()
    }

    /**
     * 사용자의 비밀번호를 초기화한다.
     */
    @Transactional
    fun resetPassword(userKey: String, password: String): String {
        val publicKey = aliceCryptoRsa.getPublicKey()
        val encryptPassword = aliceCryptoRsa.encrypt(publicKey, password)
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val privateKey =
            attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
        val decryptPassword = aliceCryptoRsa.decrypt(privateKey, encryptPassword)
        val targetEntity = userDetailsService.selectUserKey(userKey)
        targetEntity.password = BCryptPasswordEncoder().encode(decryptPassword)

        userRepository.save(targetEntity)

        aliceCertificationMailService.sendMail(
            targetEntity.userId,
            targetEntity.email,
            AliceUserConstants.SendMailStatus.UPDATE_USER_PASSWORD.code,
            password
        )
        return AliceUserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_PASSWORD.code
    }

    /**
     * Avatar 이미지 사이즈 조회
     */
    fun getUserAvatarSize(userEntity: AliceUserEntity): Long {
        return if (userEntity.uploaded) {
            Paths.get(userEntity.uploadedLocation).toFile().length()
        } else {
            ClassPathResource(userDefaultProfile).inputStream.available().toLong()
        }
    }

    /**
     * 사용자 설정 기본 코드 조회
     */
    fun getInitCodeList(): LinkedHashMap<String, List<CodeDto>> {
        val codes = mutableListOf(
            UserConstants.PTHEMECODE.value,
            UserConstants.PLANGCODE.value,
            UserConstants.PDATECODE.value,
            UserConstants.PTIMECODE.value
        )
        val codeList = codeService.selectCodeByParent(codes)
        val allCodes: LinkedHashMap<String, List<CodeDto>> = LinkedHashMap()
        allCodes["themeList"] = AliceUtil().getCodes(codeList, UserConstants.PTHEMECODE.value)
        allCodes["langList"] = AliceUtil().getCodes(codeList, UserConstants.PLANGCODE.value)
        allCodes["dateList"] = AliceUtil().getCodes(codeList, UserConstants.PDATECODE.value)
        allCodes["timeList"] = AliceUtil().getCodes(codeList, UserConstants.PTIMECODE.value)
        return allCodes
    }

    /**
     * 사용자 정의 색상 조회
     */
    fun getUserCustomColors(): UserCustomDto? {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userDetailsService.selectUserKey(aliceUserDto.userKey)
        return userCustomRepository.findByUserAndCustomType(userEntity, UserConstants.UserCustom.COLOR.code)
    }

    /**
     * 사용자 정의 색상 저장
     */
    fun updateUserCustomColors(userCustomDto: UserCustomDto): Boolean {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userDetailsService.selectUserKey(aliceUserDto.userKey)
        // 삭제
        userCustomRepository.deleteByUserAndAndCustomType(userEntity, UserConstants.UserCustom.COLOR.code)
        // 저장
        userCustomRepository.save(
            UserCustomEntity(
                user = userEntity,
                customType = UserConstants.UserCustom.COLOR.code,
                customValue = userCustomDto.customValue
            )
        )
        return true
    }
}
