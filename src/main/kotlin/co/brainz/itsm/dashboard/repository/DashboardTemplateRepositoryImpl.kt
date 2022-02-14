/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.dashboard.entity.DashboardTemplateEntity
import co.brainz.workflow.instance.dto.WfInstanceListInstanceDto
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class DashboardTemplateRepositoryImpl : QuerydslRepositorySupport(DashboardTemplateEntity::class.java), DashboardTemplateRepositoryCustom {
    override fun countByDocumentIdAndOrganizationIdAndStatus(document: String, organizationId: String, status: String): Long {
        val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
        val user : QAliceUserEntity = QAliceUserEntity.aliceUserEntity
        return from(instance)
            .select(
                Projections.constructor(
                    WfInstanceListInstanceDto::class.java,
                    instance.instanceId,
                    instance.instanceStatus,
                    instance.instanceStartDt,
                    instance.instanceEndDt,
                    instance.instanceCreateUser,
                    instance.pTokenId,
                    instance.document,
                    instance.documentNo
                )
            )
            .join(user).on(instance.instanceCreateUser.userKey.eq(user.userKey))
            .fetchJoin()
            .where(instance.instanceStatus.eq(status).and(instance.document.documentId.eq(document)).and(user.department.eq(organizationId)))
            .fetchCount()
    }

}
