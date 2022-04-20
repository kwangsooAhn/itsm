/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciClass.constants

object CIClassConstants {
    const val CI_CLASS_ROOT_ID = "root"

    enum class AttributeType(val code: String) {
        CUSTOM_CODE("custom-code"),
        DATE("date"),
        DATE_TIME("datetime"),
        USER_SEARCH("userSearch")
    }
}
