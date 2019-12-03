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
    @Autowired
    lateinit var roleRepository: RoleRepository

    /**
     * 사용자 목록을 조회한다.
     */
    fun selectUserList(userSearchDto: UserSearchDto): MutableList<UserEntity> {
        //return userRepository.findAll(UserSpecification(userSearchDto))
        return userRepository.findAll()
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
        user.email?.let { updateEntity.email = user.email!! }
        user.position?.let { updateEntity.position = user.position!! }
        user.department?.let { updateEntity.department = user.department }
        user.extensionNumber?.let { updateEntity.extensionNumber = user.extensionNumber }


        user.roles?.let {
            updateEntity.roleEntities = roleRepository.findAllById(user.roles!!).toSet()
        }

        updateEntity.updateDate = LocalDateTime.now()
        updateEntity.updateId = SecurityContextHolder.getContext().authentication.principal as String
        return userRepository.save(updateEntity)
    }
}