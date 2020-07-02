package co.brainz.itsm.image.controller

import co.brainz.framework.fileTransaction.service.AliceFileService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/images")
class ImageController(private val fileService: AliceFileService) {

    private val imageListPage: String = "image/imageList"

    @GetMapping("")
    fun getImages(request: HttpServletRequest, model: Model): String {
        model.addAttribute("imageList", fileService.getImageFileList())
        return imageListPage
    }
}
