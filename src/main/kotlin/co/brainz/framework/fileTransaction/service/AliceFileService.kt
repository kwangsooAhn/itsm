package co.brainz.framework.fileTransaction.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.dto.AliceFileLocDto
import co.brainz.framework.fileTransaction.dto.AliceFileOwnMapDto
import co.brainz.framework.fileTransaction.dto.AliceImageFileDto
import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.AliceFileNameExtensionEntity
import co.brainz.framework.fileTransaction.entity.AliceFileOwnMapEntity
import co.brainz.framework.fileTransaction.repository.AliceFileLocRepository
import co.brainz.framework.fileTransaction.repository.AliceFileNameExtensionRepository
import co.brainz.framework.fileTransaction.repository.AliceFileOwnMapRepository
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Long.signum
import java.lang.String.format
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.text.StringCharacterIterator
import java.util.Base64
import java.util.Calendar
import java.util.stream.Collectors
import javax.imageio.ImageIO
import kotlin.math.abs
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

/**
 * 파일 서비스 클래스
 */
@Service
class AliceFileService(
    private val aliceFileLocRepository: AliceFileLocRepository,
    private val aliceFileNameExtensionRepository: AliceFileNameExtensionRepository,
    private val aliceFileOwnMapRepository: AliceFileOwnMapRepository,
    private val environment: Environment
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${file.upload.dir}")
    lateinit var basePath: String

    private val processStatusRootDirectory = "processes"
    private val imagesRootDirectory = "images"
    private val allowedImageExtensions = listOf("png", "gif", "jpg", "jpeg")
    private val thumbnailImageWidth = 300

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
     * 파일 허용 확장자 목록 가져오기
     */
    fun getFileNameExtension(): List<AliceFileNameExtensionEntity> {
        return aliceFileNameExtensionRepository.findAll()
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
     * 프로세스 및 이미지관리에서 사용하는 dir 조회.
     */
    private fun getWorkflowDir(rootDir: String): Path {
        if (this.basePath == "") {
            this.basePath = environment.getProperty("catalina.base").toString()
        }

        var dir: Path = Paths.get(basePath + File.separator + rootDir)
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return dir
    }

    /**
     * 파일을 임시로 업로드한다.
     * temp 경로에 파일을 임시로 업로드하고 파일관리테이블에 uploaded 상태를 false로 저장한다.
     */
    @Transactional
    fun uploadTemp(multipartFile: MultipartFile): AliceFileLocEntity {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val fileName = getRandomFilename()
        val tempPath = getDir("temp", fileName)
        val filePath = getDir("uploadRoot", fileName)
        val fileNameExtension = File(multipartFile.originalFilename).extension.toUpperCase()

        if (Files.notExists(tempPath.parent)) {
            throw AliceException(AliceErrorConstants.ERR, "Unknown file path. [" + tempPath.toFile() + "]")
        }

        if (aliceFileNameExtensionRepository.findById(fileNameExtension).isEmpty) {
            throw AliceException(AliceErrorConstants.ERR_00004, "The file extension is not allowed.")
        }

        multipartFile.transferTo(tempPath.toFile())

        val aliceFileLocEntity = AliceFileLocEntity(
            0,
            aliceUserDto.userKey,
            false,
            filePath.parent.toString(),
            fileName,
            multipartFile.originalFilename,
            multipartFile.size,
            0
        )
        logger.debug("{}", aliceFileLocEntity)
        aliceFileLocRepository.save(aliceFileLocEntity)
        logger.debug(">> 임시업로드파일 {}", tempPath.toAbsolutePath())

        return aliceFileLocEntity
    }

    /**
     * 임시 업로드된 파일을 업로드한다.
     * 임시 업로드 경로에 업로드된 파일을 업로드하고 파일관리테이블에 uploaded 상태를 true 변경하여 조회가 가능하도록 한다.
     */
    fun upload(aliceFileDto: AliceFileDto) {
        for (fileSeq in aliceFileDto.fileSeq.orEmpty()) {
            val fileLocEntity = aliceFileLocRepository.getOne(fileSeq)
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
                val fileOwnMapEntity = AliceFileOwnMapEntity(aliceFileDto.ownId, fileLocEntity)
                aliceFileOwnMapRepository.save(fileOwnMapEntity)
            } catch (e: Exception) {
                logger.error("{}", e.message)
                Files.move(filePath, tempPath, StandardCopyOption.REPLACE_EXISTING)
                throw AliceException(AliceErrorConstants.ERR, e.message)
            }
        }
    }

    /**
     * 문서에서 사용한다.
     * 임시 업로드된 파일을 업로드한다.
     * 임시 업로드 경로에 업로드된 파일을 업로드하고 파일관리테이블에 uploaded 상태를 true 변경하여 조회가 가능하도록 한다.
     */
    fun uploadFiles(fileDataId: String) {
        val fileDataIds = fileDataId.split(',')
        for (index in fileDataIds.indices) {
            if (fileDataIds[index].isNotEmpty()) {
                val fileLocEntity = aliceFileLocRepository.getOne(fileDataIds[index].toLong())
                val filePath = Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName)
                val tempPath = getDir("temp", fileLocEntity.randomName)
                if (Files.exists(tempPath)) {
                    Files.move(tempPath, filePath, StandardCopyOption.REPLACE_EXISTING)
                    fileLocEntity.uploaded = true
                    try {
                        aliceFileLocRepository.save(fileLocEntity)
                    } catch (e: Exception) {
                        logger.error("{}", e.message)
                        Files.move(filePath, tempPath, StandardCopyOption.REPLACE_EXISTING)
                        throw AliceException(AliceErrorConstants.ERR, e.message)
                    }
                }
            }
        }
    }

    /**
     * 프로세스 상태 표시를 위한 프로세스 XML 파일을 업로드한다.
     */
    fun uploadProcessFile(multipartFile: MultipartFile) {
        val dir = getWorkflowDir(this.processStatusRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + multipartFile.originalFilename)
        multipartFile.transferTo(filePath.toFile())
    }

    /**
     * 프로세스 상태 파일 로드.
     */
    fun getProcessStatusFile(processId: String): File {
        val dir = getWorkflowDir(this.processStatusRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + processId + ".xml")
        return filePath.toFile()
    }

    /**
     * 워크플로우 이미지 파일 업로드.
     */
    fun uploadImageFiles(multipartFiles: List<MultipartFile>): List<AliceImageFileDto> {
        val dir = getWorkflowDir(this.imagesRootDirectory)
        val images = mutableListOf<AliceImageFileDto>()
        multipartFiles.forEach {
            val filePath = Paths.get(dir.toString() + File.separator + it.originalFilename)
            val file = filePath.toFile()
            if (allowedImageExtensions.indexOf(file.extension.toLowerCase()) > -1) {
                var num = 1
                var fileName = file.name
                while (file.exists()) {
                    fileName = file.nameWithoutExtension + "(" + num++ + ")." + file.extension
                    file.renameTo(File(dir.toFile(), fileName))
                }
                it.transferTo(file)
                val bufferedImage = ImageIO.read(file)
                val resizedBufferedImage = resizeBufferedImage(bufferedImage)
                images.add(
                    AliceImageFileDto(
                        name = fileName,
                        extension = file.extension,
                        fullpath = file.absolutePath,
                        size = humanReadableByteCount(it.size),
                        data = encodeToString(resizedBufferedImage, file.extension),
                        width = bufferedImage.width,
                        height = bufferedImage.height
                    )
                )
            }
        }

        return images
    }

    /**
     * 워크플로우 이미지 파일 로드.
     */
    fun getImageFileList(): List<AliceImageFileDto> {
        val dir = getWorkflowDir(this.imagesRootDirectory)
        val fileList = mutableListOf<Path>()
        if (Files.isDirectory(dir)) {
            val fileDirMap = Files.list(dir).collect(Collectors.partitioningBy { it -> Files.isDirectory(it) })
            fileDirMap[false]?.forEach { filePath ->
                val file = filePath.toFile()
                if (allowedImageExtensions.indexOf(file.extension.toLowerCase()) > -1) {
                    fileList.add(filePath)
                }
            }
        }

        val images = mutableListOf<AliceImageFileDto>()
        fileList.forEach {
            val file = it.toFile()
            val bufferedImage = ImageIO.read(file)
            val resizedBufferedImage = resizeBufferedImage(bufferedImage)
            images.add(
                AliceImageFileDto(
                    name = file.name,
                    extension = file.extension,
                    fullpath = file.absolutePath,
                    size = humanReadableByteCount(file.length()),
                    data = encodeToString(resizedBufferedImage, file.extension),
                    width = bufferedImage.width,
                    height = bufferedImage.height
                )
            )
        }
        return images
    }

    /**
     * 이미지 사이즈 조정.
     */
    private fun resizeBufferedImage(image: BufferedImage): BufferedImage {
        val scaledWidth = thumbnailImageWidth
        val scaledHeight = image.height / (image.width / scaledWidth)
        val bufferedImage = BufferedImage(scaledWidth, scaledHeight, image.type)
        val g2d = bufferedImage.createGraphics()
        g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null)
        g2d.dispose()
        return bufferedImage
    }

    /**
     * 이미지 data 조회.
     */
    private fun encodeToString(image: BufferedImage, type: String): String {
        var imageString = ""
        val bos = ByteArrayOutputStream()
        try {
            ImageIO.write(image, type, bos)
            val imageBytes = bos.toByteArray()
            imageString = Base64.getEncoder().encodeToString(imageBytes)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            bos.close()
        }
        return imageString
    }

    /**
     * 이미지 로드
     */
    fun getImageFile(name: String): AliceImageFileDto? {
        val dir = getWorkflowDir(this.imagesRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + name)
        val file = filePath.toFile()
        return if (file.exists()) {
            val bufferedImage = ImageIO.read(file)
            AliceImageFileDto(
                name = file.name,
                extension = file.extension,
                fullpath = file.absolutePath,
                size = humanReadableByteCount(file.length()),
                data = Base64.getEncoder().encodeToString(file.readBytes()),
                width = bufferedImage.width,
                height = bufferedImage.height
            )
        } else {
            null
        }
    }

    /**
     * byte 사이즈를 단위를 넣어 변환한다.
     */
    private fun humanReadableByteCount(bytes: Long): String {
        val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
        if (absB < 1024) {
            return "$bytes Byte"
        }
        var value = absB
        val ci = StringCharacterIterator("KMGTPE")
        var i = 40
        while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
            value = value shr 10
            ci.next()
            i -= 10
        }
        value *= signum(bytes)
        return format("%.1f %cByte", value / 1024.0, ci.current())
    }

    /**
     * 이미지 삭제.
     */
    fun deleteImage(name: String): Boolean {
        val dir = getWorkflowDir(this.imagesRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + name)
        return try {
            Files.delete(filePath)
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * 이미지명 수정.
     */
    fun renameImage(originName: String, modifyName: String): Boolean {
        val dir = getWorkflowDir(this.imagesRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + originName)
        val file = filePath.toFile()
        if (file.exists()) {
            val modifyFile = File(dir.toFile(), modifyName)
            if (modifyFile.exists()) {
                return false
            }
            return try {
                file.renameTo(modifyFile)
                true
            } catch (e: IOException) {
                false
            }
        } else {
            return false
        }
    }

    /**
     * 파일 목록을 가져온다.
     */
    fun getList(ownId: String, fileDataId: String): List<AliceFileOwnMapDto> {
        val aliceFileOwnMapList: MutableList<AliceFileOwnMapDto> = mutableListOf()
        if (ownId != "") {
            val fileOwnMapEntities = aliceFileOwnMapRepository.findAllByOwnIdAndFileLocEntityUploaded(ownId, true)
            for (fileOwnMapEntity in fileOwnMapEntities) {
                val fileLocEntity = fileOwnMapEntity.fileLocEntity
                val fileLocDto = AliceFileLocDto(
                    fileSeq = fileLocEntity.fileSeq,
                    fileOwner = fileLocEntity.fileOwner,
                    fileSize = fileLocEntity.fileSize,
                    originName = fileLocEntity.originName,
                    randomName = fileLocEntity.randomName,
                    sort = fileLocEntity.sort,
                    uploaded = fileLocEntity.uploaded,
                    uploadedLocation = fileLocEntity.uploadedLocation
                )
                val fileOwnMapDto = AliceFileOwnMapDto(
                    ownId = fileOwnMapEntity.ownId,
                    fileLocDto = fileLocDto
                )
                aliceFileOwnMapList.add(fileOwnMapDto)
            }
        } else if (fileDataId != "") {
            val fileDataIds = fileDataId.split(',')
            for (index in fileDataIds.indices) {
                val aliceFileLocEntity = aliceFileLocRepository.getOne(fileDataIds[index].toLong())
                val fileLocDto = AliceFileLocDto(
                    fileSeq = aliceFileLocEntity.fileSeq,
                    fileOwner = aliceFileLocEntity.fileOwner,
                    fileSize = aliceFileLocEntity.fileSize,
                    originName = aliceFileLocEntity.originName,
                    randomName = aliceFileLocEntity.randomName,
                    sort = aliceFileLocEntity.sort,
                    uploaded = aliceFileLocEntity.uploaded,
                    uploadedLocation = aliceFileLocEntity.uploadedLocation
                )
                val fileOwnMapDto = AliceFileOwnMapDto(
                    ownId = "",
                    fileLocDto = fileLocDto
                )
                aliceFileOwnMapList.add(fileOwnMapDto)
            }
        }
        return aliceFileOwnMapList
    }

    /**
     * 시퀀스 번호로 파일을 삭제한다.
     */
    @Transactional
    fun delete(seq: Long) {
        delete(aliceFileOwnMapRepository.findByFileLocEntityFileSeq(seq).fileLocEntity)
    }

    /**
     * 소유번호로 파일을 삭제한다.
     */
    fun delete(ownId: String) {
        val fileOwnMapEntityList = aliceFileOwnMapRepository.findAllByOwnId(ownId)
        for (fileOwnMapEntity in fileOwnMapEntityList) {
            delete(fileOwnMapEntity.fileLocEntity)
        }
    }

    /**
     * 파일을 삭제한다.
     */
    private fun delete(aliceFileLocEntity: AliceFileLocEntity) {
        try {
            Files.delete(Paths.get(aliceFileLocEntity.uploadedLocation + File.separator + aliceFileLocEntity.randomName))
            logger.info(
                "Delete physical file {}({})",
                aliceFileLocEntity.uploadedLocation + File.separator + aliceFileLocEntity.randomName,
                aliceFileLocEntity.originName
            )
        } catch (e: Exception) {
            logger.warn(
                "Delete physical file failed. {}\nFile info: {}({})",
                e.message,
                aliceFileLocEntity.uploadedLocation + File.separator + aliceFileLocEntity.randomName,
                aliceFileLocEntity.originName
            )
        }
        aliceFileOwnMapRepository.deleteByFileLocEntity(aliceFileLocEntity)
    }

    /**
     * 파일을 다운로드 한다.
     */
    fun download(seq: Long): ResponseEntity<InputStreamResource> {
        val fileLocEntity = aliceFileLocRepository.getOne(seq)
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

    fun uploadResources(multipartFile: MultipartFile, location: String, baseDir: String, fileName: String?) {
        this.basePath = ClassPathResource(baseDir).file.path.toString()
        val fileNameExtension = File(multipartFile.originalFilename).extension.toUpperCase()
        val filePath: Path
        var dir = Paths.get(this.basePath, location)
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)

        filePath = when (fileName) {
            null -> {
                Paths.get(dir.toString() + File.separator + multipartFile.originalFilename)
            }
            else -> {
                Paths.get(dir.toString() + File.separator + fileName)
            }
        }

        if (aliceFileNameExtensionRepository.findById(fileNameExtension).isEmpty) {
            throw AliceException(AliceErrorConstants.ERR_00004, "The file extension is not allowed.")
        }

        multipartFile.transferTo(filePath.toFile())
    }

    fun uploadAvatar(location: String, baseDir: String, userKey: String, avatarUUID: String) {
        this.basePath = ClassPathResource(baseDir).file.path.toString()
        val dir = Paths.get(this.basePath, location)
        val tempPath = Paths.get(dir.toString() + File.separator + avatarUUID)
        val filePath = Paths.get(dir.toString() + File.separator + userKey)
        val sampleFilePath = Paths.get(dir.toString() + File.separator + AliceUserConstants.SAMPLE_FILE_NAME)
        val tempFile = File(tempPath.toString())
        val uploadFile = File(filePath.toString())

        when (tempFile.exists()) {
            true -> {
                Files.move(tempPath, filePath, StandardCopyOption.REPLACE_EXISTING)
            }
            false -> {
                if (!uploadFile.exists()) {
                    Files.copy(sampleFilePath, filePath)
                }
            }
        }
    }
}
