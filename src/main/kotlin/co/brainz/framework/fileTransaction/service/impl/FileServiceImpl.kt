package co.brainz.framework.fileTransaction.service.impl

import co.brainz.framework.fileTransaction.entity.FileLocEntity
import co.brainz.framework.fileTransaction.repository.FileLocRepository
import co.brainz.framework.fileTransaction.service.FileService
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.*

/**
 * 파일 서비스 클래스
 */
@Service
open class FileServiceImpl: FileService {

    companion object {
        private val logger = LoggerFactory.getLogger(FileServiceImpl::class.java)
    }

    @Autowired
    lateinit var fileLocRepository: FileLocRepository

    @Value("\${file.upload.dir}")
    lateinit var basePath: String

    /**
     * 파일명으로 사용할 값 리턴 (난수화)
     */
    private fun getRandomFilename(): String {
        var charCode: Char
        var fileName = ""
        for (i in 0 until 12) {
            charCode = (Math.random() * 26 + 65).toChar()
            fileName += charCode
        }
        return fileName

    }

    /**
     * 업로드 대상 파일 경로 구하기
     *
     * @param rootDir 업로드할 경로
     * @param fileName 업로드할 파일명
     */
    private fun getDir(rootDir:String, fileName: String?): Path {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyyMMdd")
        var dir: Path = Paths.get(basePath + File.separator + rootDir + File.separator + df.format(cal.time))
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return Paths.get(dir.toString() + File.separator + fileName)
    }

    @Transactional(rollbackFor = [Exception::class])
    @Throws(Exception::class)
    override fun uploadTemp(multipartFile: MultipartFile): FileLocEntity {
        val fileLocEntity: FileLocEntity

        try {
            var sessionid = "beomho"
            val fileName = getRandomFilename()
            val tempPath = getDir("temp", fileName)
            val filePath = getDir("uploadRoot", fileName)
            multipartFile.transferTo(tempPath.toFile())

            fileLocEntity = FileLocEntity(0, "Default", "Test", fileName, multipartFile.originalFilename,
                    multipartFile.size, filePath.parent.toString(), sessionid, false, 0)
            logger.debug("{}", fileLocEntity)
            //fileLocRepository.save(fileLocEntity)
            logger.debug(">> 임시업로드파일 {}", tempPath.toAbsolutePath())

        } catch (e: Exception) {
            throw e
        }
        return fileLocEntity
    }

    @Transactional(rollbackFor = [Exception::class])
    @Throws(Exception::class)
    override fun upload(fileSeq: List<Long>?) {

        // test data
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyyMMdd")

        var temp = Paths.get("D:\\temp\\" + df.format(cal.time))
        var real = Paths.get("D:\\uploadRoot\\" + df.format(cal.time))

        temp.toFile().listFiles().forEach { f -> logger.debug("{} -> {}", f.toPath(), real) }
        temp.toFile().listFiles().forEach { f -> Files.move(f.toPath(), Paths.get("D:\\uploadRoot\\" + df.format(cal.time) + File.separator + f.name), StandardCopyOption.REPLACE_EXISTING) }



//        for (seq in fileSeq.orEmpty()) {
//            var fileLocEntity = fileLocRepository.getOne(seq)
//
//            var filePath = Paths.get(fileLocEntity.location + File.separator + fileLocEntity.name)
//            val tempPath = getDir("temp", fileLocEntity.name)
//            Files.move(tempPath, filePath, StandardCopyOption.REPLACE_EXISTING)
//
//            logger.debug(">> 임시업로드파일 {} 을 사용할 위치로 이동 {}", tempPath.toAbsolutePath(), filePath.toAbsolutePath())
//            logger.debug(">> 여기에 업로드 파일 {}({})", fileLocEntity.location + File.separator + fileLocEntity.originName, fileLocEntity.name)
//            //fileLocRepository.updateUploadFlag(fileLocEntity.fileSeq)
//        }
    }


    override fun getList(task: String): MutableList<FileLocEntity> {
        // test data
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyyMMdd")
        var real = Paths.get("D:\\uploadRoot\\" + df.format(cal.time))


        getDir("temp", "")
        getDir("uploadRoot", "")

        var findAllByTask:MutableList<FileLocEntity> = mutableListOf()
        real.toFile().listFiles().forEach {
            f -> logger.debug(">> 현재 파일들 {}", f.name)
            findAllByTask.add(FileLocEntity(11, "", "", f.name, f.name, f.length(), "", "", false, 0))
        }
        return findAllByTask
        //return fileLocRepository.findAllByTask(task)
    }


    @Transactional(rollbackFor = [Exception::class])
    @Throws(Exception::class)
    //override fun delete(seq: Long) {
    override fun delete(seq: String) {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyyMMdd")
        Files.delete(Paths.get("D:\\uploadRoot\\" + df.format(cal.time) + File.separator + seq))
        logger.debug(">> 삭제 {}", "D:\\uploadRoot\\" + df.format(cal.time) + File.separator + seq)


//        val fileLocEntity = fileLocRepository.getOne(seq)
//        Files.delete(Paths.get(fileLocEntity.location + File.separator + fileLocEntity.name))
//        logger.debug(">> 삭제한 파일 정보 {}({})", fileLocEntity.location + File.separator + fileLocEntity.name, fileLocEntity.originName)
//        fileLocRepository.deleteById(fileLocEntity.fileSeq)
    }

    @Throws(Exception::class)
    override fun download(seq: Long): ResponseEntity<InputStreamResource> {

        var fileLocEntity = fileLocRepository.getOne(seq)
        var resource = FileSystemResource(Paths.get(fileLocEntity.location + File.separator + fileLocEntity.name))
        if (resource.exists()) {

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Tika().detect(resource.path)))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileLocEntity.originName + "\"")
                    .body(InputStreamResource(resource.inputStream));
        } else {
            logger.error("File not fount: {}", fileLocEntity.location + File.separator + fileLocEntity.originName)
            throw FileNotFoundException("File not found: " + fileLocEntity.originName)
        }
    }

    // TODO 삭제해야됨.
    @Throws(Exception::class)
    override fun download(seq: String): ResponseEntity<InputStreamResource> {
        var resource = FileSystemResource(getDir("uploadRoot", seq).toFile())
        logger.debug(">> 다운로드할 파일: {}, {}", resource.path, resource.filename)



        if (resource.exists()) {
            logger.debug(">> media type: {}", MediaType.parseMediaType(Tika().detect(resource.path)));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Tika().detect(resource.path)))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"파일다운로드임.txt\"")
                    .body(InputStreamResource(resource.inputStream));
        } else {
            logger.error("File not fount: ")
            throw FileNotFoundException("File not found: ")
        }
    }

    @ExceptionHandler(Exception::class)
    fun exceptionHandle(e: Exception): ResponseEntity<MutableMap<String, Any>> {
        logger.error("{}", e)
        throw e;
    }

}