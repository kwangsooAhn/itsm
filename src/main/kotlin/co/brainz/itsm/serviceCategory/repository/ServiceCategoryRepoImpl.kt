/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.serviceCategory.repository

import co.brainz.itsm.serviceCategory.entity.QServiceCategoryEntity
import co.brainz.itsm.serviceCategory.entity.ServiceCategoryEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ServiceCategoryRepoImpl : QuerydslRepositorySupport(ServiceCategoryEntity::class.java), ServiceCategoryRepoCustom {

    override fun findServiceCategorySearchList(searchValue: String): List<ServiceCategoryEntity> {
        val service = QServiceCategoryEntity.serviceCategoryEntity

        val query = from(service)
        if (searchValue.isNotEmpty()) {
            query.where(
                super.likeIgnoreCase(service.serviceCode, searchValue)
                    ?.or(super.likeIgnoreCase(service.serviceName, searchValue))
            )
        }
        query.orderBy(service.level.asc(), service.seqNum.asc())
        return query.fetch()
    }
}
