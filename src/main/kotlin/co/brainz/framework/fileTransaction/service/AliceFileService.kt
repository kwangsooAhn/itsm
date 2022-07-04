/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.fileTransaction.service

import co.brainz.framework.auth.constants.AuthConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.fileTransaction.constants.FileConstants
import co.brainz.framework.fileTransaction.dto.AliceFileDetailDto
import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.dto.AliceFileLocDto
import co.brainz.framework.fileTransaction.dto.AliceFileOwnMapDto
import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.AliceFileOwnMapEntity
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.framework.fileTransaction.repository.AliceFileLocRepository
import co.brainz.framework.fileTransaction.repository.AliceFileNameExtensionRepository
import co.brainz.framework.fileTransaction.repository.AliceFileOwnMapRepository
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceFileUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.user.service.UserService
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Base64
import javax.imageio.ImageIO
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
    private val aliceFileProvider: AliceFileProvider,
    private val currentSessionUser: CurrentSessionUser,
    private val userService: UserService,
    environment: Environment
) : AliceFileUtil(environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 파일을 임시로 업로드한다.
     * temp 경로에 파일을 임시로 업로드하고 파일관리테이블에 uploaded 상태를 false 로 저장한다.
     */
    @Transactional
    fun uploadTemp(multipartFile: MultipartFile): AliceFileLocEntity {
        val fileName = super.getRandomFilename()
        val tempPath = super.getUploadFilePath(FileConstants.Path.TEMP.path, fileName)
        val filePath = super.getUploadFilePath(FileConstants.Path.UPLOAD.path, fileName)
        val fileNameExtension = File(multipartFile.originalFilename!!).extension.toUpperCase()

        if (Files.notExists(tempPath.parent)) {
            throw AliceException(AliceErrorConstants.ERR, "Unknown file path. [" + tempPath.toFile() + "]")
        }

        aliceFileNameExtensionRepository.findByIdOrNull(fileNameExtension)
            ?: throw AliceException(AliceErrorConstants.ERR_00001, "The file extension  is not allowed.")

        multipartFile.transferTo(tempPath.toFile())

        val aliceFileLocEntity = AliceFileLocEntity(
            fileSeq = 0,
            fileOwner = currentSessionUser.getUserKey(),
            uploaded = false,
            uploadedLocation = filePath.parent.toString(),
            randomName = fileName,
            originName = multipartFile.originalFilename,
            fileSize = multipartFile.size,
            sort = 0
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
            val tempPath = super.getUploadFilePath(FileConstants.Path.TEMP.path, fileLocEntity.randomName)
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
            this.delete(aliceFileOwnMapRepository.findByFileLocEntityFileSeq(delFileSeq).fileLocEntity)
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
                val tempPath = super.getUploadFilePath(FileConstants.Path.TEMP.path, fileLocEntity.randomName)

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
        val dir = super.getPath(FileConstants.Path.PROCESSES.path)
        val filePath = Paths.get(dir.toString() + File.separator + multipartFile.originalFilename)

        multipartFile.transferTo(filePath.toFile())
    }

    /**
     * 파일관리 파일 업로드.
     */
    fun uploadFiles(multipartFiles: List<MultipartFile>): ZResponse {
        val dir = super.getPath(FileConstants.Path.FILE.path)
        val extSet = mutableSetOf<String>()

        aliceFileProvider.getFileNameExtension().forEach {
            extSet.add(it.fileNameExtension.toUpperCase())
        }

        multipartFiles.forEach {
            val filePath = Paths.get(dir.toString() + File.separator + it.originalFilename)
            val file = filePath.toFile()
            if (extSet.contains(file.extension.toUpperCase())) {
                var num = 1
                var fileName = file.name

                while (file.exists()) {
                    fileName = file.nameWithoutExtension + "(" + num++ + ")." + file.extension
                    file.renameTo(File(dir.toFile(), fileName))
                }
                it.transferTo(file)
            }
        }

        return ZResponse()
    }

    /**
     * 파일 조회
     */
    fun getFile(name: String): AliceFileDetailDto? {
        val dir = super.getPath(FileConstants.Path.FILE.path)
        val filePath = Paths.get(dir.toString() + File.separator + name)
        val file = filePath.toFile()
        var fileDetailDto: AliceFileDetailDto? = null

        if (file.exists()) {
            var width: Int? = null
            var height: Int? = null

            if (aliceFileProvider.getAllowedImageExtensions().indexOf(file.extension.toLowerCase()) > -1) {
                val bufferedImage = ImageIO.read(file)
                width = bufferedImage.width
                height = bufferedImage.height
            }

            fileDetailDto = AliceFileDetailDto(
                name = file.name,
                extension = file.extension,
                fullpath = file.absolutePath,
                data = Base64.getEncoder().encodeToString(file.readBytes()),
                width = width,
                height = height,
                size = super.humanReadableByteCount(file.length()),
                updateDt = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault())
            )

            fileDetailDto.width = width
        }

        return fileDetailDto
    }

    /**
     * 파일 삭제.
     */
    fun deleteFile(name: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val dir = super.getPath(FileConstants.Path.FILE.path)
        val filePath = Paths.get(dir.toString() + File.separator + name)

        try {
            Files.delete(filePath)
        } catch (e: IOException) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
            e.printStackTrace()
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 파일명 수정.
     */
    fun renameFile(originName: String, modifyName: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val dir = super.getPath(FileConstants.Path.FILE.path)
        val filePath = Paths.get(dir.toString() + File.separator + originName)
        val file = filePath.toFile()
        val modifyFile = File(dir.toFile(), modifyName)

        if (file.exists() && !modifyFile.exists()) {
            try {
                file.renameTo(modifyFile)
            } catch (e: IOException) {
                status = ZResponseConstants.STATUS.ERROR_FAIL
            }
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 파일 목록을 가져온다.
     */
    fun getList(ownId: String, fileDataId: String): List<AliceFileOwnMapDto> {
        val aliceFileOwnMapList: MutableList<AliceFileOwnMapDto> = mutableListOf()

        if (ownId.isNotBlank()) {
            val fileOwnMapEntities =
                aliceFileOwnMapRepository.findFileOwnIdAndFileLocUploaded(ownId, true)

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
        } else if (fileDataId.isNotBlank()) {
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
        this.delete(aliceFileOwnMapRepository.findByFileLocEntityFileSeq(seq).fileLocEntity)
    }

    /**
     * 소유번호로 파일을 삭제한다.
     */
    fun delete(ownId: String) {
        val fileOwnMapEntityList = aliceFileOwnMapRepository.findAllByOwnId(ownId)

        for (fileOwnMapEntity in fileOwnMapEntityList) {
            this.delete(fileOwnMapEntity.fileLocEntity)
        }
    }

    /**
     * 파일관리 다운로드
     */
    fun download(fileName: String): ResponseEntity<InputStreamResource> {
        this.fileNameSpecialCheck(fileName)
        userService.userAccessAuthCheck("", AuthConstants.AuthType.WORKFLOW_MANAGE.value)

        val dir = super.getPath(FileConstants.Path.FILE.path)
        val resource =
            FileSystemResource(Paths.get(dir.toString() + File.separator + fileName))

        if (resource.exists() && fileName.isNotEmpty()) {
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Tika().detect(resource.path)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
                .body(InputStreamResource(resource.inputStream))
        } else {
            logger.error("File not found: {}", fileName)
            throw AliceException(AliceErrorConstants.ERR, "File not found: $fileName")
        }
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
     * 파일명 검사
     */
    private fun fileNameSpecialCheck(fileName: String) {
        val regexChar = "[/\\\\%]".toRegex()

        if (regexChar.containsMatchIn(fileName)) {
            throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "Invalid file name :  $fileName"
            )
        }
    }
}
