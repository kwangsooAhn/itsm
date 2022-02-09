/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.fileTransaction.constants

import java.io.File

object FileConstants {

    /**
     * 파일 타입.
     */
    enum class Type(val code: String) {
        IMAGE("image"),
        ICON("icon"),
        ICON_CI_TYPE("cmdb-icon"),
        FILE("file")
    }

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
}
