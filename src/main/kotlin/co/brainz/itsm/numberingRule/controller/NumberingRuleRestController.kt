/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.numberingRule.dto.NumberingRuleDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleSearchCondition
import co.brainz.itsm.numberingRule.service.NumberingRuleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/rest/numberingRules")
@RestController
class NumberingRuleRestController(private val numberingRuleService: NumberingRuleService) {

    /**
     * 문서번호 리스트 조회
     */
    @GetMapping("/", "")
    fun getNumberingRuleList(
        numberingRuleSearchCondition: NumberingRuleSearchCondition
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            numberingRuleService.getNumberingRuleList(numberingRuleSearchCondition).data
        )
    }

    /**
     * 문서번호 상세 정보 조회.
     */
    @GetMapping("/{numberingId}")
    fun getNumberingRuleDetail(@PathVariable numberingId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(numberingRuleService.getNumberingRuleDetail(numberingId))
    }

    /**
     * 문서번호 등록.
     */
    @PostMapping("/", "")
    fun saveNumberingPattern(
        @RequestBody numberingRuleDto: NumberingRuleDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(numberingRuleService.saveNumberingRule(numberingRuleDto))
    }

    /**
     * 문서번호 수정.
     */
    @PutMapping("/{numberingId}")
    fun updateNumberingRule(
        @RequestBody numberingRuleDto: NumberingRuleDto
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(numberingRuleService.saveNumberingRule(numberingRuleDto))
    }

    /**
     * 문서번호 삭제.
     */
    @DeleteMapping("/{numberingId}")
    fun deleteNumberingRule(@PathVariable numberingId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(numberingRuleService.deleteNumberingRule(numberingId))
    }
}
