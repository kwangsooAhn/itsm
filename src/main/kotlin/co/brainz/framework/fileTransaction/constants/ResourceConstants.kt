/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.fileTransaction.constants

import java.io.File

object ResourceConstants {

    /**
     * 폴더
     */
    const val FILE_TYPE_FOLDER = "folder"

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
        TEMP("temp")
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
        FILE("file")
    }

    /**
     * limit 값
     */
    enum class OffsetCount(val code: String, val value: Long) {
        LIST("list", 15L),
        THUMBNAIL("thumbnail", 18L),
        THUMBNAIL_CI_ICON("thumbnail-ci-icon", 30L),
        THUMBNAIL_MODAL("modal", 12L);

        companion object {
            fun getOffsetCount(code: String): Long {
                return values().first { it.code == code }.value
            }
        }
    }
}
