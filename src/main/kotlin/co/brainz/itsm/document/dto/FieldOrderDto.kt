/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import co.brainz.framework.querydsl.QuerydslConstants
import java.io.Serializable

data class FieldOrderDto(
    var field: String = "",
    var order: String = QuerydslConstants.OrderSpecifier.ASC.code
) : Serializable
