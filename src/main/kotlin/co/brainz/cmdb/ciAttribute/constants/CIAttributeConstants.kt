/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciAttribute.constants

object CIAttributeConstants {

    enum class Type(val code: String) {
        ICON("icon"),
        DATE("date"),
        DATE_TIME("datetime"),
        HIDDEN("hidden"),
        STRING("string"),
        RADIO("radio")
    }

    enum class Column(val value: String) {
        CI_ICON("ciIconData"),
        CI_ID("ciId"),
        CI_NO("ciNo"),
        CI_TYPE_NAME("typeName"),
        CI_NAME("ciName"),
        CI_DESC("ciDesc")
    }

    enum class Width(val width: String) {
        CI_ICON("20px"),
        CI_ID("0px"),
        CI_NO("180px"),
        CI_TYPE_NAME("120px"),
        CI_NAME("150px"),
        CI_DESC("245px")
    }
}
