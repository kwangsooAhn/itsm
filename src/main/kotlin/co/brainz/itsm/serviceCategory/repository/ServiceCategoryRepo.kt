/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.serviceCategory.repository

import co.brainz.itsm.serviceCategory.entity.ServiceCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ServiceCategoryRepo : JpaRepository<ServiceCategoryEntity, String>,
    JpaSpecificationExecutor<ServiceCategoryEntity>,
    ServiceCategoryRepoCustom {

    fun existsByServiceName(serviceName: String): Boolean

    fun findByServiceCode(serviceCode: String): ServiceCategoryEntity
}
