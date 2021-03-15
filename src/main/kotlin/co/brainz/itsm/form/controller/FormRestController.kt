/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormService
import co.brainz.workflow.provider.dto.RestTemplateFormComponentListDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/form")
class FormRestController(private val formService: FormService) {

    /**
     * 문서양식 불러오기.
     */
    @GetMapping("/{formId}/data")
    fun getFormData(@PathVariable formId: String): RestTemplateFormComponentListDto {
        return formService.getFormData(formId)
    }

    /**
     * 문서양식 저장.
     */
    @PutMapping("/{formId}/data")
    fun saveFormData(@RequestBody formData: String, @PathVariable formId: String): Boolean {
        return formService.saveFormData(formId, formData)
    }

    /**
     * 문서 삭제.
     */
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String): Boolean {
        return formService.deleteForm(formId)
    }
}
