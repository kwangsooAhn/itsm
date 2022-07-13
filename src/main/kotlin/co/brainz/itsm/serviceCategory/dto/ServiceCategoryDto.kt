/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.serviceCategory.dto

import java.io.Serializable
import java.time.LocalDate

data class ServiceCategoryDto(
    val serviceCode: String = "",
    val pServiceCode: String? = null,
    val serviceName: String = "",
    val serviceDesc: String? = null,
    val availabilityGoal: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val editable: Boolean? = true,
    val useYn: Boolean? = true,
    val level: Int? = null,
    val seqNum: Int? = null
) : Serializable
