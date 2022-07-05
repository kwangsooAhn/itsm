/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.fileTransaction.provider

import co.brainz.framework.fileTransaction.constants.FileConstants
import co.brainz.framework.util.AliceFileUtil
import co.brainz.itsm.resource.dto.ResourceDto
import java.nio.file.Path
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class AliceResourceProvider(
    environment: Environment
) : AliceFileUtil(environment) {

    private val imageExtensions = listOf("png", "gif", "jpg", "jpeg")

    /**
     * 외부 경로에 있는 리소스 정보를 조회
     * 
     * @param type CMDB 아이콘|리소스 관리|업무흐름 아이콘 구분
     * @param searchValue 검색어. 검색어에 따라 일치하는 파일명/폴더명 조회
     */
    fun getExternalResources(
        type: String,
        searchValue: String
    ): List<ResourceDto> {
        val dir = getExternalDir(type)
        val dirPath = super.getPath(dir)
        val depth = if (searchValue.isEmpty())  2 else Int.MAX_VALUE // 검색어가 없으면 바로 아래 데이터만 조회

        return if (isAllowedOnlyImage(type)) getImages(dirPath, depth) else getFiles(dirPath, depth)
    }

    /**
     * 외부 경로 조회
     */
    fun getExternalDir(type: String): String {
        return when (type) {
            FileConstants.Type.ICON.code -> FileConstants.Path.ICON_DOCUMENT.path
            FileConstants.Type.ICON_CI_TYPE.code -> FileConstants.Path.ICON_CI_TYPE.path
            else -> FileConstants.Path.FILE.path
        }
    }

    /**
     * 파일 타입에 따라 이미지 파일만 가져올지 여부를 판단
     */
    fun isAllowedOnlyImage(type: String): Boolean {
        return when(type) {
            FileConstants.Type.ICON.code,
            FileConstants.Type.ICON_CI_TYPE.code,
            FileConstants.Type.IMAGE.code -> true
            else -> false
        }
    }

    /**
     * 이미지 파일 여부
     * @param extension 확장자
     */
    fun isImage(extension: String): Boolean {
        return imageExtensions.indexOf(extension.toLowerCase()) > -1
    }

    /**
     * 이미지 파일만 추출
     */
    fun getImages(path: Path, depth: Int): List<ResourceDto> {
        val resources = mutableListOf<ResourceDto>()
        // TODO: 검색
        path.toFile().walk().maxDepth(depth).forEach {
            if (isImage(it.extension)) {
                resources.add(
                    ResourceDto(
                        name = it.name,
                        fullPath = it.absolutePath,
                        extension = it.extension,
                        isDirectory = it.isDirectory,
                        size = super.humanReadableByteCount(it.length())
                    )
                )
            }
        }
        return resources
    }

    /**
     * 파일 추출
     */
    fun getFiles(path: Path, depth: Int): List<ResourceDto> {
        val resources = mutableListOf<ResourceDto>()
        // TODO: 검색
        path.toFile().walk().maxDepth(depth).forEach {
            resources.add(
                ResourceDto(
                    name = it.name,
                    fullPath = it.absolutePath,
                    extension = it.extension,
                    isDirectory = it.isDirectory,
                    size = super.humanReadableByteCount(it.length())
                )
            )
        }
        return resources
    }
}
