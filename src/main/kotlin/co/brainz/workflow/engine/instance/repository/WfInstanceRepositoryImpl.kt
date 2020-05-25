package co.brainz.workflow.engine.instance.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.token.entity.QWfTokenEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfInstanceRepositoryImpl : QuerydslRepositorySupport(WfInstanceEntity::class.java), WfInstanceRepositoryCustom {

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
