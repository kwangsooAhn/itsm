/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIClassAlarmDto(
    val attributeId: String,
    val condition: CIClassAlarmConditionDto,
    val targetAttributeId: String
) : Serializable
