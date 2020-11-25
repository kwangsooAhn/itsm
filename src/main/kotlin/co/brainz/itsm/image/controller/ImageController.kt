package co.brainz.itsm.image.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/images")
class ImageController() {

    private val imageListPage: String = "image/imageList"

    @GetMapping("")
    fun getImages(): String {
        return imageListPage
    }
}
