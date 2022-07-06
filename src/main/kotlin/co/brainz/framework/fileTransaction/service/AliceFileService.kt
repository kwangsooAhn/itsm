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
import co.brainz.framework.fileTransaction.dto.AliceFileDetailListReturnDto
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
     *
     * @param multipartFile
     * @return AliceFileLocEntity
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
     *  파일을 특정 경로에 업로드한다.
     *
     * @param multipartFile
     * @param path
     */
    fun uploadDirect(multipartFile: MultipartFile, path: String) {
        val dir = super.getPath(path)
        val filePath = Paths.get(dir.toString() + File.separator + multipartFile.originalFilename)

        multipartFile.transferTo(filePath.toFile())
    }

    /**
     * 임시 경로에 업로드된 파일을 실제 경로에 업로드하고 파일관리테이블에 uploaded 상태를 true 변경하여 조회가 가능하도록 한다.
     *
     * @param data
     */
    fun upload(data: Any) {
        var fileLocEntity: AliceFileLocEntity
        when (data) {
            is AliceFileDto -> {
                for (fileSeq in data.fileSeq.orEmpty()) {
                    fileLocEntity = aliceFileLocRepository.getOne(fileSeq)
                    this.moveFile(fileLocEntity, false)
                    fileLocEntity.uploaded = true

                    try {
                        val fileOwnMapEntity = AliceFileOwnMapEntity(data.ownId, fileLocEntity)
                        aliceFileOwnMapRepository.save(fileOwnMapEntity)
                    } catch (e: Exception) {
                        logger.error("{}", e.message)
                        this.moveFile(fileLocEntity, true)
                        throw AliceException(AliceErrorConstants.ERR, e.message)
                    }
                }

                for (delFileSeq in data.delFileSeq.orEmpty()) {
                    this.delete(aliceFileOwnMapRepository.findByFileLocEntityFileSeq(delFileSeq).fileLocEntity)
                }
            }
            is String -> {
                val fileDataIds = data.split(',')
                for (fileDataId in fileDataIds) {
                    fileLocEntity = aliceFileLocRepository.getOne(fileDataId.toLong())
                    this.moveFile(fileLocEntity, false)
                    fileLocEntity.uploaded = true

                    try {
                        aliceFileLocRepository.save(fileLocEntity)
                    } catch (e: Exception) {
                        logger.error("{}", e.message)
                        this.moveFile(fileLocEntity, true)
                        throw AliceException(AliceErrorConstants.ERR, e.message)
                    }
                }
            }
        }
    }

    /**
     * 파일 목록을 가져온다.
     *
     * @param ownId
     * @param fileDataId
     * @return List<AliceFileOwnMapDto>
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
     * 파일을 다운로드 한다.
     *
     * @param seq
     * @return ResponseEntity<InputStreamResource>
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
     * 시퀀스 번호로 파일을 삭제한다.
     *
     * @param seq
     */
    @Transactional
    fun delete(seq: Long) {
        this.delete(aliceFileOwnMapRepository.findByFileLocEntityFileSeq(seq).fileLocEntity)
    }

    /**
     * 소유 번호로 파일을 삭제한다.
     *
     * @param ownId
     */
    @Transactional
    fun delete(ownId: String) {
        val fileOwnMapEntityList = aliceFileOwnMapRepository.findAllByOwnId(ownId)

        for (fileOwnMapEntity in fileOwnMapEntityList) {
            this.delete(fileOwnMapEntity.fileLocEntity)
        }
    }

    /**
     * 파일을 삭제한다.
     *
     * @param aliceFileLocEntity
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
     * 파일 관리 - 파일 조회
     *
     * @param name
     * @return AliceFileDetailDto?
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
     * 파일 관리 - 다운로드
     *
     * @param fileName
     * @return ResponseEntity<InputStreamResource>
     */
    fun downloadFile(fileName: String): ResponseEntity<InputStreamResource> {
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
     * 파일 관리 - 파일 업로드
     * 신규 파일이 기존 파일과 이름이 동일 할 경우 파일명(1).jpg 와 같이 변경된다.
     *
     * @param multipartFiles
     * @return ZResponse
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
                val oriFileNameWithoutExtension = file.nameWithoutExtension
                var num = 1
                var fileName: String

                while (file.exists()) {
                    fileName = oriFileNameWithoutExtension + "(" + num++ + ")." + file.extension
                    // 현재 올라간 파일의 파일명을 변경한다.
                    filePath = Paths.get(dirPath.toString() + File.separator + fileName)
                    file = filePath.toFile()
                }
                it.transferTo(file)
            }
        }

        return ZResponse()
    }

    /**
     * 파일 관리 - 파일명 수정
     *
     * @param originName
     * @param modifyName
     * @return ZResponse
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
     * 파일 관리 - 파일 삭제
     *
     * @param name
     * @return ZResponse
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
     * 파일 관리 - 외부 경로에 있는 파일 정보 조회
     *
     * @param type
     * @param searchValue
     * @param currentOffset
     * @return AliceFileDetailListReturnDto
     */
    fun getExternalFileList(
        type: String,
        searchValue: String,
        currentOffset: Int = -1
    ): AliceFileDetailListReturnDto {
        return aliceFileProvider.getExternalFileList(type, searchValue, currentOffset)
    }

    /**
     * 파일 관리 - 파일명 검사.
     *
     * @param fileName
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

    /**
     * 파일을 특정 경로로 이동 및 복원한다.
     *
     * @param fileLocEntity
     * @param isRollback
     */
    private fun moveFile(fileLocEntity: AliceFileLocEntity, isRollback: Boolean) {
        val filePath = Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName)
        val tempPath = super.getUploadFilePath(FileConstants.Path.TEMP.path, fileLocEntity.randomName)

        if (isRollback) {
            Files.move(filePath, tempPath, StandardCopyOption.REPLACE_EXISTING)
        } else {
            Files.move(tempPath, filePath, StandardCopyOption.REPLACE_EXISTING)
        }
    }
}
