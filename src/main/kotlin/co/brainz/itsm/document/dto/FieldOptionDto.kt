/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import java.io.Serializable

data class FieldOptionDto(
    var table: String = "",
    var keyField: String = "",
    val documentNo: String = "",
    val fields: MutableList<FieldDto> = mutableListOf(),
    var sort: FieldOrderDto = FieldOrderDto()
) : Serializable
