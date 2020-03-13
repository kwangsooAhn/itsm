package co.brainz.itsm.process.controller

import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.process.service.ProcessService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("/processes")
class ProcessController(private val processService: ProcessService,
                        private val formService: FormService) {

    private val processSearchPage: String = "process/processSearch"
    private val processListPage: String = "process/processList"
    private val processEditPage: String = "process/processEdit"
    private val processDesignerEditPage: String = "process/processDesignerEdit"

    /**
     * 프로세스 리스트 검색 호출 화면.
     */
    @GetMapping("/search")
    fun getProcessSearch(request: HttpServletRequest, model: Model): String {
        return processSearchPage
    }

    /**
     * 프로세스 리스트 화면.
     */
    @GetMapping("/list")
    fun getProcessList(request: HttpServletRequest, model: Model): String {
        model.addAttribute("processList", processService.getProcesses(request.getParameter("search")))
        return processListPage
    }

    /**
     * 프로세스 기본 정보 등록 화면.
     */
    @GetMapping("/new")
    fun getProcessNew(request: HttpServletRequest, model: Model): String {
        //TODO 템플릿 정보 가져오기

        return processEditPage
    }

    /**
     * 프로세스 디자이너 편집 화면.
     */
    @GetMapping("/{processId}/edit")
    fun getProcessDesignerEdit(@PathVariable processId: String, model: Model): String {
        model.addAttribute("processId", processId)
        return processDesignerEditPage
    }
}
