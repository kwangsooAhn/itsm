/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import java.io.Serializable

data class FieldReturnDto(
    val fields: List<FieldDataDto> = mutableListOf(),
    val data: List<Any?>? = mutableListOf()
) : Serializable
