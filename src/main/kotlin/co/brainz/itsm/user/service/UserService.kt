package co.brainz.itsm.user.service

import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.entity.UserSpecification
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.dto.UserSearchDto
import co.brainz.itsm.user.entity.UserEntity
import co.brainz.itsm.user.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional

/**
 * 사용자 관리 서비스
 */
@Service
class UserService(private val userRepository: UserRepository, private val roleRepository: RoleRepository) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

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
     * 사용자 ID, 플랫폼으로 해당 정보를 조회한다.
     */
    fun selectByUserIdAndPlatform(userId: String, platform: String): Optional<UserEntity> {
        return userRepository.findByUserIdAndPlatform(userId, platform)
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
        targetEntity.updateUserkey = SecurityContextHolder.getContext().authentication.principal as String

        logger.debug("targetEntity {}, update {}", targetEntity, update)

        return userRepository.save(targetEntity)
    }
}
