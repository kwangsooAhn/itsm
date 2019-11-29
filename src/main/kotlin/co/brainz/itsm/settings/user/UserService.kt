package co.brainz.itsm.settings.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * 사용자 관리 서비스
 */
@Service
class UserService {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)
    @Autowired
    lateinit var userRepository: UserRepository

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
     * 사용자 ID로 정보를 수정한다.
     */
    fun updateUser(user: UserUpdateDto): UserEntity {
        val updateEntity = userRepository.findByUserId(user.userId)
        user.userName?.let { updateEntity.userName = user.userName!! }
        user.department?.let { logger.debug("update departmenet.") }

        updateEntity.updateDate = LocalDateTime.now()
        updateEntity.updateId = SecurityContextHolder.getContext().authentication.principal as String
        return userRepository.save(updateEntity)
    }


}