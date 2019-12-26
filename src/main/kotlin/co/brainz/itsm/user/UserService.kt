package co.brainz.itsm.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import co.brainz.itsm.role.RoleRepository

/**
 * 사용자 관리 서비스
 */
@Service
class UserService {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var roleRepository: RoleRepository

    /**
     * 사용자 목록을 조회한다.
     */
    fun selectUserList(userSearchDto: UserSearchDto): MutableList<UserEntity> {
        return userRepository.findAll(UserSpecification(userSearchDto))
    }

    /**
     * 사용자 ID로 해당 정보를 1건 조회한다.
     */
    fun selectUser(userId: String): UserEntity {
        return userRepository.findByUserId(userId)
    }

    /**
     * 사용자 EMAIL로 해당 정보를 조회한다.
     */
    fun selectByEmail(email: String): Optional<UserEntity> {
        return userRepository.findByEmail(email)
    }

    /**
     * 사용자 ID로 정보를 수정한다.
     */
    fun updateUser(update: UserUpdateDto): UserEntity {
        val targetEntity = userRepository.findByUserId(update.userId)
        update.userName?.let { targetEntity.userName = update.userName!! }
        update.email?.let { targetEntity.email = update.email!! }
        update.position?.let { targetEntity.position = update.position!! }
        update.department?.let { targetEntity.department = update.department }
        update.extensionNumber?.let { targetEntity.extensionNumber = update.extensionNumber }
        update.certificationCode?.let { targetEntity.certificationCode = update.certificationCode!! }
        update.status?.let { targetEntity.status = update.status }

        targetEntity.roleEntities = update.roles?.let {
            roleRepository.findAllById(it).toMutableSet()
        }

        targetEntity.updateDt = LocalDateTime.now()
        targetEntity.updateUserid = SecurityContextHolder.getContext().authentication.principal as String

        logger.debug("targetEntity {}, update {}", targetEntity, update)

        return userRepository.save(targetEntity)
    }
}