/**
 * Form RestAPI 처리용 컨트롤러
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.form.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.form.service.FormService
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
@RequestMapping("/rest/forms")
class FormRestController(private val formService: FormService) {

    /**
     * 문서양식 일반정보 조회
     *
     * @param formId
     */
    @GetMapping("/{formId}")
    fun getForm(@PathVariable formId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(formService.getForm(formId))
    }

    /**
     * 문서양식 전체 데이터 조회.
     *
     * @param formId
     */
    @GetMapping("/{formId}/data")
    fun getFormData(@PathVariable formId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(formService.getFormData(formId))
    }

    /**
     * 신규 문서양식 등록, 새이름으로 저장
     *
     * @param saveType
     * @param formData
     */
    @PostMapping("")
    fun createForm(
        @RequestParam(value = "saveType", defaultValue = "") saveType: String,
        @RequestBody formData: RestTemplateFormDataDto
    ): ResponseEntity<ZResponse> {
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return when (saveType) {
            WorkflowConstants.FormSaveType.SAVE_AS.code ->
                ZAliceResponse.response(formService.saveAsForm(formData))
            else -> ZAliceResponse.response(formService.createForm(formData))
        }
    }

    /**
     * 문서양식 일반정보 저장.
     *
     * @param restTemplateFormDto
     * @param formId
     */
    @PutMapping("/{formId}")
    fun saveForm(
        @RequestBody restTemplateFormDto: RestTemplateFormDto,
        @PathVariable formId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(formService.saveForm(formId, restTemplateFormDto))
    }

    /**
     * 문서양식 전체 데이터 저장.
     *
     * @param formData
     * @param formId
     */
    @PutMapping("/{formId}/data")
    fun saveFormData(
        @RequestBody formData: RestTemplateFormDataDto,
        @PathVariable formId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(formService.saveFormData(formId, formData))
    }

    /**
     * 문서 삭제.
     *
     * @param formId
     */
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(formService.deleteForm(formId))
    }
}
