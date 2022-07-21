package co.brainz.itsm.resource.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.constants.ResourceConstants
import co.brainz.framework.util.AliceFileUtil
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.resource.dto.ResourceDto
import co.brainz.itsm.resource.dto.ResourceSearchDto
import co.brainz.itsm.resource.dto.ResourcesPagingDto
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.imageio.ImageIO
import kotlin.math.ceil
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
class ResourceService(
    environment: Environment
): AliceFileUtil(environment) {

    /**
     * 기본 경로 조회
     */
    fun getResourceBasePath(type: String): String {
        return getExternalPath(type).toString()
    }

    /**
     * 리소스 전체 목록 가져오기
     */
    fun getResources(searchCondition: ResourceSearchDto): ResourcesPagingDto {
        val dir = File(searchCondition.searchPath)
        val depth = if (searchCondition.searchValue.isEmpty()) 1 else Int.MAX_VALUE // 검색어가 없으면 바로 아래 데이터만 조회
        val pagingResult = when(isAllowedOnlyImageByType(searchCondition.type)) {
            true -> getImageAndFolders(dir, depth, searchCondition.searchValue, searchCondition.type)
            false -> getFileAndFolders(dir, depth, searchCondition.searchValue, searchCondition.type)
        }
        // 타입에 따라서 항목을 가져오는 갯수가 다르게 설정
        val contentNumPerPage = ResourceConstants.OffsetCount.getOffsetCount(searchCondition.pageType)
        val pagingOffset = (searchCondition.pageNum * contentNumPerPage)
        val result: List<ResourceDto> = pagingResult.take(pagingOffset.toInt())

        return ResourcesPagingDto(
            data = result,
            paging = AlicePagingData(
                totalCount = result.size.toLong(),
                totalCountWithoutCondition = pagingResult.size.toLong(),
                currentPageNum = searchCondition.pageNum,
                totalPageNum = ceil(pagingResult.size.toDouble() / contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code,
                orderColName = searchCondition.orderColName,
                orderDir = searchCondition.orderDir
            )
        )
    }

    /**
     * 이미지 파일만 추출
     * @param dir 파일
     * @param depth 레벨
     * @param search 검색어
     * @param type 타입
     */
    private fun getImageAndFolders(dir: File, depth: Int, search: String, type: String): List<ResourceDto> {
        val resources = mutableListOf<ResourceDto>()
        dir.walk().maxDepth(depth).forEachIndexed { index, file ->
            if (index > 0 && isMatchedInSearch(file.name, search)) {
                if (file.isDirectory) {
                    resources.add(getFileDto(file))
                } else {
                    if (isImage(file.extension)) {
                        resources.add(getImageDto(type, file))
                    }
                }
            }
        }
        return resources
    }

    /**
     * 파일 추출
     * @param dir 파일
     * @param depth 레벨
     * @param search 검색어
     * @param type 타입
     */
    private fun getFileAndFolders(dir: File, depth: Int, search: String, type: String): List<ResourceDto> {
        val resources = mutableListOf<ResourceDto>()
        dir.walk().maxDepth(depth).forEachIndexed { index, file ->
            if (index > 0 && isMatchedInSearch(file.name, search)) {
                if (isImage(file.extension)) {
                    resources.add(getImageDto(type, file))
                } else {
                    resources.add(getFileDto(file))
                }
            }
        }
        return resources
    }

    /**
     *  이미지 파일 Dto
     *  이미지 파일 Dto 는 data, width, height 가 존재함
     *  @param type 타입
     *  @param file 파일
     */
    private fun getImageDto(type: String, file: File): ResourceDto {
        val bufferedImage = ImageIO.read(file)
        val resizedBufferedImage = resizeBufferedImage(bufferedImage, type)
        return ResourceDto(
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
     *  @param file 파일
     */
    private fun getFileDto(file: File): ResourceDto {
        return ResourceDto(
            name = file.name,
            fullPath = file.absolutePath,
            extension = if (file.isDirectory) ResourceConstants.FILE_TYPE_FOLDER else file.extension,
            directoryYn = file.isDirectory,
            imageFileYn = false,
            size = super.humanReadableByteCount(file.length()),
            updateDt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(file.lastModified()),
                ZoneId.systemDefault()
            )
        )
    }

}
