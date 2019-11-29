package co.brainz.itsm.settings.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * 사용자 관리 데이터 처리 클래스
 */
@RestController
@RequestMapping("/users")
class UserRestController {

    @Autowired
    lateinit var userService: UserService

    /**
     * 사용자 전체 목록을 조회한다.
     */
    @GetMapping("/", "")
    fun getUsers(userSearchDto: UserSearchDto): MutableList<UserEntity> {
        return userService.selectUserList(userSearchDto)
    }

    /**
     * 사용자 ID로 해당 정보를 1건 조회한다.
     */
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): UserEntity {
        return userService.selectUser(userId)
    }

    /**
     * 사용자를 저장한다.
     */
    @PostMapping("/{userId}")
    fun setUser(@PathVariable userId: String): String {
        return ""
    }

    /**
     * 사용자를 업데이트한다.
     */
    @PutMapping("/{userId}")
    fun updateUser(@PathVariable userId: String): String {
        return ""
    }

    /**
     * 사용자를 삭제한다.
     */
    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: String): String {
        return ""
    }

}