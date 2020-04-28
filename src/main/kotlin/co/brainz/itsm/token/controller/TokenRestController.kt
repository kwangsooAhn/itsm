package co.brainz.itsm.token.controller

import co.brainz.itsm.token.service.TokenService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/rest/tokens")
class TokenRestController(private val tokenService: TokenService) {

    /**
     * 처리할 문서 데이터 조회.
     *
     * @param tokenId
     * */
    @GetMapping("/data/{tokenId}")
    fun getToken(@PathVariable tokenId: String): String {
        return tokenService.findToken(tokenId)
    }

    /**
     * Post Token 처리.
     *
     * @param restTemplateTokenDto
     * @return Boolean
     */
    @PostMapping("/data")
    fun postToken(@RequestBody restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        return tokenService.postToken(restTemplateTokenDto)
    }

    /**
     * Put Token 처리.
     */
    @PutMapping("/data")
    fun putToken(@RequestBody restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        return tokenService.putToken(restTemplateTokenDto)
    }

}
