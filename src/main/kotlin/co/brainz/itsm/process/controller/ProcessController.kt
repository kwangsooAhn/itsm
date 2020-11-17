package co.brainz.itsm.process.controller

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.process.service.ProcessService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/processes")
class ProcessController(
    private val processService: ProcessService,
    private val fileService: AliceFileService
) {

    private val processDesignerEditPage: String = "process/processDesignerEdit"
    private val processImportPage: String = "process/processImport"
    private val processStatusPage: String = "process/processStatus"
    private val processAttachFileViewPage: String = "process/processAttachFileView"

    /**
     * 프로세스 Import 화면.
     */
    @GetMapping("/import")
    fun getProcessImport(request: HttpServletRequest, model: Model): String {
        return processImportPage
    }

    /**
     * 프로세스 상태 화면.
     */
    @GetMapping("/{instanceId}/status")
    fun getProcessStatus(@PathVariable instanceId: String, model: Model): String {
        val processStatusDto = processService.getProcessStatus(instanceId)
        model.addAttribute("processStatus", processStatusDto)
        return processStatusPage
    }

    /**
     * 이미지 컴포넌트 팝업 화면.
     */
    @GetMapping("/attachFile/view")
    fun getProcessAttachFileView(
        @RequestParam(value = "callback", defaultValue = "") callback: String,
        model: Model
    ): String {
        model.addAttribute("callback", callback)
        model.addAttribute("imageList", fileService.getImageFileList(AliceConstants.FileType.IMAGE.code))
        return processAttachFileViewPage
    }
}
