package co.brainz.itsm.auth.controller

import co.brainz.itsm.auth.dto.AuthDto
import co.brainz.itsm.auth.dto.AuthListReturnDto
import co.brainz.itsm.auth.service.AuthService
import javax.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 권한 관리 데이터 처리 클래스
 */
@RestController
@RequestMapping("/rest/auths")
class AuthRestController(private val authService: AuthService) {

    val logger = LoggerFactory.getLogger(AuthRestController::class.java)

    /**
     * 권한 전체 목록 조회
     */
    @GetMapping("/", "")
    fun getAuths(): AuthListReturnDto {
        return authService.getAuthList()
    }

    /**
     * 권한 상세 정보 조회
     */
    @GetMapping("/{authId}")
    fun getAuths(@PathVariable authId: String): AuthDto {
        return authService.getAuthDetail(authId)
    }

    /**
     * 권한 등록
     */
    @PostMapping("/", "")
    fun createAuth(@RequestBody @Valid auth: AuthDto): String {
        return authService.createAuth(auth)
    }

    /**
     * 권한 수정
     */
    @PutMapping("/{authId}")
    fun updateAuth(@RequestBody @Valid auth: AuthDto): String {
        return authService.updateAuth(auth)
    }

    /**
     * 권한 삭제
     */
    @DeleteMapping("/{authId}")
    fun deleteAuth(@PathVariable authId: String): String {
        return authService.deleteAuth(authId)
    }
}
