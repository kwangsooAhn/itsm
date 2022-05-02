package co.brainz.itsm.code.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.service.CodeService
import javax.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/codes")
class CodeRestController(private val codeService: CodeService) {

    /**
     * 코드 데이터 전체 목록 조회
     */
    @GetMapping("/", "")
    fun getCodeList(
        @RequestParam(value = "search", defaultValue = "") search: String,
        @RequestParam(value = "pCode", defaultValue = "") pCode: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(codeService.getCodeList(search, pCode))
    }

    /**
     * 코드 데이터 상세 정보 조회
     */
    @GetMapping("/{code}")
    fun getCodes(@PathVariable code: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(codeService.getDetailCodes(code))
    }

    /**
     * 연관 코드 상세 조회
     */
    @GetMapping("/related/{code}")
    fun getRelatedCodes(@PathVariable code: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(codeService.selectCodeByParent(code))
    }

    /**
     * 코드 데이터 등록
     */
    @PostMapping("/", "")
    fun createCode(
        @RequestBody @Valid codeDetailDto: CodeDetailDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(codeService.createCode(codeDetailDto))
    }

    /**
     * 코드 데이터 수정
     */
    @PutMapping("/{code}")
    fun updateCode(
        @RequestBody @Valid codeDetailDto: CodeDetailDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(codeService.updateCode(codeDetailDto))
    }

    /**
     * 코드 데이터 삭제
     */
    @DeleteMapping("/{code}")
    fun deleteCode(@PathVariable code: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(codeService.deleteCode(code))
    }

    /**
     * 코드 데이터 목록 조회 Excel 다운로드
     */
    @GetMapping("/excel")
    fun getCodeListExcelDownload(
        @RequestParam(value = "search", defaultValue = "") search: String,
        @RequestParam(value = "pCode", defaultValue = "") pCode: String
    ): ResponseEntity<ByteArray> {
        return codeService.getCodeListExcelDownload(search, pCode)
    }
}
