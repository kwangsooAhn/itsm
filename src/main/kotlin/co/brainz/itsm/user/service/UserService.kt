package co.brainz.itsm.user.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.entity.UserSpecification
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.dto.UserSearchDto
import co.brainz.framework.constants.UserConstants
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.framework.certification.repository.CertificationRepository
import co.brainz.framework.certification.service.CertificationService
import co.brainz.framework.certification.service.MailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.CryptoRsa
import java.security.PrivateKey
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.dao.EmptyResultDataAccessException

/**
 * 사용자 관리 서비스
 */
@Service
class UserService(private val userRepository: UserRepository,
                        private val roleRepository: RoleRepository,
                        private val certificationRepository: CertificationRepository,
                        private val cryptoRsa: CryptoRsa,
                        private val certificationService: CertificationService,
                        private val mailService: MailService) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 사용자 목록을 조회한다.
     */
    fun selectUserList(userSearchDto: UserSearchDto): MutableList<AliceUserEntity> {
        return userRepository.findAll(UserSpecification(userSearchDto))
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

    /**
     * 사용자 ID로 정보를 수정한다.
     */
   fun updateUser(update: UserUpdateDto): AliceUserEntity {
        val targetEntity = userRepository.findByUserId(update.userId!!)
        update.userName?.let { targetEntity.userName = update.userName!! }
        update.email?.let { targetEntity.email = update.email!! }
        update.position?.let { targetEntity.position = update.position!! }
        update.department?.let { targetEntity.department = update.department }
        update.extensionNumber?.let { targetEntity.extensionNumber = update.extensionNumber }
        update.certificationCode?.let { targetEntity.certificationCode = update.certificationCode!! }
        update.status?.let { targetEntity.status = update.status!! }

        targetEntity.roleEntities = update.roles?.let {
            roleRepository.findAllById(it).toMutableSet()
        }

        targetEntity.updateDt = LocalDateTime.now()
        targetEntity.updateUserkey = SecurityContextHolder.getContext().authentication.principal as String

        logger.debug("targetEntity {}, update {}", targetEntity, update)

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
                val targetEntity = userRepository.findByUserKey(update.userKey)
                val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
                val privateKey = attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
        
                if (targetEntity.password != update.password) {
                    val password = cryptoRsa.decrypt(privateKey, update.password!!)
                    update.password.let { targetEntity.password = BCryptPasswordEncoder().encode(password)}
                }
        
                update.userId?.let { targetEntity.userName = update.userId!!}
                update.userName?.let { targetEntity.userName = update.userName!! }
                update.email?.let { targetEntity.email = update.email!! }
                update.position?.let { targetEntity.position = update.position!! }
                update.department?.let { targetEntity.department = update.department }
                update.extensionNumber?.let { targetEntity.extensionNumber = update.extensionNumber }

                targetEntity.updateDt = LocalDateTime.now()
                targetEntity.updateUserkey = SecurityContextHolder.getContext().authentication.principal as String
        
                logger.debug("targetEntity {}, updateEdit {}", targetEntity, update)
                
                userRepository.save(targetEntity)
                code = UserConstants.UserEditStatus.STATUS_SUCCESS.code
                
            }
            
        }
        return code
    }
    
    fun userEditValid(update: UserUpdateDto): String {
        var isContinue = true
        var code: String = UserConstants.UserEditStatus.STATUS_VALID_SUCCESS.code
      /*  if (userRepository.findByIdOrNull(update.userId!!) != null) {
            code = UserConstants.SignUpStatus.STATUS_ERROR_USER_ID_DUPLICATION.code
            isContinue = false
        }
        when (isContinue) {
            true -> {
                try {
                    if (certificationRepository.countByEmail(update.email!!) > 0) {
                        code = UserConstants.SignUpStatus.STATUS_ERROR_EMAIL_DUPLICATION.code
                    }
                } catch (e: EmptyResultDataAccessException) {
                }
            }
        }*/
        return code        
        
    }
}