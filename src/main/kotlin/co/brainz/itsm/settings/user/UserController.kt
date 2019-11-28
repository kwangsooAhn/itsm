package co.brainz.itsm.settings.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/**
 * 사용자 관리 뷰 클래스
 */
@Controller
@RequestMapping("/users")
class UserController {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var userService: UserService

    /**
     * 사용자 조회 뷰를 호출한다.
     */
    @GetMapping("/list")
    fun selectUserView(): String {

        //TODO 검색에 필요한 코드성 데이터 조회하여 전달
        return "user/list"
    }

    /**
     * 사용자 조회 리스트 뷰 호출한다.
     */
    @GetMapping("/list-search")
    fun selectUsers(users: String, model: Model): String {
        val mapper = jacksonObjectMapper()
        mapper.findAndRegisterModules() // localDateTime 변환을 위해 선언
        val mapperedUsers: MutableList<UserEntity> = mapper.readValue(users)
        model.addAttribute("users", mapperedUsers)

        logger.debug(">>> users {} <<<", users)
        logger.debug(">>> mapperd {} <<<", mapperedUsers)

        return "user/users"
    }


}