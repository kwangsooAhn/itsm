package co.brainz.itsm.form.controller

import co.brainz.framework.fileTransaction.service.AliceFileService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * ### 폼(문서양식) 관련 화면 호출 처리용 클래스.
 *
 * FormController에서 처리하는 모든 호출은 View 혹은 View + Data를 반환한다.
 * 즉, View가 포함되는 호출에 대한 처리이며, 순수하게 JSON 데이터만 반환하는 경우는 FormRestController에서 담당한다.
 *
 * @author Woo Dajung
 */
@Controller
@RequestMapping("/forms")
class FormController(private val fileService: AliceFileService) {

    private val formDesignerEditPage: String = "form/formDesignerEdit"
    private val formEditPreviewPage: String = "form/formEditPreview"
    private val imageUploadPopupPage: String = "form/imagePopup"
    private val formImportPage: String = "form/formImport"

    /**
     * 폼 디자이너 편집 화면.
     */
    @GetMapping("/{formId}/edit")
    fun getFormDesignerEdit(@PathVariable formId: String, model: Model): String {
        model.addAttribute("formId", formId)
        return formDesignerEditPage
    }

    /**
     * 폼 디자이너 미리보기 화면.
     */
    @GetMapping("/{formId}/preview")
    fun getFormEditPreview(@PathVariable formId: String, model: Model): String {
        model.addAttribute("formId", formId)
        return formEditPreviewPage
    }

    /**
     * 폼 디자이너 상세화면.
     */
    @GetMapping("/{formId}/view")
    fun getFormDesignerView(@PathVariable formId: String, model: Model): String {
        model.addAttribute("formId", formId)
        model.addAttribute("isView", true)
        return formDesignerEditPage
    }

    /**
     * 이미지 컴포넌트 팝업 화면.
     */
    @GetMapping("/imageUpload/{componentId}/view")
    fun getImageUploadPopup(@PathVariable componentId: String, model: Model): String {
        model.addAttribute("componentId", componentId)
        model.addAttribute("imageList", fileService.getImageFileList())
        return imageUploadPopupPage
    }

    /**
     * 폼 Import 화면.
     */
    @GetMapping("/import")
    fun getFormImport(request: HttpServletRequest, model: Model): String {
        return formImportPage
    }
}
