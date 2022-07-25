/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.resourceManager.constants

import java.io.File

object ResourceConstants {

    /**
     * 폴더
     */
    const val FILE_TYPE_FOLDER = "folder"

    /**
     * 파일명 - 날짜 포맷
     */
    const val FILE_NAME_FORMAT = "yyyyMMdd"

    /**
     * 이미지 확장자 목록
     */
    val IMAGE_EXTENSIONS = listOf("PNG", "GIF", "JPG", "JPEG")

    /**
     * 썸네일 이미지 사이즈
     */
    const val THUMBNAIL_WIDTH = 700
    const val THUMBNAIL_WIDTH_ICON = 100
    
    /**
     * 경로.
     */
    enum class Path(val path: String) {
        ICON_DOCUMENT("icons${File.separator}document"),
        ICON_CI_TYPE("icons${File.separator}ciType"),
        IMAGE("images"),
        FILE("shared"),
        PROCESSES("processes"),
        UPLOAD("uploadRoot"),
        TEMP("temp"),
        AVATAR_TEMP("avatar${File.separator}temp"),
        AVATAR("avatar")
    }

    /**
     * 페이지 타입
     */
    enum class PageType(val code: String) {
        LIST("list"),
        THUMBNAIL("thumbnail"),
        THUMBNAIL_CI_ICON("thumbnail-ci-icon"),
        THUMBNAIL_MODAL("modal")
    }

    /**
     * 파일 타입.
     */
    enum class FileType(val code: String) {
        IMAGE("image"),
        ICON("icon"),
        CI_ICON("cmdb-icon"),
        FILE("file"),
        PROCESS("process"),
        AVATAR_TEMP("avatar-temp"),
        AVATAR("avatar")
    }

    /**
     * 페이징 오프셋
     */
    const val OFFSET_LIST = 14L
    const val OFFSET_THUMBNAIL = 18L
    const val OFFSET_THUMBNAIL_ICON = 30L
    const val OFFSET_MODAL = 12L

    /**
     * limit 값
     */
    enum class OffsetCount(val code: String, val value: Long) {
        LIST("list", OFFSET_LIST),
        THUMBNAIL("thumbnail", OFFSET_THUMBNAIL),
        THUMBNAIL_CI_ICON("thumbnail-ci-icon", OFFSET_THUMBNAIL_ICON),
        THUMBNAIL_MODAL("modal", OFFSET_MODAL);

        companion object {
            fun getOffsetCount(code: String): Long {
                return values().first { it.code == code }.value
            }
        }
    }
}
