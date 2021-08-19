/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.controller

import co.brainz.itsm.numberingPattern.dto.NumberingPatternDetailDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternSearchCondition
import co.brainz.itsm.numberingPattern.service.NumberingPatternService
import javax.validation.Valid
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

    /**
     * 패턴 리스트 조회.
     */
    @GetMapping("/", "")
    fun getNumberingPatternList(numberingPatternSearchCondition: NumberingPatternSearchCondition): List<NumberingPatternListDto> {
        return numberingPatternService.getNumberingPatternList(numberingPatternSearchCondition).data
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
