/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIClassAlarmConditionDto(
    val period: Long,
    val unit: String,
    val option: String
) : Serializable
