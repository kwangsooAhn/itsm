package co.brainz.workflow.token.controller

import co.brainz.workflow.token.service.WfTokenService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateTokenViewDto
import javax.transaction.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/tokens")
class WfTokenRestController(
    private val wfTokenService: WfTokenService
) {

    /**
     * 토큰 목록 조회.
     *
     * @param parameters
     * @return List<RestTemplateTokenDto>
     */
    @GetMapping("")
    fun getTokens(@RequestParam parameters: LinkedHashMap<String, Any>): List<RestTemplateTokenDto> {
        return wfTokenService.getTokens(parameters)
    }

    /**
     * 토큰 일반정보 조회.
     *
     * @param tokenId
     * @return RestTemplateTokenDto
     */
    @GetMapping("/{tokenId}")
    fun getToken(@PathVariable tokenId: String): RestTemplateTokenDto {
        return wfTokenService.getToken(tokenId)
    }

    /**
     * 토큰 상세정보 조회.
     *
     * @param tokenId
     * @return RestTemplateTokenViewDto
     */
    @GetMapping("/{tokenId}/data")
    fun getTokenData(@PathVariable tokenId: String): RestTemplateTokenViewDto {
        return wfTokenService.getTokenData(tokenId)
    }

    /**
     * Post Token Gate.
     *
     * @param restTemplateTokenDto
     * @return Any
     */
    @Transactional
    @PostMapping("")
    fun postTokenGate(@RequestBody restTemplateTokenDto: RestTemplateTokenDto) {
        return wfTokenService.initToken(restTemplateTokenDto)
    }

    /**
     * Put Token Gate.
     */
    @Transactional
    @PutMapping("/{tokenId}")
    fun putTokenGate(@RequestBody restTemplateTokenDto: RestTemplateTokenDto) {
        return wfTokenService.setToken(restTemplateTokenDto)
    }
}
