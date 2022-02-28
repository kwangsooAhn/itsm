/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.itsm.dashboard.entity.DashboardTemplateEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class DashboardTemplateRepositoryImpl : QuerydslRepositorySupport(DashboardTemplateEntity::class.java),
    DashboardTemplateRepositoryCustom {

    override fun countByOrganizationRunningDocument(document: WfDocumentEntity, organization: OrganizationEntity): Long {
        val instance = QWfInstanceEntity.wfInstanceEntity
        val user: QAliceUserEntity = QAliceUserEntity.aliceUserEntity
        return from(instance)
            .innerJoin(user).on(instance.instanceCreateUser.eq(user))
            .fetchJoin()
            .where(
                instance.document.eq(document)
                    .and(instance.instanceStatus.eq(WfInstanceConstants.Status.RUNNING.code))
                    .and(user.department.eq(organization.organizationId))
            )
            .fetchCount()
    }

    override fun countByUserRunningDocument(document: WfDocumentEntity, userKey: String): Long {
        val instance = QWfInstanceEntity.wfInstanceEntity
        return from(instance)
            .where(
                instance.document.eq(document)
                    .and(instance.instanceStatus.eq(WfInstanceConstants.Status.RUNNING.code))
                    .and(instance.instanceCreateUser.userKey.eq(userKey))
            )
            .fetchCount()
    }

    override fun organizationRunningDocument(
        documentIds: Set<String>,
        organizationId: String
    ): List<WfInstanceEntity> {
        val instance = QWfInstanceEntity.wfInstanceEntity
        val user = QAliceUserEntity.aliceUserEntity
        val query = from(instance)
            .innerJoin(instance.document).fetchJoin()
            .where(
                instance.document.documentId.`in`(documentIds)
                    .and(instance.instanceStatus.eq(WfInstanceConstants.Status.RUNNING.code))
            )
        if (organizationId.isNotEmpty()) {
            query.where(
                instance.instanceCreateUser.`in`(
                    JPAExpressions.select(user)
                        .from(user)
                        .where(user.department.eq(organizationId))
                )
            )
        }
        return query.fetch()
    }
}
