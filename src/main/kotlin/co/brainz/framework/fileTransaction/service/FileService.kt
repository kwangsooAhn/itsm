package co.brainz.framework.fileTransaction.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.fileTransaction.entity.FileLocEntity
import co.brainz.framework.fileTransaction.repository.FileLocRepository
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/**
 * 파일 서비스 클래스
 */
@Service
class FileService(private val fileLocRepository: FileLocRepository, private val environment: Environment) {

    companion object {
        private val logger = LoggerFactory.getLogger(FileService::class.java)
    }


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
    private fun getDir(rootDir: String, fileName: String?): Path {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyyMMdd")

        if (basePath == "") {
            this.basePath = environment.getProperty("catalina.base").toString()
        }

        var dir: Path = Paths.get(basePath + File.separator + rootDir + File.separator + df.format(cal.time))
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return Paths.get(dir.toString() + File.separator + fileName)
    }

    @Transactional
    fun uploadTemp(multipartFile: MultipartFile): FileLocEntity {
        val fileLocEntity: FileLocEntity
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val fileName = getRandomFilename()
        val tempPath = getDir("temp", fileName)
        val filePath = getDir("uploadRoot", fileName)

        if (Files.notExists(tempPath.parent)) {
            throw AliceException(AliceErrorConstants.ERR, "Unknown file path. [" + tempPath.toFile() + "]")
        }

        multipartFile.transferTo(tempPath.toFile())

        fileLocEntity = FileLocEntity(0, aliceUserDto.userKey, false, filePath.parent.toString(), fileName, multipartFile.originalFilename,
                multipartFile.size, 0, aliceUserDto.userKey, aliceUserDto.userKey, LocalDateTime.now(), LocalDateTime.now())
        logger.debug("{}", fileLocEntity)
        fileLocRepository.save(fileLocEntity)
        logger.debug(">> 임시업로드파일 {}", tempPath.toAbsolutePath())

        return fileLocEntity
    }

    @Transactional
    fun upload(fileSeq: List<Long>?) {
        for (seq in fileSeq.orEmpty()) {
            var fileLocEntity = fileLocRepository.getOne(seq)

            var filePath = Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName)
            val tempPath = getDir("temp", fileLocEntity.randomName)
            Files.move(tempPath, filePath, StandardCopyOption.REPLACE_EXISTING)

            logger.debug(">> 임시업로드파일 {} 을 사용할 위치로 이동 {}", tempPath.toAbsolutePath(), filePath.toAbsolutePath())
            logger.debug(">> 여기에 업로드 파일 {}({})", fileLocEntity.uploadedLocation + File.separator + fileLocEntity.originName, fileLocEntity.randomName)

            fileLocEntity.uploaded = true
            fileLocRepository.save(fileLocEntity)
        }
    }

    fun getList(task: String): MutableList<FileLocEntity> {
        return fileLocRepository.findAll()
    }

    @Transactional
    fun delete(seq: Long) {
        val fileLocEntity = fileLocRepository.getOne(seq)
        Files.delete(Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName))
        logger.debug(">> 삭제한 파일 정보 {}({})", fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName, fileLocEntity.originName)
        fileLocRepository.deleteById(fileLocEntity.fileSeq)
    }

    fun download(seq: Long): ResponseEntity<InputStreamResource> {
        var fileLocEntity = fileLocRepository.getOne(seq)
        var resource = FileSystemResource(Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName))
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Tika().detect(resource.path)))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileLocEntity.originName + "\"")
                    .body(InputStreamResource(resource.inputStream));
        } else {
            logger.error("File not fount: {}", fileLocEntity.uploadedLocation + File.separator + fileLocEntity.originName)
            throw AliceException(AliceErrorConstants.ERR, "File not found: " + fileLocEntity.originName)
        }
    }
}
