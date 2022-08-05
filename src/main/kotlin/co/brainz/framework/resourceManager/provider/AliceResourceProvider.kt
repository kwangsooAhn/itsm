/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.resourceManager.provider

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.resourceManager.dto.AliceFileDetailDto
import co.brainz.framework.resourceManager.dto.AliceFileDetailListReturnDto
import co.brainz.framework.resourceManager.dto.AliceFileDto
import co.brainz.framework.resourceManager.dto.AliceFileLocDto
import co.brainz.framework.resourceManager.dto.AliceFileOwnMapDto
import co.brainz.framework.resourceManager.dto.AliceResourceDto
import co.brainz.framework.resourceManager.dto.AliceResourceSearchDto
import co.brainz.framework.resourceManager.dto.AliceResourcesPagingDto
import co.brainz.framework.resourceManager.dto.AliceResourcesScrollDto
import co.brainz.framework.resourceManager.entity.AliceFileLocEntity
import co.brainz.framework.resourceManager.entity.AliceFileOwnMapEntity
import co.brainz.framework.resourceManager.repository.AliceFileLocRepository
import co.brainz.framework.resourceManager.repository.AliceFileNameExtensionRepository
import co.brainz.framework.resourceManager.repository.AliceFileOwnMapRepository
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.AliceResourceUtil
import co.brainz.framework.util.AliceScrollData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.constants.ItsmConstants
import java.io.File
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Collectors
import javax.imageio.ImageIO
import javax.transaction.Transactional
import kotlin.math.ceil
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class AliceResourceProvider(
    private val aliceFileNameExtensionRepository: AliceFileNameExtensionRepository,
    private val aliceFileOwnMapRepository: AliceFileOwnMapRepository,
    private val aliceFileLocRepository: AliceFileLocRepository,
    private val currentSessionUser: CurrentSessionUser,
    environment: Environment
) : AliceResourceUtil(environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val classPathInJar = "/BOOT-INF/classes/"

    /**
     * 파일 허용 확장자 가져오기
     */
    fun getAllowedExtensions(type: String): List<String> {
        return when (super.isAllowedOnlyImageByType(type)) {
            true -> getImageExtensions()
            false -> {
                val extensions = mutableListOf<String>()
                aliceFileNameExtensionRepository.findAll().forEach {
                    extensions.add(it.fileNameExtension)
                }
                return extensions
            }
        }
    }

    /**
     * 외부 경로 조회
     * @param type 타입
     */
    fun getExternalPath(type: String): Path {
        val dir = when (type) {
            ResourceConstants.FileType.ICON.code -> ResourceConstants.Path.ICON_DOCUMENT.path
            ResourceConstants.FileType.CI_ICON.code -> ResourceConstants.Path.ICON_CI_TYPE.path
            ResourceConstants.FileType.PROCESS.code -> ResourceConstants.Path.PROCESSES.path
            ResourceConstants.FileType.AVATAR.code -> ResourceConstants.Path.AVATAR.path
            ResourceConstants.FileType.AVATAR_TEMP.code -> ResourceConstants.Path.AVATAR_TEMP.path
            else -> ResourceConstants.Path.FILE.path
        }
        return super.getPath(dir)
    }

    /**
     * 외부경로의 이미지 파일을 데이터로 읽기.
     *
     * @param fileFullName 파일 경로와 파일명까지 포함된 전체 이름
     * @return String 화면 태그에서 사용할 수 있는 형태의 data URI Schema
     */
    fun getDataUriSchema(fileFullName: String): String {
        return "data:image/png;base64," + super.getImageData(fileFullName)
    }

    /**
     * 리소스 전체 목록 가져오기 - 페이징
     */
    fun getResourcesPaging(searchCondition: AliceResourceSearchDto): AliceResourcesPagingDto {
        val dir = File(searchCondition.searchPath)
        val depth = if (searchCondition.searchValue.isEmpty()) 1 else Int.MAX_VALUE // 검색어가 없으면 바로 아래 데이터만 조회
        val contentNumPerPage = ResourceConstants.OffsetCount.getOffsetCount(searchCondition.pageType)
        val pagingOffsetStart = ((searchCondition.pageNum - 1) * contentNumPerPage).toInt()
        val condition = super.isAllowedOnlyImageByType(searchCondition.type)
        // 페이징 데이터
        val pagingResult = when (condition) {
            true -> this.getImageAndFolders(dir, depth, pagingOffsetStart, contentNumPerPage.toInt(), searchCondition, condition)
            false -> this.getFileAndFolders(dir, depth, pagingOffsetStart, contentNumPerPage.toInt(), searchCondition, condition)
        }
        // 전체 파일 갯수 카운트
        val totalCountWithoutCondition = when (condition) {
            true -> super.getExternalPathImageCount(dir, searchCondition.searchValue, depth)
            false -> super.getExternalPathCount(dir, searchCondition.searchValue, depth)
        }

        return AliceResourcesPagingDto(
            data = pagingResult,
            paging = AlicePagingData(
                totalCount = pagingResult.size.toLong(),
                totalCountWithoutCondition = totalCountWithoutCondition.toLong(),
                currentPageNum = searchCondition.pageNum,
                totalPageNum = ceil(totalCountWithoutCondition.toDouble() / contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code,
                orderColName = searchCondition.orderColName,
                orderDir = searchCondition.orderDir,
                contentNumPerPage = contentNumPerPage
            )
        )
    }

    /**
     * 리소스 전체 목록 가져오기 - 스크롤
     */
    fun getResourcesScroll(searchCondition: AliceResourceSearchDto): AliceResourcesScrollDto {
        val dir = File(searchCondition.searchPath)
        val depth = if (searchCondition.searchValue.isEmpty()) 1 else Int.MAX_VALUE // 검색어가 없으면 바로 아래 데이터만 조회
        var contentNumPerScroll = ResourceConstants.OffsetCount.getOffsetCount(searchCondition.pageType)
        val scrollOffsetStart = ((searchCondition.pageNum) * contentNumPerScroll).toInt()
        val condition = super.isAllowedOnlyImageByType(searchCondition.type)
        // 스크롤 데이터
        val scrollResult = when (condition) {
            true -> this.getImageAndFolders(dir, depth, scrollOffsetStart, contentNumPerScroll.toInt(), searchCondition, condition)
            false -> this.getFileAndFolders(dir, depth, scrollOffsetStart, contentNumPerScroll.toInt(), searchCondition, condition)
        }
        // 전체 파일 갯수 카운트
        val totalCountWithoutCondition = when (condition) {
            true -> super.getExternalPathImageCount(dir, searchCondition.searchValue, depth)
            false -> super.getExternalPathCount(dir, searchCondition.searchValue, depth)
        }

        return AliceResourcesScrollDto(
            data = scrollResult,
            scroll = AliceScrollData(
                totalCount = scrollResult.size.toLong(),
                totalCountWithoutCondition = totalCountWithoutCondition.toLong(),
                currentScrollNum = searchCondition.pageNum,
                totalScrollNum = ceil(totalCountWithoutCondition.toDouble() / contentNumPerScroll.toDouble()).toLong(),
                contentNumPerScroll = contentNumPerScroll
            )
        )
    }

    /**
     * 이미지 파일만 추출
     *
     * @param dir 파일
     * @param depth 레벨
     * @param start offsetCount - 시작
     * @param offset offsetCount - 종료
     * @param condition 검색조건
     * @param isImageOnly 이미지 파일만 포함할지 여부
     */
    private fun getImageAndFolders(
        dir: File,
        depth: Int,
        start: Int,
        offset: Int,
        condition: AliceResourceSearchDto,
        isImageOnly: Boolean
    ): List<AliceResourceDto> {
        val resources = mutableListOf<AliceResourceDto>()
        run loop@{
            var limit = 0
            dir.walk().maxDepth(depth)
                .filter { it != dir && super.isMatchedInSearch(it.name, condition.searchValue) &&
                    (it.isDirectory || isImage(it.extension)) }
                .sortedBy { it.isFile }
                .forEachIndexed { index, file ->
                    if (index >= start) {
                        if (file.isDirectory) {
                            resources.add(this.getFileDto(file, isImageOnly))
                            limit++
                        } else {
                            resources.add(this.getImageDto(condition.type, file))
                            limit++
                        }
                    }
                    if (limit >= offset) {
                        return@loop
                    }
                }
        }
        return resources
    }

    /**
     * 파일 추출
     *
     * @param dir 파일
     * @param depth 레벨
     * @param start offsetCount - 시작
     * @param offset offsetCount - 종료
     * @param condition 검색조건
     * @param isImageOnly 이미지 파일만 포함할지 여부
     */
    private fun getFileAndFolders(
        dir: File,
        depth: Int,
        start: Int,
        offset: Int,
        condition: AliceResourceSearchDto,
        isImageOnly: Boolean
    ): List<AliceResourceDto> {
        val resources = mutableListOf<AliceResourceDto>()
        run loop@{
            var limit = 0
            dir.walk().maxDepth(depth)
                .filter { it != dir && super.isMatchedInSearch(it.name, condition.searchValue) }
                .sortedBy { it.isFile }
                .forEachIndexed { index, file ->
                    if (index >= start) {
                        if (isImage(file.extension)) {
                            resources.add(this.getImageDto(condition.type, file))
                            limit++
                        } else {
                            resources.add(this.getFileDto(file, isImageOnly))
                            limit++
                        }
                    }
                    if (limit >= offset) {
                        return@loop
                    }
                }
        }
        return resources
    }

    /**
     *  이미지 파일 Dto
     *  이미지 파일 Dto 는 data, width, height 가 존재함
     *
     *  @param type 타입
     *  @param file 파일
     */
    private fun getImageDto(type: String, file: File): AliceResourceDto {
        val bufferedImage = ImageIO.read(file)
        val resizedBufferedImage = super.resizeBufferedImage(bufferedImage, type)
        return AliceResourceDto(
            name = file.name,
            fullPath = file.absolutePath,
            extension = file.extension,
            directoryYn = file.isDirectory,
            imageFileYn = true,
            size = super.humanReadableByteCount(file.length()),
            data = super.encodeToString(resizedBufferedImage, file.extension),
            width = bufferedImage.width,
            height = bufferedImage.height,
            updateDt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(file.lastModified()),
                ZoneId.systemDefault()
            )
        )
    }

    /**
     *  파일 Dto
     *
     *  @param file 파일
     *  @param isImageOnly 이미지 파일만 포함할지 여부
     */
    private fun getFileDto(file: File, isImageOnly: Boolean): AliceResourceDto {
        var totalSize = file.length()
        var count = 0
        if (file.isDirectory) {
            count = if (isImageOnly) {
                super.getExternalPathImageCount(file, "", 1)
            } else {
                super.getExternalPathCount(file, "", 1)
            }
            // 하위 파일만 조회
            totalSize = if (isImageOnly) {
                super.getImageFolderSize(file, 1)
            } else {
                super.getFolderSize(file, 1)
            }
        }
        return AliceResourceDto(
            name = file.name,
            fullPath = file.absolutePath,
            extension = if (file.isDirectory) ResourceConstants.FILE_TYPE_FOLDER else file.extension,
            directoryYn = file.isDirectory,
            imageFileYn = false,
            size = super.humanReadableByteCount(totalSize),
            count = count,
            updateDt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(file.lastModified()),
                ZoneId.systemDefault()
            )
        )
    }

    /**
     * 파일 목록 가져오기
     *
     * @param ownId
     * @param fileDataId
     */
    fun getFiles(ownId: String, fileDataId: String): List<AliceFileOwnMapDto> {
        val aliceFileOwnMaps = mutableListOf<AliceFileOwnMapDto>()
        if (ownId.isNotBlank()) {
            val fileOwnMapEntities =
                aliceFileOwnMapRepository.findFileOwnIdAndFileLocUploaded(ownId, true)
            for (fileOwnMapEntity in fileOwnMapEntities) {
                val fileLocDto = this.getFileLocDto(fileOwnMapEntity.fileLocEntity)
                aliceFileOwnMaps.add(
                    AliceFileOwnMapDto(
                        ownId = fileOwnMapEntity.ownId,
                        fileLocDto = fileLocDto
                    )
                )
            }
        } else if (fileDataId.isNotBlank()) {
            val fileDataIds = fileDataId.split(',')
            for (index in fileDataIds.indices) {
                val fileLocDto = this.getFileLocDto(
                    aliceFileLocRepository.getOne(fileDataIds[index].toLong())
                )
                aliceFileOwnMaps.add(
                    AliceFileOwnMapDto(
                        ownId = "",
                        fileLocDto = fileLocDto
                    )
                )
            }
        }
        return aliceFileOwnMaps
    }

    /**
     *  파일 Loc Dto
     *
     *  @param fileLoc
     */
    private fun getFileLocDto(fileLoc: AliceFileLocEntity): AliceFileLocDto {
        return AliceFileLocDto(
            fileSeq = fileLoc.fileSeq,
            fileOwner = fileLoc.fileOwner,
            fileSize = fileLoc.fileSize,
            originName = fileLoc.originName,
            randomName = fileLoc.randomName,
            sort = fileLoc.sort,
            uploaded = fileLoc.uploaded,
            uploadedLocation = fileLoc.uploadedLocation
        )
    }

    /**
     * 폴더 추가
     * 신규 폴더가 기존 폴더와 이름이 동일 할 경우 폴더명(1) 와 같이 변경된다.
     *
     * @param path 경로
     */
    fun createFolder(path: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var dir: Path = Paths.get(path)
        var file = dir.toFile()
        val oriName = file.name
        var num = 1
        var modifyName: String
        // 기존 경로가 존재할 경우
        while (file.exists()) {
            modifyName = oriName + "(" + num++ + ")"
            dir = Paths.get(file.parentFile.path + File.separator + modifyName)
            file = dir.toFile()
        }

        try {
            Files.createDirectory(dir)
        } catch (e: IOException) {
            // directory does not exist
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 파일 및 폴더명 수정
     *
     * @param originPath 기존 폴더 경로
     * @param modifyPath 수정 폴더 경로
     */
    fun renameFileAndFolder(originPath: String, modifyPath: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val originFile = Paths.get(originPath).toFile()
        val modifyFile = Paths.get(modifyPath).toFile()

        // 원본 폴더를 찾을 수 없을 경우
        if (!originFile.exists()) {
            return ZResponse(
                status = ZResponseConstants.STATUS.ERROR_FAIL.code
            )
        }

        // 기존 폴더명이 존재하는 경우
        if (!modifyFile.exists()) {
            try {
                originFile.renameTo(modifyFile)
            } catch (e: IOException) {
                // directory does not exist
                status = ZResponseConstants.STATUS.ERROR_FAIL
            }
        } else {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 폴더 삭제 (하위 폴더 및 파일 모두 삭제)
     *
     * @param path 경로
     */
    fun deleteFolder(path: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val deleteFile = Paths.get(path).toFile()
        if (!deleteFile.deleteRecursively()) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 파일 조회
     */
    fun getFile(type: String, path: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var imageDto: AliceResourceDto? = null
        try {
            val dir = Paths.get(path)
            if (Files.exists(dir)) {
                imageDto = this.getImageDto(type, dir.toFile())
            } else {
                status = ZResponseConstants.STATUS.ERROR_FAIL
            }
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }

        return ZResponse(
            status = status.code,
            data = imageDto
        )
    }

    /**
     * 파일 업로드
     */
    fun uploadFiles(type: String, path: String, multipartFiles: List<MultipartFile>, fileName: String?): ZResponse {
        return when (type) {
            ResourceConstants.FileType.AVATAR.code -> {
                val dir = super.getPath(ResourceConstants.Path.AVATAR_TEMP.path)
                this.uploadAvatar(dir.toString(), multipartFiles, fileName)
            }
            ResourceConstants.FileType.PROCESS.code -> {
                val dir = super.getPath(ResourceConstants.Path.PROCESSES.path)
                this.uploadDirect(type, dir.toString(), multipartFiles)
            }
            ResourceConstants.FileType.FILE.code -> {
                this.uploadDirect(type, path, multipartFiles)
            }
            else -> {
                this.uploadTemporary(type, multipartFiles)
            }
        }
    }

    /**
     *  아바타 파일을 특정 경로에 업로드한다. 단, 이름을 변경하여 저장된다.
     *
     * @param path 경로
     * @param multipartFiles 파일
     * @param fileName 아바타 파일명
     */
    private fun uploadAvatar(path: String, multipartFiles: List<MultipartFile>, fileName: String?): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        try {
            multipartFiles.forEach {
                val filePath = when (fileName) {
                    null -> {
                        Paths.get(path + File.separator + it.originalFilename)
                    }
                    else -> {
                        Paths.get(path + File.separator + fileName)
                    }
                }
                val file = filePath.toFile()
                it.transferTo(file)
            }
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     *  파일을 특정 경로에 업로드한다.
     *
     * @param type 타입
     * @param path 경로
     * @param multipartFiles 파일
     */
    private fun uploadDirect(type: String, path: String, multipartFiles: List<MultipartFile>): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val extensions = this.getAllowedExtensions(type)
        try {
            multipartFiles.forEach {
                val file = super.getUploadFileWithModifyName(path, it)
                // 확장자 체크
                if (extensions.contains(file.extension.toUpperCase())) {
                    it.transferTo(file)
                }
            }
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 파일을 임시 경로에 업로드한다.
     *
     * @param type 타입
     * @param multipartFiles 파일
     */
    private fun uploadTemporary(type: String, multipartFiles: List<MultipartFile>): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val extensions = this.getAllowedExtensions(type)
        val fileSeq = mutableSetOf<Long>()
        try {
            multipartFiles.forEach {
                val fileName = super.getRandomFilename()
                val tempPath = super.getUploadFilePathWithNow(ResourceConstants.Path.TEMP.path, fileName)
                val filePath = super.getUploadFilePathWithNow(ResourceConstants.Path.UPLOAD.path, fileName)
                val fileNameExtension = File(it.originalFilename!!).extension.toUpperCase()
                val file = tempPath.toFile()
                // 확장자 체크
                if (extensions.contains(fileNameExtension)) {
                    it.transferTo(file)

                    val aliceFileLocEntity = AliceFileLocEntity(
                        fileSeq = 0,
                        fileOwner = currentSessionUser.getUserKey(),
                        uploaded = false,
                        uploadedLocation = filePath.parent.toString(),
                        randomName = fileName,
                        originName = it.originalFilename,
                        fileSize = it.size,
                        sort = 0
                    )
                    aliceFileLocRepository.save(aliceFileLocEntity)
                    fileSeq.add(aliceFileLocEntity.fileSeq)
                }
            }
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code,
            data = fileSeq
        )
    }

    /**
     * 파일 다운로드
     *
     * @param type 타입
     * @param path 경로
     */
    fun downloadFile(type: String, path: String): ResponseEntity<InputStreamResource> {
        val dir = when (type) {
            ResourceConstants.FileType.FILE.code -> {
                Paths.get(path)
            }
            else -> {
                val fileLoc = aliceFileLocRepository.getOne(path.toLong())
                Paths.get(fileLoc.uploadedLocation + File.separator + fileLoc.randomName)
            }
        }
        val file = dir.toFile()
        val resource = FileSystemResource(dir)
        if (file.exists() && resource.exists()) {
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Tika().detect(resource.path)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${file.name}\"")
                .body(InputStreamResource(resource.inputStream))
        } else {
            throw AliceException(AliceErrorConstants.ERR, "File not found: ${file.name}")
        }
    }

    /**
     * 파일 삭제
     * @param type 타입
     * @param path 경로
     */
    fun deleteFile(type: String, path: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (type) {
            ResourceConstants.FileType.FILE.code -> {
                if (!this.deleteDirect(path)) {
                    status = ZResponseConstants.STATUS.ERROR_FAIL
                }
            }
            else -> {
                // TODO: CMDB 아이콘
            }
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     *  특정 경로에 파일을 삭제한다.
     * @param path 경로
     */
    private fun deleteDirect(path: String): Boolean {
        val deleteFile = Paths.get(path).toFile()
        return deleteFile.delete()
    }

    /**
     * 시퀀스 번호로 파일을 삭제한다.
     *
     * @param seq
     */
    @Transactional
    fun deleteBySeq(seq: Long) {
        this.deleteFileLoc(aliceFileOwnMapRepository.findByFileLocEntityFileSeq(seq).fileLocEntity)
    }

    /**
     * 소유 번호로 파일을 삭제한다.
     *
     * @param ownId
     */
    @Transactional
    fun deleteByOwnId(ownId: String) {
        val fileOwnMapEntityList = aliceFileOwnMapRepository.findAllByOwnId(ownId)

        for (fileOwnMapEntity in fileOwnMapEntityList) {
            this.deleteFileLoc(fileOwnMapEntity.fileLocEntity)
        }
    }

    /**
     * 파일을 삭제한다.
     *
     * @param aliceFileLocEntity
     */
    private fun deleteFileLoc(aliceFileLocEntity: AliceFileLocEntity) {
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
     * 임시 경로에 업로드된 파일을 실제 경로에 업로드하고 파일관리테이블에 uploaded 상태를 true 변경하여 조회가 가능하도록 한다.
     * 첨부 파일 및 파일 컴포넌트에서 사용
     * @param data
     */
    fun setUploadFileLoc(data: Any) {
        var fileLocEntity: AliceFileLocEntity
        when (data) {
            is AliceFileDto -> {
                for (fileSeq in data.fileSeq.orEmpty()) {
                    fileLocEntity = aliceFileLocRepository.getOne(fileSeq)
                    val originPath = Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName)
                    val modifyPath = super.getUploadFilePathWithNow(ResourceConstants.Path.TEMP.path, fileLocEntity.randomName)
                    this.moveFileLoc(originPath, modifyPath, false)
                    fileLocEntity.uploaded = true

                    try {
                        val fileOwnMapEntity = AliceFileOwnMapEntity(data.ownId, fileLocEntity)
                        aliceFileOwnMapRepository.save(fileOwnMapEntity)
                    } catch (e: Exception) {
                        logger.error("{}", e.message)
                        this.moveFileLoc(originPath, modifyPath, true)
                        throw AliceException(AliceErrorConstants.ERR, e.message)
                    }
                }

                for (delFileSeq in data.delFileSeq.orEmpty()) {
                    this.deleteBySeq(delFileSeq)
                }
            }
            is String -> {
                val fileDataIds = data.split(',')
                for (fileDataId in fileDataIds) {
                    fileLocEntity = aliceFileLocRepository.getOne(fileDataId.toLong())
                    val originPath = Paths.get(fileLocEntity.uploadedLocation + File.separator + fileLocEntity.randomName)
                    val modifyPath = super.getUploadFilePathWithNow(ResourceConstants.Path.TEMP.path, fileLocEntity.randomName)
                    this.moveFileLoc(originPath, modifyPath, false)
                    fileLocEntity.uploaded = true

                    try {
                        aliceFileLocRepository.save(fileLocEntity)
                    } catch (e: Exception) {
                        logger.error("{}", e.message)
                        this.moveFileLoc(originPath, modifyPath, true)
                        throw AliceException(AliceErrorConstants.ERR, e.message)
                    }
                }
            }
        }
    }

    /**
     * 파일을 특정 경로로 이동 및 복원한다.
     *
     * @param origin
     * @param modify
     * @param isRollback
     */
    private fun moveFileLoc(origin: Path, modify: Path, isRollback: Boolean) {
        if (isRollback) {
            Files.move(origin, modify, StandardCopyOption.REPLACE_EXISTING)
        } else {
            Files.move(modify, origin, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    /**
     * 내부 경로에 있는 이미지 경로 리스트 가져오기
     *
     */
    // 2021-03-25 Jung Hee Chan
    // 기존에 CI Type 및 신청서용 아이콘이 Jar 파일 내부에 포함되어 있어서 만들었음.
    // 현재 사용되는 곳이 없으나 남겨둠.
    fun getInternalImageDataList(
        type: String,
        dir: String,
        searchValue: String,
        currentOffset: Int
    ): AliceFileDetailListReturnDto {
        var fileList = mutableListOf<Path>()
        val resourceURL = javaClass.classLoader.getResource(dir)
        val imageExtensions = getAllowedExtensions(ResourceConstants.FileType.IMAGE.code)
        // 2021-03-17 Jung Hee Chan
        // 내부 경로에 위치한 이미지 파일은 경우 Jar 파일 내부에 있을 수 있어 파일시스템을 이용해서 Path 리스트를 취득.
        resourceURL?.let {
            FileSystems.newFileSystem(
                resourceURL.toURI(),
                mutableMapOf<String, String>()
            ).use { fs ->
                fileList = Files.walk(fs.getPath(classPathInJar + dir))
                    .filter { path: Path ->
                        val fileName = path.fileName.toString()

                        Files.isRegularFile(path) && // 파일 타입만 조회
                            // 허용 확장자 체크
                            (imageExtensions.indexOf(
                                fileName.substring(
                                    (fileName.lastIndexOf(".") + 1),
                                    fileName.length
                                ).toUpperCase()
                            ) > -1) &&
                            fileName.matches(".*$searchValue.*".toRegex()) // 검색조건 적용
                    }
                    .collect(Collectors.toList())
            }
        }

        var startIndex = 0
        if (currentOffset != -1) { // -1인 경우는 전체 조회
            startIndex = currentOffset
        }

        // 2021-03-17 Jung Hee Chan
        // 내부 경로에 위치한 이미지 파일은 Jar 파일 내부에 있을 수 있어 File 이 아닌 Stream 형태로 읽어옴.
        // 이때 기존 File 방식과 달리 확장자, 사이즈, 수정일자등 데이터를 가져올 수 없음. --> 이미지 관리에 대한 개선의 여지가 있음.
        val imageList = mutableListOf<AliceFileDetailDto>()
        for (i in startIndex until getImageListEndIndex(currentOffset, fileList.size)) {
            var filePathInJAR = fileList[i].toString()
            val fileName = fileList[i].fileName.toString()

            // 윈도우즈 환경인 경우 제일 앞 / 제거가 필요.
            if (filePathInJAR.startsWith("/")) {
                filePathInJAR = filePathInJAR.substring(1, filePathInJAR.length)
            }

            val fileInputStream = javaClass.classLoader.getResourceAsStream(filePathInJAR)
            val bufferedImage = ImageIO.read(fileInputStream)
            val resizedBufferedImage = resizeBufferedImage(bufferedImage, type)
            imageList.add(
                AliceFileDetailDto(
                    name = fileName,
                    extension = "",
                    fullpath = "",
                    size = "",
                    data = super.encodeToString(
                        resizedBufferedImage,
                        fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length).toLowerCase()
                    ),
                    width = bufferedImage.width,
                    height = bufferedImage.height,
                    updateDt = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(0),
                        ZoneId.systemDefault()
                    )
                )
            )
        }
        return AliceFileDetailListReturnDto(
            data = imageList,
            totalCount = fileList.size.toLong()
        )
    }

    /**
     * 조회하는 이미지 리스트의 갯수를 계산
     */
    fun getImageListEndIndex(currentOffset: Int, maxSize: Int): Int {
        var endIndex: Int
        // currentOffset 값이 현재 인덱스의 값을 나타내면서 전체 조회인지 여부까지 포함해서 이중적으로 사용되고 있음.
        if (currentOffset == -1) {
            endIndex = maxSize // 전체 목록 조회인 경우
        } else {
            endIndex = (currentOffset + ItsmConstants.IMAGE_OFFSET_COUNT).toInt()
            if (maxSize < endIndex) endIndex = maxSize
        }
        return endIndex
    }

    /**
     * 썸네일 공통 모달의 사이즈를 작게 볼지 여부
     * 현재는 CMDB CI 아이콘만 해당되나 추후 늘어날 수 있음
     */
    fun isThumbnailSmall(type: String): Boolean {
        return when (type) {
            ResourceConstants.FileType.CI_ICON.code -> true
            else -> false
        }
    }

    /**
     * 파일 및 폴더명 수정
     *
     * @param originPath 기존 폴더 경로
     * @param modifyPath 수정 폴더 경로
     */
    fun moveFile(originPath: String, modifyPath: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val originFile = Paths.get(originPath).toFile()
        var modifyFile = Paths.get(modifyPath).toFile()

        // 원본 파일를 찾을 수 없을 경우
        if (!originFile.exists()) {
            return ZResponse(
                status = ZResponseConstants.STATUS.ERROR_FAIL.code
            )
        }

        try {
            Files.move(originFile.toPath(), modifyFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }

        return ZResponse(
            status = status.code
        )
    }
}
