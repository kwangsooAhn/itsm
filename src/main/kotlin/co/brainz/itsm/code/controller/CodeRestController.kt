package co.brainz.itsm.code.controller

import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.service.CodeService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
    fun getCodes(@PathVariable code: String): CodeDetailDto {
        return codeService.getDetailCodes(code)
    }

    /**
     * 코드 데이터 등록
     */
    @PostMapping("/", "")
    fun createCode(@RequestBody codeDetailDto: CodeDetailDto) {
        return codeService.createCode(codeDetailDto)
    }

    /**
     * 코드 데이터 수정
     */
    @PutMapping("/{code}")
    fun updateCode(@RequestBody codeDetailDto: CodeDetailDto) {
        return codeService.createCode(codeDetailDto)
    }

    /**
     * 코드 데이터 삭제
     */
    @DeleteMapping("/{code}")
    fun deleteCode(@PathVariable code: String): String {
        return codeService.deleteCode(code)
    }
}
