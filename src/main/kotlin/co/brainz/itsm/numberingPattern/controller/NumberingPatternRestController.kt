package co.brainz.itsm.numberingPattern.controller

import co.brainz.itsm.numberingPattern.dto.NumberingPatternDetailDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto
import co.brainz.itsm.numberingPattern.service.NumberingPatternService
import javax.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/rest/numberingPatterns")
@RestController
class NumberingPatternRestController(private val numberingPatternService: NumberingPatternService) {

    private val logger = LoggerFactory.getLogger(NumberingPatternRestController::class.java)

    /**
     * 패턴 리스트 조회.
     */
    @GetMapping("/", "")
    fun getNumberingPatternList(search: String) : MutableList<NumberingPatternListDto> {
        return numberingPatternService.getNumberingPatternList(search)
    }

    /**
     * 패턴 상세 정보 조회.
     */
    @GetMapping("/{patternId}")
    fun getNumberingPatternsDetail(@PathVariable patternId: String): NumberingPatternDetailDto {
        return numberingPatternService.getNumberingPatternsDetail(patternId)
    }

    /**
     * 패턴 등록.
     */
    @PostMapping("/", "")
    fun saveNumberingPattern(@RequestBody @Valid numberingPatternDto: NumberingPatternDto): String {
        return numberingPatternService.saveNumberingPattern(numberingPatternDto)
    }

    /**
     * 패턴 수정.
     */
    @PutMapping("/{patternId}")
    fun updateNumberingPattern(@RequestBody @Valid numberingPatternDto: NumberingPatternDto): String {
        return numberingPatternService.saveNumberingPattern(numberingPatternDto)
    }

    /**
     * 패턴 삭제.
     */
    @DeleteMapping("/{patternId}")
    fun deleteNumberingPattern(@PathVariable patternId: String): String {
        return numberingPatternService.deleteNumberingPattern(patternId)
    }
}
