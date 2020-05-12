package co.brainz.itsm.customCode.controller

import co.brainz.itsm.customCode.dto.CustomCodeDataDto
import co.brainz.itsm.customCode.dto.CustomCodeDto
import co.brainz.itsm.customCode.service.CustomCodeService
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
class CustomCodeRestController(private val customCodeService: CustomCodeService) {

    /**
     * 사용자 정의 코드 리스트.
     *
     * @return List<CustomCodeDto>
     */
    @GetMapping("")
    fun getCustomCodeList(): List<CustomCodeDto> {
        return customCodeService.getCustomCodeList()
    }

    /**
     * 커스텀 코드 목록 조회.
     */
    @GetMapping("/{customCodeId}")
    fun getCustomCodes(@PathVariable customCodeId: String): List<CustomCodeDataDto> {
        return customCodeService.getCustomCodeData(customCodeId)
    }

    /**
     * 사용자 정의 코드 등록.
     *
     * @param customCodeDto
     * @return String
     */
    @PostMapping("")
    fun createCustomCode(@RequestBody customCodeDto: CustomCodeDto): String {
        return customCodeService.saveCustomCode(customCodeDto)
    }

    /**
     * 사용자 정의 코드 수정.
     *
     * @param customCodeDto
     * @return String
     */
    @PutMapping("")
    fun updateCustomCode(@RequestBody customCodeDto: CustomCodeDto): String {
        return customCodeService.saveCustomCode(customCodeDto)
    }

    /**
     * 사용자 정의 코드 삭제.
     *
     * @param customCodeId
     * @return String
     */
    @DeleteMapping("/{customCodeId}")
    fun deleteCustomCode(@PathVariable customCodeId: String): String {
        return customCodeService.deleteCustomCode(customCodeId)
    }
}
