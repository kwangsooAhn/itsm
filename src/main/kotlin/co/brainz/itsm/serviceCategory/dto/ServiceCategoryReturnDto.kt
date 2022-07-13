/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.serviceCategory.dto

import java.io.Serializable

data class ServiceCategoryReturnDto(
    val data: List<ServiceCategoryDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
