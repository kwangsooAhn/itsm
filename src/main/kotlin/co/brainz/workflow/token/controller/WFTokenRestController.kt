package co.brainz.workflow.token.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.token.dto.TokenDto
import co.brainz.workflow.token.dto.TokenSaveDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
@RequestMapping("/rest/wf/tokens")
class WFTokenRestController(private val wfEngine: WFEngine) {

    /**
     * 토큰 목록 조회.
     *
     * @param assignee
     * @param assigneeType
     * @param tokenStatus
     * @return List<TokenDto>
     */
    @GetMapping("")
    fun getTokens(@PathVariable assignee: String,
                  @PathVariable assigneeType: String,
                  @PathVariable tokenStatus: String): List<TokenDto> {
        return wfEngine.token().getTokens(assignee, assigneeType, tokenStatus)
    }

    /**
     * 토큰 신규 등록.
     *
     * @param tokenSaveDto
     * @return Boolean
     */
    @Transactional
    @PostMapping("")
    fun postTokenData(@RequestBody tokenSaveDto: TokenSaveDto): Boolean {
        return wfEngine.token().postTokenData(tokenSaveDto)
    }

    /**
     * 토큰 일반정보 조회.
     *
     * @param tokenId
     * @return TokenDto
     */
    @GetMapping("/{tokenId}")
    fun getToken(@PathVariable tokenId: String): TokenDto {
        return wfEngine.token().getToken(tokenId)
    }

    /**
     * 토큰 일반정보 업데이트.
     *
     * @param tokenId
     * @param tokenDto
     * @return Boolean
     */
    @PutMapping("/{tokenId}")
    fun putToken(@PathVariable tokenId: String, @RequestBody tokenDto: TokenDto): Boolean {
        return wfEngine.token().putToken(tokenDto)
    }

    /**
     * 토큰 상세정보 조회.
     *
     * @param tokenId
     * @return TokenSaveDto
     */
    @GetMapping("/{tokenId}/data")
    fun getTokenData(@PathVariable tokenId: String): TokenSaveDto {
        return wfEngine.token().getTokenData(tokenId)
    }

    /**
     * 토큰 상세정보 업데이트.
     *
     * @param tokenId
     * @param tokenSaveDto
     * @return Boolean
     */
    @Transactional
    @PutMapping("/{tokenId}/data")
    fun putTokenData(@PathVariable tokenId: String, @RequestBody tokenSaveDto: TokenSaveDto): Boolean {
        return wfEngine.token().putTokenData(tokenSaveDto)
    }

}
