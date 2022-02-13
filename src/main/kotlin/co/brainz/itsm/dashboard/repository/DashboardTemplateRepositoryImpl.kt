/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.itsm.dashboard.entity.DashboardTemplateEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class DashboardTemplateRepositoryImpl : QuerydslRepositorySupport(DashboardTemplateEntity::class.java), DashboardTemplateRepositoryCustom {
    override fun countByDocumentIdAndStatusAndOrganization(
        document: String,
        organization: String,
        status: String
    ): Long {
        TODO("Not yet implemented")
    }
}
