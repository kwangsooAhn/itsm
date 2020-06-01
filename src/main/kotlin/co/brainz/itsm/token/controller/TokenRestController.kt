package co.brainz.itsm.token.controller

import co.brainz.itsm.token.service.TokenService
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/tokens")
class TokenRestController(private val tokenService: TokenService) {

    /**
     * 처리할 문서 데이터 조회.
     * */
    @GetMapping("/{tokenId}/data")
    fun getToken(@PathVariable tokenId: String): String {
        return tokenService.findToken(tokenId)
    }

    /**
     * Post Token 처리.
     */
    @PostMapping("/data")
    fun postToken(@RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        return tokenService.postToken(restTemplateTokenDataUpdateDto)
    }

    /**
     * Put Token 처리.
     */
    @PutMapping("/{tokenId}/data")
    fun putToken(
        @RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto,
        @PathVariable tokenId: String
    ): Boolean {
        return tokenService.putToken(tokenId, restTemplateTokenDataUpdateDto)
    }
}
