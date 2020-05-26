package co.brainz.workflow.engine.instance.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.workflow.engine.document.entity.QWfDocumentEntity
import co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.engine.instance.entity.QWfInstanceEntity
import co.brainz.workflow.engine.token.entity.QWfTokenEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

class WfInstanceRepositoryImpl : QuerydslRepositorySupport(WfInstanceEntity::class.java),
    WfInstanceRepositoryCustom {
    override fun findInstances(status: String): List<WfInstanceListViewDto> {
        val instance = QWfInstanceEntity.wfInstanceEntity
        val token = QWfTokenEntity.wfTokenEntity
        val document = QWfDocumentEntity.wfDocumentEntity
        return from(token)
            .select(
                Projections.constructor(
                    WfInstanceListViewDto::class.java,
                    token,
                    document,
                    instance
                )
            )
            .innerJoin(instance).on(token.instance.eq(instance))
            .fetchJoin()
            .innerJoin(document).on(instance.document.eq(document))
            .fetchJoin()
            .where(instance.instanceStatus.eq(status).and(token.tokenStatus.eq(status)))
            .orderBy(instance.instanceStartDt.desc())
            .fetch()
    }

    override fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto> {
        val token = QWfTokenEntity.wfTokenEntity
        val user = QAliceUserEntity.aliceUserEntity
        return from(token)
            .select(
                Projections.constructor(
                    RestTemplateInstanceHistoryDto::class.java,
                    token.tokenStartDt,
                    token.tokenEndDt,
                    token.element.elementName,
                    token.element.elementType,
                    token.assigneeId,
                    user.userName
                )
            )
            .innerJoin(token.instance)
            .innerJoin(token.element)
            .leftJoin(user).on(token.assigneeId.eq(user.userKey))
            .where(token.instance.instanceId.eq(instanceId))
            .orderBy(token.tokenStartDt.asc())
            .fetch()
    }
}
