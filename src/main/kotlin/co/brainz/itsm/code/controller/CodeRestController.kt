package co.brainz.itsm.code.controller

import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.dto.CodeDtoHeechan
import co.brainz.itsm.code.service.CodeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/rest/codes")
class CodeRestController(private val codeService: CodeService) {

    /**
     * 코드 데이터 전체 목록 조회
     */
    @GetMapping("/", "")
    fun getCodes(): MutableList<CodeDto> {
        return codeService.getCodeList()
    }

    /**
     * 코드 데이터 상세 정보 조회
     */
    @GetMapping("/{code}")
    fun getCodes(@PathVariable code: String): CodeDtoHeechan {
        return codeService.getDetailCodes(code)
    }

    /**
     * 코드 데이터 등록
     */
    @PostMapping("/", "")
    fun createCode(@RequestBody codeDto: CodeDto) {
        return codeService.saveCode(codeDto)

    }

    /**
     * 코드 데이터 수정
     */
    @PutMapping("/{code}")
    fun updateCode(@RequestBody codeDto: CodeDto) {
        return codeService.saveCode(codeDto)
    }

    /**
     * 코드 데이터 삭제
     */
    @DeleteMapping("/{code}")
    fun deleteCode(@PathVariable code: String) {
        return codeService.deleteCode(code)
    }

}