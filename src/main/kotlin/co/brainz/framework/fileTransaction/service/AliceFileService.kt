/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.framework.fileTransaction.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.AliceConstants
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
import co.brainz.framework.util.AliceFileUtil
import co.brainz.itsm.constants.ItsmConstants
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Base64
import java.util.stream.Collectors
import javax.imageio.ImageIO
import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamResource
import org.springframework.data.repository.findByIdOrNull
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
    private val userRepository: AliceUserRepository,
    environment: Environment
) : AliceFileUtil(environment) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val processStatusRootDirectory = "processes"
    private val imagesRootDirectory = "images"
    private val allowedImageExtensions = listOf("png", "gif", "jpg", "jpeg")
    private val fileUploadRootDirectory = "uploadRoot"
    private val processAttachFileRootDirectory = this.imagesRootDirectory
    private val documentIconRootDirectory = "public/assets/media/images/document"
    private val typeIconRootDirectory = "public/assets/media/images/cmdb"

    /**
     * 파일 허용 확장자 목록 가져오기
     */
    fun getFileNameExtension(): List<AliceFileNameExtensionEntity> {
        return aliceFileNameExtensionRepository.findAll()
    }

    /**
     * 파일을 임시로 업로드한다.
     * temp 경로에 파일을 임시로 업로드하고 파일관리테이블에 uploaded 상태를 false로 저장한다.
     */
    @Transactional
    fun uploadTemp(multipartFile: MultipartFile): AliceFileLocEntity {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val fileName = super.getRandomFilename()
        val tempPath = super.getDir("temp", fileName)
        val filePath = super.getDir(this.fileUploadRootDirectory, fileName)
        val fileNameExtension = File(multipartFile.originalFilename!!).extension.toUpperCase()
        val metadata = Metadata()
        metadata[Metadata.RESOURCE_NAME_KEY] = multipartFile.originalFilename
        metadata[Metadata.CONTENT_TYPE] = multipartFile.contentType
        val mediaType = Tika().detect(multipartFile.inputStream, metadata)

        if (Files.notExists(tempPath.parent)) {
            throw AliceException(AliceErrorConstants.ERR, "Unknown file path. [" + tempPath.toFile() + "]")
        }

        // 파일 확장자 및 파일 media type 체크
        val fileExtensionEntity = aliceFileNameExtensionRepository.findByIdOrNull(fileNameExtension)
        if (fileExtensionEntity == null || fileExtensionEntity.fileContentType != mediaType) {
            throw AliceException(AliceErrorConstants.ERR_00001, "The file extension $mediaType is not allowed.")
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
     * 임시 경로에 업로드된 파일을 실제 경로에 업로드하고 파일관리테이블에 uploaded 상태를 true 변경하여 조회가 가능하도록 한다.
     * 업로드 간 삭제할 파일이 있다면 동시에 같이 삭제한다.
     */
    fun upload(aliceFileDto: AliceFileDto) {
        for (fileSeq in aliceFileDto.fileSeq.orEmpty()) {
            val fileLocEntity = aliceFileLocRepository.getOne(fileSeq)
            val filePath = Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName)
            val tempPath = super.getDir("temp", fileLocEntity.randomName)
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
        for (delFileSeq in aliceFileDto.delFileSeq.orEmpty()) {
            delete(aliceFileOwnMapRepository.findByFileLocEntityFileSeq(delFileSeq).fileLocEntity)
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
                val tempPath = super.getDir("temp", fileLocEntity.randomName)
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
        val dir = super.getWorkflowDir(this.processStatusRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + multipartFile.originalFilename)
        multipartFile.transferTo(filePath.toFile())
    }

    /**
     * 프로세스 상태 파일 로드.
     */
    fun getProcessStatusFile(processId: String): File {
        val dir = super.getWorkflowDir(this.processStatusRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + processId + ".xml")
        return filePath.toFile()
    }

    /**
     * 워크플로우 이미지 파일 업로드.
     */
    fun uploadImageFiles(multipartFiles: List<MultipartFile>): List<AliceImageFileDto> {
        val dir = super.getWorkflowDir(this.imagesRootDirectory)
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
                val resizedBufferedImage = resizeBufferedImage(bufferedImage, AliceConstants.FileType.IMAGE.code)
                images.add(
                    AliceImageFileDto(
                        name = fileName,
                        extension = file.extension,
                        fullpath = file.absolutePath,
                        size = super.humanReadableByteCount(it.size),
                        data = super.encodeToString(resizedBufferedImage, file.extension),
                        width = bufferedImage.width,
                        height = bufferedImage.height,
                        updateDt = LocalDateTime.now()
                    )
                )
            }
        }

        return images
    }

    /**
     * 업로드 경로.
     */
    fun getUploadFilePath(fileName: String): Path {
        return super.getDir(this.fileUploadRootDirectory, fileName)
    }

    /**
     * 프로세스 디자이너 첨부파일 경로.
     */
    fun getProcessFilePath(): Path {
        return super.getWorkflowDir(this.processAttachFileRootDirectory)
    }

    /**
     * 이미지리스트 전체 또는 offset 단위로 가져오기
     *
     */
    fun getImageFileList(type: String, searchValue: String, offset: Long): List<AliceImageFileDto> {
        val dir = when (type) {
            AliceConstants.FileType.ICON.code -> Paths.get(javaClass.classLoader.getResource(this.documentIconRootDirectory).toURI())
            AliceConstants.FileType.ICON_TYPE.code -> Paths.get(javaClass.classLoader.getResource(this.typeIconRootDirectory).toURI())
            else -> super.getWorkflowDir(this.imagesRootDirectory)
        }
//        logger.debug(">>>> WORKFLOW IMAGE URI = {}", Paths.get(ClassPathResource(this.documentIconRootDirectory).uri))
        var searchDataCount = ItsmConstants.IMAGE_SEARCH_DATA_COUNT
        var offset = offset
        var endIndex = 0
        val fileList = mutableListOf<Path>()
        if (Files.isDirectory(dir)) {
            val fileDirMap = Files.list(dir).collect(Collectors.partitioningBy { Files.isDirectory(it) })
            fileDirMap[false]?.forEach { filePath ->
                val file = filePath.toFile()
                if (allowedImageExtensions.indexOf(file.extension.toLowerCase()) > -1) {
                    when (searchValue) {
                        "" -> fileList.add(filePath)
                        else -> {
                            if (file.name.matches(".*$searchValue.*".toRegex())) {
                                fileList.add(filePath)
                            }
                        }
                    }
                }
            }
        }
        if (offset === -1L) {
            offset = 0L
            endIndex = fileList.size
        } else {
            endIndex = (offset + searchDataCount).toInt()
            if (fileList.size < endIndex) {
                endIndex = fileList.size
            }
        }
        val images = mutableListOf<AliceImageFileDto>()
        for (i in offset until endIndex) {
            val file = fileList[i.toInt()].toFile()
            val bufferedImage = ImageIO.read(file)
            val resizedBufferedImage = resizeBufferedImage(bufferedImage, "")
            images.add(
                AliceImageFileDto(
                    name = file.name,
                    extension = file.extension,
                    fullpath = file.absolutePath,
                    size = super.humanReadableByteCount(file.length()),
                    data = super.encodeToString(resizedBufferedImage, file.extension),
                    width = bufferedImage.width,
                    height = bufferedImage.height,
                    totalCount = fileList.size.toLong(),
                    updateDt = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(file.lastModified()),
                        ZoneId.systemDefault()
                    )
                )
            )
        }
        return images
    }

    /**
     * 이미지 로드
     */
    fun getImageFile(name: String): AliceImageFileDto? {
        val dir = super.getWorkflowDir(this.imagesRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + name)
        val file = filePath.toFile()
        return if (file.exists()) {
            val bufferedImage = ImageIO.read(file)
            AliceImageFileDto(
                name = file.name,
                extension = file.extension,
                fullpath = file.absolutePath,
                size = super.humanReadableByteCount(file.length()),
                data = Base64.getEncoder().encodeToString(file.readBytes()),
                width = bufferedImage.width,
                height = bufferedImage.height,
                updateDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault())
            )
        } else {
            null
        }
    }

    /**
     * 이미지 삭제.
     */
    fun deleteImage(name: String): Boolean {
        val dir = super.getWorkflowDir(this.imagesRootDirectory)
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
        val dir = super.getWorkflowDir(this.imagesRootDirectory)
        val filePath = Paths.get(dir.toString() + File.separator + originName)
        val file = filePath.toFile()
        val modifyFile = File(dir.toFile(), modifyName)
        return if (file.exists() && !modifyFile.exists()) {
            try {
                file.renameTo(modifyFile)
            } catch (e: IOException) {
                false
            }
        } else {
            false
        }
    }

    /**
     * 파일 목록을 가져온다.
     */
    fun getList(ownId: String, fileDataId: String): List<AliceFileOwnMapDto> {
        val aliceFileOwnMapList: MutableList<AliceFileOwnMapDto> = mutableListOf()
        if (ownId != "") {
            val fileOwnMapEntities =
                aliceFileOwnMapRepository.findAllByOwnIdAndFileLocEntityUploaded(ownId, true)
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
            Files.delete(
                Paths.get(
                    aliceFileLocEntity.uploadedLocation + File.separator + aliceFileLocEntity.randomName
                )
            )
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

    /**
     * 회원 가입 시 아바타 파일[multipartFile]를 받아서 임시 폴더에[avatar/temp/]저장 한다.
     */
    fun uploadTempAvatarFile(multipartFile: MultipartFile, fileName: String?) {
        val fileNameExtension = File(multipartFile.originalFilename!!).extension.toUpperCase()
        val filePath: Path
        var dir = super.getWorkflowDir(AliceUserConstants.AVATAR_IMAGE_TEMP_DIR)
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
            throw AliceException(AliceErrorConstants.ERR_00001, "The file extension is not allowed.")
        }

        multipartFile.transferTo(filePath.toFile())
    }

    /**
     * 업로드한 아바타 이미지정보를 [userEntity] ,[avatarUUID] 를 받아서 처리한다.
     */
    fun uploadAvatarFile(userEntity: AliceUserEntity, avatarUUID: String) {
        val tempDir = super.getWorkflowDir(AliceUserConstants.AVATAR_IMAGE_TEMP_DIR)
        val tempPath = Paths.get(tempDir.toString() + File.separator + avatarUUID)
        val tempFile = File(tempPath.toString())

        val avatarDir = super.getWorkflowDir(AliceUserConstants.AVATAR_IMAGE_DIR)
        val avatarFilePath = Paths.get(avatarDir.toString() + File.separator + avatarUUID)

        if (avatarUUID !== "" && tempFile.exists()) {
            Files.move(tempPath, avatarFilePath, StandardCopyOption.REPLACE_EXISTING)
            userEntity.avatarValue = avatarUUID
            userEntity.uploaded = true
            userEntity.uploadedLocation = avatarFilePath.toString()
        } else {
            if (avatarUUID == "" || !userEntity.uploaded) {
                val uploadedFile = Paths.get(userEntity.uploadedLocation)
                if (uploadedFile.toFile().exists()) {
                    Files.delete(uploadedFile)
                }
                userEntity.avatarValue = AliceUserConstants.AVATAR_BASIC_FILE_NAME
                userEntity.uploaded = false
                userEntity.uploadedLocation = AliceUserConstants.AVATAR_BASIC_FILE_PATH
            }
        }
    }

    /**
     * 아바타 이미지명을 uuid에서 ID 값으로 변경 한다.
     * 신규 사용자 등록 시 avatar_id, user_key를 구할 수가 없기 때문에
     * 임시적으로 생성한 avatar_uuid로 파일명을 만든다. avatar_uuid가 고유 값을 보장 하지 못하기 때문에
     * 사용자, 아바타 정보를 등록 후 다시 한번 파일명 및 아바타 이미지명을 변경한다.
     */
    fun avatarFileNameMod(userEntity: AliceUserEntity) {
        if (userEntity.avatarType == AliceUserConstants.AvatarType.FILE.code &&
            userEntity.uploaded && userEntity.userKey != userEntity.avatarValue
        ) {
            val avatarDir = super.getWorkflowDir(AliceUserConstants.AVATAR_IMAGE_DIR)
            val avatarFilePath = Paths.get(avatarDir.toString() + File.separator + userEntity.avatarValue)
            val avatarIdFilePath = Paths.get(avatarDir.toString() + File.separator + userEntity.userKey)
            val avatarUploadFile = File(avatarFilePath.toString())
            if (avatarUploadFile.exists()) {
                Files.move(avatarFilePath, avatarIdFilePath, StandardCopyOption.REPLACE_EXISTING)
                userEntity.avatarValue = userEntity.userKey
                userEntity.uploadedLocation = avatarIdFilePath.toString()
                userRepository.save(userEntity)
            }
        }
    }
}
