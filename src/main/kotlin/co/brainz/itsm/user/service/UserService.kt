package co.brainz.itsm.user.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.UserConstants
import co.brainz.framework.certification.repository.CertificationRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.CryptoRsa
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.entity.UserSpecification
import co.brainz.framework.auth.entity.TimezoneEntity
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.framework.auth.repository.TimezoneRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.dao.EmptyResultDataAccessException
import java.security.PrivateKey
import java.util.Optional

/**
 * 사용자 관리 서비스
 */
@Service
class UserService(private val certificationRepository: CertificationRepository,
                  private val cryptoRsa: CryptoRsa,
                  private val roleRepository: RoleRepository,
                  private val userRepository: UserRepository,
                  private val userTimezoneRepository: TimezoneRepository,
                  private val codeService: CodeService) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 사용자 목록을 조회한다.
     */
    fun selectUserList(searchValue: String): MutableList<AliceUserEntity> {
        val codeList= codeService.selectCodeByParent(co.brainz.itsm.user.constants.UserConstants.PCODE.value)
        return userRepository.findAll(UserSpecification(codeList, searchValue))
    }

    /**
     * 사용자 ID로 해당 정보를 1건 조회한다.
     */
    fun selectUser(userId: String): AliceUserEntity {
        return userRepository.findByUserId(userId)
    }

    /**
     * 사용자 ID, 플랫폼으로 해당 정보를 조회한다.
     */
    fun selectByUserIdAndPlatform(userId: String, platform: String): Optional<AliceUserEntity> {
        return userRepository.findByUserIdAndPlatform(userId, platform)
    }

    /*
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

        /*targetEntity.roleEntities = update.roles?.let {
            roleRepository.findAllById(it).toMutableSet()
        }*/

        return userRepository.save(targetEntity)
    }

    /**
     * 사용자의 KEY로 해당 정보 1건을 조회한다.
     */
    fun selectUserKey(userKey: String): AliceUserEntity {
        return userRepository.findByUserKey(userKey)
    }

    /**
     * 사용자의 KEY로 정보를 수정한다.
     */
    fun updateUserEdit(update: UserUpdateDto): String {
        var code: String = userEditValid(update)
        when (code) {
            UserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code -> {
                val userEntity = userRepository.findByUserKey(update.userKey)
                val emailConfirmVal = userEntity.email
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey = attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
                val targetEntity = updateDataInput(update)

                if (targetEntity.password != update.password) {
                    val password = cryptoRsa.decrypt(privateKey, update.password!!)
                    update.password.let { targetEntity.password = BCryptPasswordEncoder().encode(password)}
                }
                logger.debug("targetEntity {}, update {}", targetEntity, update)
                userRepository.save(targetEntity)

                code =  if (targetEntity.email ==  emailConfirmVal) {
                    UserConstants.UserEditStatus.STATUS_SUCCESS.code
                } else {
                    UserConstants.UserEditStatus.STATUS_SUCCESS_EDIT_EMAIL.code
                }
            }
        }
        return code
    }

    /**
     * 자기정보 수정 시, 이메일 및 ID의 중복을 검사한다.
     */
    fun userEditValid(update: UserUpdateDto): String {
        var isContinue = true
        val targetEntity = userRepository.findByUserKey(update.userKey)
        var code: String = UserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code

        if (targetEntity.userId != update.userId) {
            if (userRepository.countByUserId(update.userId) > 0) {
                code = UserConstants.SignUpStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
                isContinue = false
            }
        }

        when (isContinue) {
            true -> {
                try {
                    if (targetEntity.email != update.email) {
                        if (certificationRepository.countByEmail(update.email!!) > 0) {
                            code = UserConstants.SignUpStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                        }
                    }
                } catch (e: EmptyResultDataAccessException) {
                }
            }
        }
        return code
    }

    /**
     * 사용자 수정 관련 데이터 저장 공통화
     */
    fun updateDataInput(update: UserUpdateDto): AliceUserEntity {
        val targetEntity = userRepository.findByUserKey(update.userKey)
        update.userId.let { targetEntity.userId = update.userId}
        update.userName?.let { targetEntity.userName = update.userName!! }
        update.email?.let { targetEntity.email = update.email!! }
        update.position?.let { targetEntity.position = update.position!! }
        update.department?.let { targetEntity.department = update.department }
        update.officeNumber?.let { targetEntity.officeNumber = update.officeNumber }
        update.mobileNumber?.let { targetEntity.mobileNumber = update.mobileNumber }
        update.timezone?.let { targetEntity.timezone = update.timezone!! }
        update.lang?.let { targetEntity.lang = update.lang!! }
        update.timeFormat?.let { targetEntity.timeFormat = update.timeFormat!! }

        return targetEntity
    }

    /**
     * 자기정보 수정 시, 타임존의 데이터를 가져온다.
     */
    fun selectTimezoneList(): MutableList<TimezoneEntity> {
        return userTimezoneRepository.findAllByOrderByTimezoneIdAsc()
    }
}
