package co.brainz.sample.file.controller

import co.brainz.framework.fileTransaction.controller.FileController
import co.brainz.framework.fileTransaction.service.FileService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class FileUploadTestSample {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    @Autowired
    lateinit var fileService: FileService

    @PostMapping("/fileSubmit")
    fun formSubmit(@ModelAttribute fileSubmitDto: FileSubmitTestDtoSample): ModelAndView {

        logger.debug("id {}:name: {}", fileSubmitDto.id, fileSubmitDto.name)
        for (seq in fileSubmitDto.fileSeq.orEmpty()) {
            logger.debug("{}:{} ", seq)
        }

        fileService.upload(fileSubmitDto.fileSeq)

        var mv = ModelAndView("jsonView")
        mv.addObject("msg", "filesubmit")

        return mv
    }
}