package co.brainz.framework.fileTransaction.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.constants.UserConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.fileTransaction.dto.FileDto
import co.brainz.framework.fileTransaction.dto.FileLocDto
import co.brainz.framework.fileTransaction.dto.FileOwnMapDto
import co.brainz.framework.fileTransaction.entity.FileLocEntity
import co.brainz.framework.fileTransaction.entity.FileOwnMapEntity
import co.brainz.framework.fileTransaction.repository.FileLocRepository
import co.brainz.framework.fileTransaction.repository.FileOwnMapRepository
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
import java.util.Calendar


/**
 * 파일 서비스 클래스
 */
@Service
class FileService(
    private val fileLocRepository: FileLocRepository,
    private val fileOwnMapRepository: FileOwnMapRepository,
    private val environment: Environment
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

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

    /**
     * 파일을 임시로 업로드한다.
     * temp 경로에 파일을 임시로 업로드하고 파일관리테이블에 uploaded 상태를 false로 저장한다.
     */
    @Transactional
    fun uploadTemp(multipartFile: MultipartFile): FileLocEntity {
        lateinit var fileLocEntity: FileLocEntity
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val fileName = getRandomFilename()
        val tempPath = getDir("temp", fileName)
        val filePath = getDir("uploadRoot", fileName)

        if (Files.notExists(tempPath.parent)) {
            throw AliceException(AliceErrorConstants.ERR, "Unknown file path. [" + tempPath.toFile() + "]")
        }

        try {
            for (it in UserConstants.InAcceptableExtension.values()) {
                val extension = it.toString()
                if (File(multipartFile.originalFilename).extension.toUpperCase() == extension) {
                    throw AliceException(AliceErrorConstants.ERR_00004, "The file extension is not allowed.")
                }
            }

            multipartFile.transferTo(tempPath.toFile())

            fileLocEntity = FileLocEntity(
                    0,
                    aliceUserDto.userKey,
                    false,
                    filePath.parent.toString(),
                    fileName,
                    multipartFile.originalFilename,
                    multipartFile.size,
                    0
            )
            logger.debug("{}", fileLocEntity)
            fileLocRepository.save(fileLocEntity)
            logger.debug(">> 임시업로드파일 {}", tempPath.toAbsolutePath())

        } catch (e: AliceException) {
            e.printStackTrace()
        }
        return fileLocEntity
    }

    /**
     * 임시 업로드된 파일을 업로드한다.
     * 임시 업로드 경로에 업로드된 파일을 업로드하고 파일관리테이블에 uploaded 상태를 true 변경하여 조회가 가능하도록 한다.
     */
    fun upload(fileDto: FileDto) {
        for (fileSeq in fileDto.fileSeq.orEmpty()) {
            val fileLocEntity = fileLocRepository.getOne(fileSeq)
            val filePath = Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName)
            val tempPath = getDir("temp", fileLocEntity.randomName)
            Files.move(tempPath, filePath, StandardCopyOption.REPLACE_EXISTING)
            logger.debug(">> 임시업로드파일 {} 을 사용할 위치로 이동 {}", tempPath.toAbsolutePath(), filePath.toAbsolutePath())
            logger.debug(
                ">> 여기에 업로드 파일 {}({})",
                fileLocEntity.uploadedLocation + File.separator + fileLocEntity.originName,
                fileLocEntity.randomName
            )
            fileLocEntity.uploaded = true
            try {
                val fileOwnMapEntity = FileOwnMapEntity(fileDto.ownId, fileLocEntity)
                fileOwnMapRepository.save(fileOwnMapEntity)
            } catch (e: Exception) {
                logger.error("{}", e.message)
                Files.move(filePath, tempPath, StandardCopyOption.REPLACE_EXISTING)
                throw AliceException(AliceErrorConstants.ERR, e.message)
            }
        }
    }

    /**
     * 파일 목록을 가져온다.
     */
    fun getList(ownId: String): List<FileOwnMapDto> {
        val fileOwnMapEntities = fileOwnMapRepository.findAllByOwnIdAndFileLocEntityUploaded(ownId, true)
        val fileOwnMapList: MutableList<FileOwnMapDto> = mutableListOf()
        for (fileOwnMapEntity in fileOwnMapEntities) {
            val fileLocEntity = fileOwnMapEntity.fileLocEntity
            val fileLocDto = FileLocDto(
                    fileSeq = fileLocEntity.fileSeq,
                    fileOwner = fileLocEntity.fileOwner,
                    fileSize = fileLocEntity.fileSize,
                    originName = fileLocEntity.originName,
                    randomName = fileLocEntity.randomName,
                    sort = fileLocEntity.sort,
                    uploaded = fileLocEntity.uploaded,
                    uploadedLocation = fileLocEntity.uploadedLocation
            )
            val fileOwnMapDto = FileOwnMapDto(
                    ownId = fileOwnMapEntity.ownId,
                    fileLocDto = fileLocDto
            )
            fileOwnMapList.add(fileOwnMapDto)
        }
        return fileOwnMapList
    }

    /**
     * 시퀀스 번호로 파일을 삭제한다.
     */
    @Transactional
    fun delete(seq: Long) {
        delete(fileOwnMapRepository.findByFileLocEntityFileSeq(seq).fileLocEntity)
    }

    /**
     * 소유번호로 파일을 삭제한다.
     */
    fun delete(ownId: String) {
        val fileOwnMapEntityList = fileOwnMapRepository.findAllByOwnId(ownId)
        for (fileOwnMapEntity in fileOwnMapEntityList) {
            delete(fileOwnMapEntity.fileLocEntity)
        }
    }

    /**
     * 파일을 삭제한다.
     */
    private fun delete(fileLocEntity: FileLocEntity) {
        try {
            Files.delete(Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName))
            logger.info(
                "Delete physical file {}({})",
                fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName,
                fileLocEntity.originName
            )
        } catch (e: Exception) {
            logger.warn(
                "Delete physical file failed. {}\nFile info: {}({})",
                e.message,
                fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName,
                fileLocEntity.originName
            )
        }
        fileOwnMapRepository.deleteByFileLocEntity(fileLocEntity)
    }

    /**
     * 파일을 다운로드 한다.
     */
    fun download(seq: Long): ResponseEntity<InputStreamResource> {
        val fileLocEntity = fileLocRepository.getOne(seq)
        val resource =
            FileSystemResource(Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName))
        if (resource.exists()) {
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Tika().detect(resource.path)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileLocEntity.originName + "\"")
                .body(InputStreamResource(resource.inputStream))
        } else {
            logger.error(
                "File not found: {}",
                fileLocEntity.uploadedLocation + File.separator + fileLocEntity.originName
            )
            throw AliceException(AliceErrorConstants.ERR, "File not found: " + fileLocEntity.originName)
        }
    }
}
