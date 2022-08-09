/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ciIcon.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.resourceManager.dto.AliceResourceDto
import co.brainz.framework.resourceManager.dto.AliceResourceSearchDto
import co.brainz.framework.resourceManager.dto.AliceResourcesPagingDto
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.AliceResourceUtil
import co.brainz.itsm.cmdb.ciIcon.entity.CIIconEntity
import co.brainz.itsm.cmdb.ciIcon.repository.CIIconRepository
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.imageio.ImageIO
import kotlin.math.ceil
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class CIIconService(
    private val ciIconRepo: CIIconRepository,
    private val aliceResourceProvider: AliceResourceProvider,
    environment: Environment
) : AliceResourceUtil(environment) {

    /**
     * CMDB CI 아이콘 목록 가져오기 - 페이징
     */
    fun getCIIconsPaging(searchCondition: AliceResourceSearchDto): AliceResourcesPagingDto {
        val dir = File(searchCondition.searchPath)
        val depth = if (searchCondition.searchValue.isEmpty()) 1 else Int.MAX_VALUE // 검색어가 없으면 바로 아래 데이터만 조회
        val contentNumPerPage = ResourceConstants.OffsetCount.getOffsetCount(searchCondition.pageType + '-' + searchCondition.type)
        val pagingOffsetStart = ((searchCondition.pageNum - 1) * contentNumPerPage).toInt()

        // 아이콘 목록
        val icons = ciIconRepo.findAll()
        // 아이콘은 이미지만 가능
        val pagingResult = this.getIconAndFolders(dir, depth, pagingOffsetStart, contentNumPerPage.toInt(), searchCondition, icons)
        // 전체 파일 갯수 카운트
        val totalCountWithoutCondition = super.getExternalPathImageCount(dir, searchCondition.searchValue, depth)

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
     * 아이콘 파일 및 폴더 추출
     *
     * @param dir 파일
     * @param depth 레벨
     * @param start offsetCount - 시작
     * @param offset offsetCount - 종료
     * @param condition 검색조건
     * @param icons db에 등록된 아이콘 목록
     */
    private fun getIconAndFolders(
        dir: File,
        depth: Int,
        start: Int,
        offset: Int,
        condition: AliceResourceSearchDto,
        icons: MutableList<CIIconEntity>
    ): List<AliceResourceDto> {
        val resources = mutableListOf<AliceResourceDto>()
        // 편집 불가능한 아이콘 추출
        val unEditableIcons = icons.filter { !it.editable }

        run loop@{
            var limit = 0
            dir.walk().maxDepth(depth)
                .filter { it != dir && super.isMatchedInSearch(it.name, condition.searchValue) &&
                    (it.isDirectory || isImage(it.extension)) }
                .sortedBy { it.isFile }
                .forEachIndexed { index, file ->
                    if (index >= start) {
                        if (file.isDirectory) {
                            // 하위 폴더, 파일이 editable 일 경우 폴더도 삭제 불가능
                            resources.add(
                                this.getFolderDto(file, this.isEditableAllInFolder(file, unEditableIcons))
                            )
                            limit++
                        } else {
                            val icon = icons.firstOrNull {
                                val path = Paths.get(it.uploadedLocation + File.separator + it.fileName + "." + it.fileNameExtension)
                                file.absolutePath == path.toAbsolutePath().toString()
                            }
                            resources.add(
                                this.getImageDto(condition.type, file, icon)
                            )
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
     *  @param icon 아이콘 데이터
     */
    private fun getImageDto(type: String, file: File, icon: CIIconEntity?): AliceResourceDto {
        val bufferedImage = ImageIO.read(file)
        val resizedBufferedImage = super.resizeBufferedImage(bufferedImage, type)
        var updateDt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(file.lastModified()),
            ZoneId.systemDefault()
        )
        if (icon != null) {
            updateDt = icon.updateDt.let { icon.createDt }
        }
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
            updateDt = updateDt,
            editable = icon?.editable ?: true
        )
    }

    /**
     *  폴더 Dto
     *
     *  @param file 파일
     *  @param editable 편집 가능 여부
     */
    private fun getFolderDto(file: File, editable: Boolean): AliceResourceDto {
        val totalSize = if (file.isDirectory) super.getImageFolderSize(file, 1) else file.length()
        return AliceResourceDto(
            name = file.name,
            fullPath = file.absolutePath,
            extension = if (file.isDirectory) ResourceConstants.FILE_TYPE_FOLDER else file.extension,
            directoryYn = file.isDirectory,
            imageFileYn = false,
            size = super.humanReadableByteCount(totalSize),
            count = if (file.isDirectory) super.getExternalPathImageCount(file, "", 1) else 0,
            updateDt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(file.lastModified()),
                ZoneId.systemDefault()
            ),
            editable = editable
        )
    }

    /**
     * 폴더 내부의 파일들이 모두 수정 가능한지 여부
     * 수정 불가능한 파일이 포함될 경우 폴더는 삭제 불가능 함
     */
    private fun isEditableAllInFolder(
        dir: File,
        icons: List<CIIconEntity>
    ): Boolean {
        var editable = true
        run loop@{
            dir.walk()
                .filter { !it.isDirectory }
                .forEach { file ->
                    val icon = icons.firstOrNull {
                        val path = Paths.get(it.uploadedLocation + File.separator + it.fileName + "." + it.fileNameExtension)
                        file.absolutePath == path.toAbsolutePath().toString()
                    }
                    if (icon !== null) {
                        editable = false
                        return@loop
                    }
                }
        }
        return editable
    }

    /**
     * 파일 및 폴더명 수정
     *
     * @param originPath 기존 폴더 경로
     * @param modifyPath 수정 폴더 경로
     */
    @Transactional
    fun renameFolder(originPath: String, modifyPath: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val originFile = Paths.get(originPath).toFile()
        val modifyFile = Paths.get(modifyPath).toFile()
        val icons = ciIconRepo.findAll()

        // 원본 폴더를 찾을 수 없을 경우
        if (!originFile.exists()) {
            return ZResponse(
                status = ZResponseConstants.STATUS.ERROR_FAIL.code
            )
        }
        val iconEntities = mutableListOf<CIIconEntity>()
        // 기존 폴더명이 존재하는 경우
        if (!modifyFile.exists()) {
            try {
                // 해당 폴더에 속한 아이콘 파일들의 경로 변경
                originFile.walk()
                    .filter { it.isDirectory }
                    .forEach { file ->
                        icons.forEach { icon ->
                            if (icon.uploadedLocation == file.path) {
                                val modifyLocation = icon.uploadedLocation?.replace(originFile.path, "")
                                icon.uploadedLocation = modifyFile.path + modifyLocation
                                iconEntities.add(icon)
                            }
                        }
                    }
                ciIconRepo.saveAll(iconEntities)

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
    @Transactional
    fun deleteFolder(path: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val deleteFile = Paths.get(path).toFile()

        if (!deleteFile.deleteRecursively()) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }

        if (status != ZResponseConstants.STATUS.ERROR_FAIL) {
            val icons = ciIconRepo.findAll()
            icons.forEach { icon ->
                if (icon.uploadedLocation != null && icon.uploadedLocation!!.matches(".*${deleteFile.name}.*".toRegex())) {
                    ciIconRepo.delete(icon)
                }
            }
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * CMDB CI Icon 업로드
     * @param type 타입
     * @param path 경로
     * @param multipartFiles 파일목록
     */
    @Transactional
    fun uploadCIIcons(type: String, path: String, multipartFiles: List<MultipartFile>): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val extensions = aliceResourceProvider.getAllowedExtensions(type)
        try {
            multipartFiles.forEach {
                val file = super.getUploadFileWithModifyName(path, it)
                // 확장자 체크
                if (extensions.contains(file.extension.toUpperCase())) {
                    it.transferTo(file)

                    // 저장
                    val iconFileEntity = CIIconEntity(
                        fileSeq = 0,
                        fileName = file.nameWithoutExtension,
                        fileNameExtension = file.extension.toLowerCase(),
                        uploadedLocation = file.parent.toString(),
                        editable = true
                    )
                    ciIconRepo.save(iconFileEntity)
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
     * CMDB CI Icon 파일명 수정
     */
    @Transactional
    fun renameFile(originPath: String, modifyPath: String): ZResponse {
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
                val iconEntities = mutableListOf<CIIconEntity>()
                val ciIcons = ciIconRepo.findByFileNameAndFileNameExtension(
                    originFile.nameWithoutExtension,
                    originFile.extension.toLowerCase()
                )
                ciIcons.forEach { icon ->
                    if (icon.uploadedLocation == originFile.parent.toString()) {
                        icon.fileName = modifyFile.nameWithoutExtension
                        iconEntities.add(icon)
                    }
                }
                ciIconRepo.saveAll(iconEntities)
                // 파일 수정
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
     * CMDB CI Icon 파일 삭제
     */
    @Transactional
    fun deleteCIIcon(path: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val deleteFile = Paths.get(path).toFile()
        try {
            val ciIcons = ciIconRepo.findByFileNameAndFileNameExtension(
                deleteFile.nameWithoutExtension,
                deleteFile.extension.toLowerCase()
            )
            ciIcons.forEach { icon ->
                if (icon.uploadedLocation == deleteFile.parent.toString()) {
                    ciIconRepo.delete(icon)
                }
            }
            // 파일 삭제
            deleteFile.delete()
        } catch (e: IOException) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
            e.printStackTrace()
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * CMDB CI Icon 파일 이동
     */
    @Transactional
    fun moveCIIcon(originPath: String, modifyPath: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val originFile = Paths.get(originPath).toFile()
        var modifyFile = Paths.get(modifyPath).toFile()
        val icons = ciIconRepo.findAll()

        // 원본 파일를 찾을 수 없을 경우
        if (!originFile.exists()) {
            return ZResponse(
                status = ZResponseConstants.STATUS.ERROR_FAIL.code
            )
        }

        val iconEntities = mutableListOf<CIIconEntity>()
        try {
            // 해당 폴더에 속한 아이콘 파일들의 경로 변경
            if (originFile.isDirectory) {
                originFile.walk()
                    .filter { it.isDirectory }
                    .forEach { file ->
                        icons.forEach { icon ->
                            if (icon.uploadedLocation == file.path) {
                                val modifyLocation = icon.uploadedLocation?.replace(originFile.path, "")
                                icon.uploadedLocation = modifyFile.path + modifyLocation
                                iconEntities.add(icon)
                            }
                        }
                    }
            } else {
                val icon = icons.firstOrNull {
                    val path = Paths.get(it.uploadedLocation + File.separator + it.fileName + "." + it.fileNameExtension)
                    originFile.absolutePath == path.toAbsolutePath().toString()
                }
                if (icon !== null) {
                    icon.uploadedLocation = modifyFile.parent.toString()
                    iconEntities.add(icon)
                }
            }
            ciIconRepo.saveAll(iconEntities)

            Files.move(originFile.toPath(), modifyFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }

        return ZResponse(
            status = status.code
        )
    }
}
