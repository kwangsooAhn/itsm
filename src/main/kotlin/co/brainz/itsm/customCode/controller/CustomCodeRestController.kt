/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.customCode.dto.CustomCodeDto
import co.brainz.itsm.customCode.dto.CustomCodeSearchCondition
import co.brainz.itsm.customCode.service.CustomCodeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/custom-codes")
class CustomCodeRestController(
    private val customCodeService: CustomCodeService
) {

    /**
     * [CustomCodeSearchCondition]를 받아서 사용자 정의 코드 리스트를 [List<CustomCodeListDto>] 반환한다.
     *
     */
    @GetMapping("")
    fun getCustomCodeList(
        customCodeSearchCondition: CustomCodeSearchCondition
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customCodeService.getCustomCodeList(customCodeSearchCondition))
    }

    /**
     * 커스텀 코드 목록 조회.
     */
    @GetMapping("/{customCodeId}")
    fun getCustomCodes(@PathVariable customCodeId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customCodeService.getCustomCodeData(customCodeId))
    }

    /**
     * 사용자 정의 코드 등록.
     *
     * @param customCodeDto
     * @return String
     */
    @PostMapping("")
    fun createCustomCode(@RequestBody customCodeDto: CustomCodeDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customCodeService.saveCustomCode(customCodeDto))
    }

    /**
     * 사용자 정의 코드 수정.
     *
     * @param customCodeDto
     * @return String
     */
    @PutMapping("")
    fun updateCustomCode(@RequestBody customCodeDto: CustomCodeDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customCodeService.saveCustomCode(customCodeDto))
    }

    /**
     * 사용자 정의 코드 삭제.
     *
     * @param customCodeId
     * @return String
     */
    @DeleteMapping("/{customCodeId}")
    fun deleteCustomCode(@PathVariable customCodeId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customCodeService.deleteCustomCode(customCodeId))
    }
}
