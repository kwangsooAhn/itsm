package co.brainz.itsm.certification

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/certification")
class CertificationRestController(private val certificationService: CertificationService) {

    /**
     * 사용자를 등록한다.
     */
    @PostMapping("/register")
    fun setUser(@RequestBody signUp: SignUpDto): Int {
        // 가입 성공 : 0, 가입 실패 - 아이디 중복 : 1, 가입 실패 : 2
        var result: Int = certificationService.insertUser(signUp)
        if (result == 0) {
            // 메일 전송
        }
        return result
    }
}