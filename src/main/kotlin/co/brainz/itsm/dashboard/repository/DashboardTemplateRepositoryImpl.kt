/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.itsm.dashboard.entity.DashboardTemplateEntity
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class DashboardTemplateRepositoryImpl : QuerydslRepositorySupport(DashboardTemplateEntity::class.java),
    DashboardTemplateRepositoryCustom {
    override fun findDocumentRunningByUserKey(document: String, userKey: String, status: String): Long {
        val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity

        return from(instance)
            .where(instance.document.documentId.eq(document)
                    .and(instance.instanceStatus.eq(status)
                            .and(instance.instanceCreateUser.userKey.eq(userKey))
                    )
            )
            .fetchCount()
    }
}
