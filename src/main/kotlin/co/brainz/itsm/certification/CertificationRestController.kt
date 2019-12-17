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
    fun setUser(@RequestBody signUp: SignUpDto): String {
        var result = certificationService.insertUser(signUp)
        if (result == CertificationConstants.STATUS_SUCCESS.code) {
            // 메일 전송
        }
        return result
    }
}