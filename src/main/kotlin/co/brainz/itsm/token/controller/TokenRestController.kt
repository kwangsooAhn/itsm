package co.brainz.itsm.token.controller

import co.brainz.cmdb.provider.dto.CIComponentDataDto
import co.brainz.itsm.token.service.TokenService
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenSearchListDto
import javax.servlet.http.HttpServletRequest
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/tokens")
class TokenRestController(private val tokenService: TokenService) {

    /**
     * 문서함 목록 조회.
     * */
    @GetMapping("")
    fun getTokenList(
        restTemplateTokenSearchListDto: RestTemplateTokenSearchListDto
    ): List<RestTemplateInstanceViewDto> {
        return tokenService.getTokenList(restTemplateTokenSearchListDto)
    }

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

    /**
     * CI 컴포넌트 - CI 세부 정보 등록 / 수정
     */
    @PutMapping("/cis/{ciId}/data")
    fun saveCIComponentData(@PathVariable ciId: String, @RequestBody ciComponentDataDto: CIComponentDataDto): Boolean {
        return tokenService.saveCIComponentData(ciId, ciComponentDataDto)
    }

    /**
     * CI 컴포넌트 - CI 세부 정보 삭제
     */
    @DeleteMapping("/cis/data")
    fun deleteCIComponentData(request: HttpServletRequest): Boolean {
        val params = LinkedMultiValueMap<String, String>()
        params["ciId"] = request.getParameter("ciId")
        params["componentId"] = request.getParameter("componentId")
        return tokenService.deleteCIComponentData(params)
    }
}
