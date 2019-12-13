package co.brainz.framework.fileTransaction.controller

import co.brainz.framework.fileTransaction.service.FileService
import co.brainz.framework.fileTransaction.service.impl.FileServiceImpl
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import java.io.FileNotFoundException

/**
 * 파일 관련 컨트롤 클래스
 */
@RestController
class FileController {

    companion object {
        private val logger = LoggerFactory.getLogger(FileController::class.java)
    }

    @Autowired
    lateinit var fileService: FileService


    /**
     * 파일 업로드.
     * 파일 추가시 임시폴더에 물리적으로 저장한다.
     */
    @PostMapping("/fileupload")
    @Throws(Exception::class)
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
    @Throws(Exception::class)
    fun downloadFile(@RequestParam seq: Long, @RequestParam test: String): ResponseEntity<InputStreamResource> {
        return fileService.download(test)
        //return fileService.download(seq)
    }

    /**
     * 파일 목록 가져오기.
     *
     * @param task 파일 목록을 가져오기 위한 값.
     */
    @GetMapping("/filelist")
    @Throws(Exception::class)
    fun getFileList(@RequestParam task: String): ModelAndView {

        val files = fileService.getList(task)
        var mv = ModelAndView("jsonView")
        mv.addObject("files", files)

        return mv

    }

    /**
     * 파일 삭제.
     *
     * @param seq 파일 고유시퀀스번호
     */
    @DeleteMapping("/filedel")
    @Throws(Exception::class)
    fun delete(@RequestParam seq: Long, @RequestParam testSeq: String): ModelAndView {
        logger.debug(">>>>>>>>>>>>>> {} >>>>> {} ", seq, testSeq)
        //fileService.delete(seq)
        fileService.delete(testSeq)
        var mv = ModelAndView("jsonView")
        mv.addObject("msg", "Delete...")
        return mv
    }


    @ExceptionHandler(Exception::class)
    fun exceptionHandle(e: Exception): ResponseEntity<MutableMap<String, Any>> {
        logger.error("{}", e)
        val info: MutableMap<String, Any> = mutableMapOf()
        info["msg"] = e.toString()
        val headers: HttpHeaders = HttpHeaders()
        headers.add("Content-Type", "application/json; charset=utf-8")
        return ResponseEntity(info, headers, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}