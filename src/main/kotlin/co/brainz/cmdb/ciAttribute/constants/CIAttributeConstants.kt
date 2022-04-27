/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciAttribute.constants

object CIAttributeConstants {

    enum class Type(val code: String) {
        ICON("icon"),
        CHECKBOX("checkbox"),
        CUSTOM_CODE("custom-code"),
        DATE("date"),
        DATE_TIME("datetime"),
        DROP_DOWN("dropdown"),
        HIDDEN("hidden"),
        GROUP_LIST("group-list"),
        INPUT_BOX("inputbox"),
        RADIO("radio"),
        USER_SEARCH("userSearch"),
        ORGANIZATION_SEARCH("organizationSearch")
    }

    enum class Column(val value: String) {
        CI_ICON("ciIconData"),
        CI_ID("ciId"),
        CI_NO("ciNo"),
        CI_NAME("ciName"),
        CI_DESC("ciDesc")
    }

    enum class Width(val width: String) {
        CI_ICON("20$UNIT_PX"),
        CI_ID("0$UNIT_PX"),
        CI_NO("180$UNIT_PX"),
        CI_NAME("150$UNIT_PX"),
        CI_DESC("245$UNIT_PX")
    }

    const val UNIT_PX = "px"
}
