package co.brainz.framework.fileTransaction.controller

import co.brainz.framework.fileTransaction.entity.FileLocEntity
import co.brainz.framework.fileTransaction.service.FileService
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView

/**
 * 파일 관련 컨트롤 클래스
 */
@RestController
class FileController(private val fileService: FileService) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    /**
     * 파일 업로드.
     * 파일 추가시 임시폴더에 물리적으로 저장한다.
     */
    @PostMapping("/fileupload")
    fun uploadFile(@RequestPart("file") multipartFile: MultipartFile): ResponseEntity<Map<String, Any>> {
        val response: ResponseEntity<Map<String, Any>>
        val map: MutableMap<String, Any> = mutableMapOf()

        map["file"] = fileService.uploadTemp(multipartFile)

        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json; charset=utf-8")
        response = ResponseEntity(map, headers, HttpStatus.OK)

        return response
    }

    /**
     * 파일 다운로드.
     * 저장된 파일을 다운로드 한다.
     */
    @GetMapping("/filedownload")
    fun downloadFile(@RequestParam seq: Long, @RequestParam test: String): ResponseEntity<InputStreamResource> {
        return fileService.download(seq)
    }

    /**
     * 파일 목록 가져오기.
     *
     * @param task 파일 목록을 가져오기 위한 값.
     */
    @GetMapping("/filelist")
    fun getFileList(@RequestParam task: String): MutableList<FileLocEntity> {
        return fileService.getList(task)
    }

    /**
     * 파일 삭제.
     *
     * @param seq 파일 고유시퀀스번호
     */
    @DeleteMapping("/filedel")
    fun delete(@RequestParam seq: Long): ModelAndView {
        logger.debug(">>>>>>>>>>>>>> {} >>>>> {} ", seq)
        fileService.delete(seq)
        var mv = ModelAndView("jsonView")
        mv.addObject("msg", "Delete...")
        return mv
    }
}