package co.brainz.itsm.settings.user

import co.brainz.itsm.common.CodeService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import kotlin.reflect.full.declaredMemberProperties

/**
 * 사용자 관리 뷰 클래스
 */
@Controller
@RequestMapping("/users")
class UserController {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var codeService: CodeService

    /**
     * 사용자 조회 뷰를 호출한다.
     */
    @GetMapping("/list")
    fun getUserList(model: Model): String {
        model.addAttribute("userSearchCode", codeService.selectCodeByParent(UserConstants.PCODE.value))
        return "user/user"
    }

    /**
     * 사용자 조회 리스트 뷰 호출한다.
     */
    @GetMapping("/search")
    fun getUserSearch(users: String, model: Model): String {
        val mapper = jacksonObjectMapper()
        mapper.findAndRegisterModules() // localDateTime 변환을 위해 선언
        val mapperedUsers: MutableList<UserEntity> = mapper.readValue(users)
        model.addAttribute("users", mapperedUsers)

        logger.debug(">>> users {} <<<", users)
        logger.debug(">>> mapperd {} <<<", mapperedUsers)


        return "user/userList"
    }

    /**
     * 사용자 조회 상세 뷰 호출한다.
     */
    @GetMapping("/detail")
    fun getUserDetail(user: String, model: Model): String {
        val mapper = jacksonObjectMapper()
        mapper.findAndRegisterModules() // localDateTime 변환을 위해 선언
        val mapperedUser: UserEntity = mapper.readValue(user)


        val userArray = arrayListOf<MutableMap<String, Any>>()

        mapperedUser::class.declaredMemberProperties.forEach {

            logger.debug(">>> UserEntity property {} <<<", it.name)
            logger.debug(">>> UserEntity property value {} <<<", it.getter.call(mapperedUser))

            if (UserConstants.UserCodeAndColumnMap.getUserColumnToCode(it.name) != "") {
                val userMap = mutableMapOf<String, Any>()
                userMap["code"] = UserConstants.UserCodeAndColumnMap.getUserColumnToCode(it.name)
                userMap["property"] = it.name
                userMap["value"] = it.getter.call(mapperedUser) as Any
                userArray.add(userMap)
            }
        }

        model.addAttribute("users", userArray)
        model.addAttribute("roles", mapperedUser.roleEntities)


        logger.debug(">>> users {} <<<", user)
        logger.debug(">>> mapperd {} <<<", mapperedUser)


        return "user/userDetail"
    }


}