/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.itsm.dashboard.entity.DashboardTemplateEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import co.brainz.itsm.instance.dto.InstanceTemplateDto
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import com.querydsl.core.types.Projections

@Repository
class DashboardTemplateRepositoryImpl : QuerydslRepositorySupport(DashboardTemplateEntity::class.java), DashboardTemplateRepositoryCustom {
    override fun findByDocumentIdAndUserKeyAndStatus(document: String, userKey: String, status: String): Long {
        val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity

        return from(instance)
            .where(instance.document.documentId.eq(document)
                .and(instance.instanceStatus.eq(status)
                    .and(instance.instanceCreateUser.userKey.eq(userKey))))
            .fetchCount()
    }
}
