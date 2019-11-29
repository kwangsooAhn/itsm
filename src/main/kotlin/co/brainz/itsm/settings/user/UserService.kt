package co.brainz.itsm.settings.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 사용자 관리 서비스
 */
@Service
class UserService {

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
    fun selectUser(userId: String ): UserEntity {
        return userRepository.findByUserId(userId)
    }


}