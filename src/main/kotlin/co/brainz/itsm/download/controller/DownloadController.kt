package co.brainz.itsm.download.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.download.constants.DownloadConstants
import co.brainz.itsm.download.dto.DownloadSearchDto
import co.brainz.itsm.download.service.DownloadService
import java.time.LocalDateTime
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/downloads")
class DownloadController(
    private val codeService: CodeService,
    private val downloadService: DownloadService
) {

    private val downloadSearchPage: String = "download/downloadSearch"
    private val downloadListPage: String = "download/downloadList"
    private val downloadEditPage: String = "download/downloadEdit"
    private val downloadViewPage: String = "download/downloadView"

    /**
     * 자료실 호출 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/search")
    fun getDownloadSearch(model: Model): String {
        model.addAttribute("categoryList", codeService.selectCodeByParent(DownloadConstants.DOWNLOAD_CATEGORY_P_CODE))
        model.addAttribute("fromDt", LocalDateTime.now().minusMonths(1))
        model.addAttribute("toDt", LocalDateTime.now())
        return downloadSearchPage
    }

    /**
     * 자료실 리스트 화면.
     *
     * @param downloadSearchDto
     * @param model
     * @return String
     */
    @GetMapping("/list")
    fun getDownloadList(downloadSearchDto: DownloadSearchDto, model: Model): String {
        model.addAttribute("downloadList", downloadService.getDownloadList(downloadSearchDto))
        return downloadListPage
    }

    /**
     * 자료실 신규 등록 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/new")
    fun getDownloadNew(model: Model): String {
        model.addAttribute("categoryList", codeService.selectCodeByParent(DownloadConstants.DOWNLOAD_CATEGORY_P_CODE))
        return downloadEditPage
    }

    /**
     * 자료실 상세 조회 화면.
     *
     * @param downloadId
     * @param model
     * @return String
     */
    @GetMapping("/{downloadId}/view")
    fun getDownloadView(@PathVariable downloadId: String, model: Model): String {
        model.addAttribute("download", downloadService.getDownload(downloadId, "view"))
        return downloadViewPage
    }

    /**
     * 자료실 편집 화면.
     *
     * @param downloadId
     * @param model
     * @return String
     */
    @GetMapping("/{downloadId}/edit")
    fun getDownloadEdit(@PathVariable downloadId: String, model: Model): String {
        model.addAttribute("download", downloadService.getDownload(downloadId, "edit"))
        model.addAttribute("categoryList", codeService.selectCodeByParent(DownloadConstants.DOWNLOAD_CATEGORY_P_CODE))
        return downloadEditPage
    }
}
