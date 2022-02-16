/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.querydsl

object QuerydslConstants {

    enum class OrderSpecifier(val code: String) {
        ASC("asc"),
        DESC("desc")
    }

    enum class OrderColumn(val code: String) {
        CREATE_USER_NAME("createUserName"),
        CREATE_DT("createDt"),
        DOCUMENT_GROUP("documentGroup"),
        ASSIGNEE_USER_NAME("assigneeUserName"),
        ELEMENT_NAME("elementName")
    }
}
