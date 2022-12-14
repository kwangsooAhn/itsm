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
}
