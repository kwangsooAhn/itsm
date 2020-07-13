package co.brainz.itsm.user.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapPk
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.certification.repository.AliceCertificationRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.timezone.AliceTimezoneEntity
import co.brainz.framework.timezone.AliceTimezoneRepository
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.dto.UserDto
import co.brainz.itsm.user.dto.UserListDto
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.mapper.UserMapper
import co.brainz.itsm.user.repository.UserRepository
import java.security.PrivateKey
import java.util.Optional
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 사용자 관리 서비스
 */
@Service
class UserService(
    private val aliceCertificationRepository: AliceCertificationRepository,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val aliceFileService: AliceFileService,
    private val userAliceTimezoneRepository: AliceTimezoneRepository,
    private val userRepository: UserRepository,
    private val userRoleMapRepository: AliceUserRoleMapRepository,
    private val roleRepository: RoleRepository
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    /**
     * 사용자 목록을 조회한다.
     */
    fun selectUserList(search: String, category: String): MutableList<UserDto> {
        val aliceUserEntities =
            userRepository.findAliceUserEntityList(search, category)
        val userList: MutableList<UserDto> = mutableListOf()
        aliceUserEntities.forEach {
            userList.add(userMapper.toUserDto(it))
        }
        return userList
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
    fun updateUser(update: UserUpdateDto): AliceUserEntity {
        val targetEntity = updateDataInput(update)
        return userRepository.save(targetEntity)
    }

    /**
     * 사용자의 KEY로 해당 정보 1건을 조회한다.
     */
    fun selectUserKey(userKey: String): AliceUserEntity {
        return userRepository.findByUserKey(userKey)
    }

    /**
     * 사용자의 정보를 수정한다.
     *
     * @param userEditType
     */
    fun updateUserEdit(userUpdateDto: UserUpdateDto, userEditType: String): String {
        var code: String = userEditValid(userUpdateDto)
        when (code) {
            AliceUserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code -> {
                val userEntity = userRepository.findByUserKey(userUpdateDto.userKey)
                val emailConfirmVal = userEntity.email
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey =
                    attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
                val targetEntity = updateDataInput(userUpdateDto)
                if (userUpdateDto.password != null) {
                    if (targetEntity.password != userUpdateDto.password) {
                        val password = aliceCryptoRsa.decrypt(privateKey, userUpdateDto.password!!)
                        userUpdateDto.password.let { targetEntity.password = BCryptPasswordEncoder().encode(password) }
                    }
                }
                logger.debug("targetEntity {}, update {}", targetEntity, userUpdateDto)
                userRepository.save(targetEntity)
                aliceFileService.uploadAvatar(
                    AliceUserConstants.USER_AVATAR_IMAGE_DIR,
                    AliceUserConstants.BASE_DIR,
                    userUpdateDto.userKey,
                    userUpdateDto.avatarUUID
                )

                if (userEditType == AliceUserConstants.UserEditType.ADMIN_USER_EDIT.code) {
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

                code = when (targetEntity.email == emailConfirmVal) {
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
        var isContinue = true
        val targetEntity = userRepository.findByUserKey(userUpdateDto.userKey)
        var code: String = AliceUserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code

        when (isContinue) {
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
        val targetEntity = userRepository.findByUserKey(userUpdateDto.userKey)
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
    fun selectUserListOrderByName(): MutableList<UserListDto> {
        val userList = userRepository.findByOrderByUserNameAsc()
        val userDtoList = mutableListOf<UserListDto>()
        for (userEntity in userList) {
            userDtoList.add(
                UserListDto(
                    userKey = userEntity.userKey,
                    userId = userEntity.userId,
                    userName = userEntity.userName
                )
            )
        }
        return userDtoList
    }
}
