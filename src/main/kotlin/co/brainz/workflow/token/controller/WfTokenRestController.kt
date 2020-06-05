package co.brainz.workflow.token.controller

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateTokenViewDto
import co.brainz.workflow.token.service.WfTokenService
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
    private val wfEngine: WfEngine,
    private val wfTokenService: WfTokenService,
    private val aliceFileService: AliceFileService
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
     * @param restTemplateTokenDataUpdateDto
     * @return Any
     */
    @Transactional
    @PostMapping("")
    fun postTokenGate(@RequestBody restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        return wfEngine.startWorkflow(wfEngine.toTokenDto(restTemplateTokenDto))
    }

    /**
     * Put Token Gate.
     */
    @Transactional
    @PutMapping("/{tokenId}")
    fun putTokenGate(@RequestBody restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        return wfEngine.progressWorkflow(wfEngine.toTokenDto(restTemplateTokenDto))
    }
}
