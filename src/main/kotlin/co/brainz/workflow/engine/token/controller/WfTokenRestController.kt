package co.brainz.workflow.engine.token.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
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
class WfTokenRestController(private val wfEngine: WfEngine) {

    /**
     * 토큰 목록 조회.
     *
     * @param parameters
     * @return List<RestTemplateTokenDto>
     */
    @GetMapping("")
    fun getTokens(@RequestParam parameters: LinkedHashMap<String, Any>): List<RestTemplateTokenDto> {
        return wfEngine.token().getTokens(parameters)
    }

    /**
     * 토큰 일반정보 조회.
     *
     * @param tokenId
     * @return RestTemplateTokenDto
     */
    @GetMapping("/{tokenId}")
    fun getToken(@PathVariable tokenId: String): RestTemplateTokenDto {
        return wfEngine.token().getToken(tokenId)
    }

    /**
     * 토큰 상세정보 조회.
     *
     * @param tokenId
     * @return RestTemplateTokenViewDto
     */
    @GetMapping("/{tokenId}/data")
    fun getTokenData(@PathVariable tokenId: String): RestTemplateTokenViewDto {
        return wfEngine.token().getTokenData(tokenId)
    }

    /**
     * Post Token Gate.
     *
     * @param restTemplateTokenDto
     * @return Any
     */
    @Transactional
    @PostMapping("")
    fun postTokenGate(@RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto) {
        // 2020-05-29 Jung Hee Chan
        // 기존 함수들의 영향도를 최소화하기 위해서 임시로 작성.
        // 이 부분은 현재 진행되고 있는 WF 구조 변경에 따라 사라질 부분이라...
        // 하지만 token쪽에 추가된 dto들은 계속 사용할 것 같음.
        val dummyTokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>
        )
        return wfEngine.token().initToken(dummyTokenDto)
    }

    /**
     * Put Token Gate.
     */
    @Transactional
    @PutMapping("/{tokenId}")
    fun putTokenGate(@RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto) {
        // 2020-05-29 Jung Hee Chan
        // 기존 함수들의 영향도를 최소화하기 위해서 임시로 작성.
        // 이 부분은 현재 진행되고 있는 WF 구조 변경에 따라 사라질 부분이라...
        // 하지만 token쪽에 추가된 dto들은 계속 사용할 것 같음.
        val dummyTokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>
        )
        return wfEngine.token().initToken(dummyTokenDto)
    }
}
