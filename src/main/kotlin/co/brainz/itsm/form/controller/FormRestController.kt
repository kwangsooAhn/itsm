package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.provider.dto.FormDto
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/forms")
class FormRestController(private val formService: FormService) {

    @PostMapping("")
    fun createForm(@RequestBody formDto: FormDto): String {
        return formService.createForm(formDto)
    }

    /**
     * 문서양식 불러오기.
     */
    @GetMapping("/data/{formId}")
    fun getFormData(@PathVariable formId: String): String {
        return formService.findForm(formId)
    }

    /**
     * 문서양식 저장.
     */
    @PutMapping("/data")
    fun saveFormData(@RequestBody formData: String): Boolean {
        return formService.saveFormData(formData)
    }

    /**
     * 문서 삭제.
     */
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String): Boolean {
        return formService.deleteForm(formId)
    }
}
