package co.brainz.workflow.engine.token.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.token.dto.WfTokenDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
@RequestMapping("/rest/wf/tokens")
class WfTokenRestController(private val wfEngine: WfEngine) {

    /**
     * 토큰 목록 조회.
     *
     * @param parameters
     * @return List<TokenDto>
     */
    @GetMapping("")
    fun getTokens(@RequestParam parameters: LinkedHashMap<String, Any>): List<LinkedHashMap<String, Any>> {
        return wfEngine.token().getTokens(parameters)
    }

    /**
     * 토큰 일반정보 조회.
     *
     * @param tokenId
     * @return TokenDto
     */
    @GetMapping("/{tokenId}")
    fun getToken(@PathVariable tokenId: String): WfTokenDto {
        return wfEngine.token().getToken(tokenId)
    }

    /**
     * 토큰 상세정보 조회.
     *
     * @param tokenId
     * @return TokenViewDto
     */
    @GetMapping("/{tokenId}/data")
    fun getTokenData(@PathVariable tokenId: String): LinkedHashMap<String, Any> {
        return wfEngine.token().getTokenData(tokenId)
    }

    /**
     * Post Token Gate.
     *
     * @param wfTokenDto
     * @return Any
     */
    @Transactional
    @PostMapping("")
    fun postTokenGate(@RequestBody wfTokenDto: WfTokenDto) {
        return wfEngine.token().initToken(wfTokenDto)
    }

    /**
     * Put Token Gate.
     */
    @Transactional
    @PutMapping("")
    fun putTokenGate(@RequestBody wfTokenDto: WfTokenDto) {
        return wfEngine.token().setTokenGate(wfTokenDto)
    }

}
