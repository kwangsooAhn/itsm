package co.brainz.sample.file.controller

import co.brainz.framework.fileTransaction.service.AliceFileService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class FileUploadTestSample(private val aliceFileService: AliceFileService) {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val fileUploaderPage: String = "sample/fileUploader"

    @GetMapping("/files")
    fun getFile(): String {
        return fileUploaderPage
    }

    @PostMapping("/fileSubmit")
    @ResponseBody
    fun formSubmit(fileSubmitDto: FileSubmitTestDtoSample): Boolean {
        logger.debug("id {}:name: {}", fileSubmitDto.id, fileSubmitDto.name)
        for (seq in fileSubmitDto.fileSeq.orEmpty()) {
            logger.debug("{}:{} ", seq)
        }
        return true
    }
}
