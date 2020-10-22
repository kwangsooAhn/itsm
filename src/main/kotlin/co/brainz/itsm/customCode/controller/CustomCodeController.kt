/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.customCode.constants.CustomCodeConstants
import co.brainz.itsm.customCode.dto.CustomCodeSearchDto
import co.brainz.itsm.customCode.service.CustomCodeService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/custom-codes")
class CustomCodeController(
    private val codeService: CodeService,
    private val customCodeService: CustomCodeService
) {

    private val customCodeSearchPage: String = "custom-code/customCodeSearch"
    private val customCodeListPage: String = "custom-code/customCodeList"
    private val customCodeEditPage: String = "custom-code/customCodeEdit"
    private val customCodeViewPage: String = "custom-code/customCodeView"
    private val documentCustomCodePage: String = "custom-code/customCodePopup"

    /**
     * 사용자 정의 코드 리스트 호출 화면.
     *
     * @return String
     */
    @GetMapping("/search")
    fun getCustomCodeSearch(model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(CustomCodeConstants.CUSTOM_CODE_TYPE_P_CODE))
        return customCodeSearchPage
    }

    /**
     * [customCodeSearchDto, model: Model]를 받아서 사용자 정의 코드 리스트 화면[String]을 반환한다.
     *
     */
    @GetMapping("/list")
    fun getCustomCodeList(customCodeSearchDto: CustomCodeSearchDto, model: Model): String {
        val result = customCodeService.getCustomCodeList(customCodeSearchDto)
        model.addAttribute("typeList", codeService.selectCodeByParent(CustomCodeConstants.CUSTOM_CODE_TYPE_P_CODE))
        model.addAttribute("customCodeList", result)
        model.addAttribute("customCodeCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return customCodeListPage
    }

    /**
     * 사용자 정의 코드 신규 등록 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/new")
    fun getCustomCodeNew(model: Model): String {
        model.addAttribute("customCodeTableList", customCodeService.getCustomCodeTableList())
        model.addAttribute("customCodeColumnList", customCodeService.getCustomCodeColumnList())
        return customCodeEditPage
    }

    /**
     * 사용자 정의 코드 상세 정보 화면.
     *
     * @param customCodeId
     * @param model
     * @return String
     */
    @GetMapping("/{customCodeId}/view")
    fun getCustomCodeView(@PathVariable customCodeId: String, model: Model): String {
        val customCodeDto = customCodeService.getCustomCode(customCodeId)
        model.addAttribute("customCode", customCodeDto)
        if (customCodeDto.type == CustomCodeConstants.Type.TABLE.code) {
            model.addAttribute(
                "customCodeColumnList",
                customCodeService.getCustomCodeColumnList(customCodeDto.targetTable)
            )
        }
        return customCodeViewPage
    }

    /**
     * 사용자 정의 코드 수정 화면.
     *
     * @param customCodeId
     * @param model
     * @return String
     */
    @GetMapping("/{customCodeId}/edit")
    fun getCustomCodeEdit(@PathVariable customCodeId: String, model: Model): String {
        model.addAttribute("customCode", customCodeService.getCustomCode(customCodeId))
        model.addAttribute("customCodeTableList", customCodeService.getCustomCodeTableList())
        model.addAttribute("customCodeColumnList", customCodeService.getCustomCodeColumnList())
        return customCodeEditPage
    }

    /**
     * 사용자 정의 코드 데이터 조회 팝업 화면.
     */
    @GetMapping("/{customCodeId}/search")
    fun getCustomCodeData(@PathVariable customCodeId: String, model: Model, request: HttpServletRequest): String {
        model.addAttribute("customCodeDataList", customCodeService.getCustomCodeData(customCodeId))
        return documentCustomCodePage
    }
}
