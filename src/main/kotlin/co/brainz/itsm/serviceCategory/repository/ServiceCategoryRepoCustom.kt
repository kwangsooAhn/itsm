/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.serviceCategory.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.serviceCategory.dto.ServiceCategoryDto
import co.brainz.itsm.serviceCategory.entity.ServiceCategoryEntity

interface ServiceCategoryRepoCustom : AliceRepositoryCustom {

    fun findServiceCategorySearchList(searchValue: String): List<ServiceCategoryEntity>

    fun findService(serviceCode: String): ServiceCategoryDto
}
