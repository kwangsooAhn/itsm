package co.brainz.itsm.form.controller

import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.provider.dto.FormDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/forms")
class FormRestController(private val formService: FormService) {

    /**
     * 문서양식 불러오기.
     */
    @GetMapping("/data/{formId}")
    fun getFormData(@PathVariable formId: String): String {
        return formService.findFormComponents(formId)
    }

    /**
     * 문서양식 저장.
     */
    @PostMapping("/data")
    fun saveFormData(@RequestBody formDto: FormDto): String {
        return formService.insertForm(formDto)
    }
}
