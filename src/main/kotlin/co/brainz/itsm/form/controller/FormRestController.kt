package co.brainz.itsm.form.controller

import co.brainz.itsm.form.dto.FormDto
import co.brainz.itsm.form.service.FormService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * ### 폼(문서양식) 관련 데이터 조회 처리용 클래스.
 *
 * REST API형식의 호출을 처리한다.
 *
 * @author Woo Dajung
 * @see co.brainz.itsm.form.controller.FormController
 */
@RestController
@RequestMapping("/rest/forms")
class FormRestController(private val formService: FormService) {

    /**
     * 폼 데이터 조회.
     */
    @GetMapping("")
    fun getForms(request: HttpServletRequest): MutableList<Map<String, String?>> {
        return formService.findFormList(request.getParameter("search"))
    }

    /**
     * 폼 신규 기본 정보 등록.
     */
    @PostMapping("")
    fun insertForm(@RequestBody formDto: FormDto): String {
        return formService.insertForm(formDto)
    }

    /**
     * 폼 1건 데이터 조회.
     */
    @GetMapping("/{formId}")
    fun getForm(@PathVariable formId: String) {
        //TODO Form 1건 데이터 조회
    }

    /**
     * 폼 1건 데이터 수정.
     */
    @PutMapping("/{formId}")
    fun updateForm(@PathVariable formId: String) {
        //TODO Form 1건 데이터 수정
    }

    /**
     * 폼 1건 데이터 삭제.
     */
    @DeleteMapping("/{formId}")
    fun deleteForm(@PathVariable formId: String) {
        formService.deleteFrom(formId)
    }
}