package co.brainz.itsm.token.controller

import co.brainz.itsm.provider.dto.TokenDto
import co.brainz.itsm.token.service.TokenService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

@RestController
@RequestMapping("/rest/tokens")
class TokenRestController(private val tokenService: TokenService) {

    /**
     * token 신규 등록 / 처리
     */
    @PostMapping("/data")
    fun createToken(@RequestBody tokenDto: TokenDto): Boolean {
        return tokenService.createToken(tokenDto)
    }

    /**
     * token 수정 / 처리
     */
    @PutMapping("/data")
    fun updateToken(@RequestBody tokenDto: TokenDto): Boolean {
        return tokenService.updateToken(tokenDto)
    }
}
