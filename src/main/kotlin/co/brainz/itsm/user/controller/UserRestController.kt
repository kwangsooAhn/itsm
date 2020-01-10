package co.brainz.itsm.user.controller

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.service.UserService
import co.brainz.itsm.user.dto.UserUpdateDto
import co.brainz.itsm.user.dto.UserSearchDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 사용자 관리 데이터 처리 클래스
 */
@RestController
@RequestMapping("/rest/users")
class UserRestController(private val userService: UserService) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 사용자 전체 목록을 조회한다.
     */
    @GetMapping("/", "")
    fun getUsers(userSearchDto: UserSearchDto): MutableList<AliceUserEntity> {
        return userService.selectUserList(userSearchDto)
    }

    /**
     * 사용자 ID로 해당 정보를 1건 조회한다.
     */
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): AliceUserEntity {
        return userService.selectUser(userId)
    }

    /**
     * 사용자를 업데이트한다.
     */
    @PutMapping("/{userId}")
    fun updateUser(user: UserUpdateDto): AliceUserEntity {
        return userService.updateUser(user)
    }
}
