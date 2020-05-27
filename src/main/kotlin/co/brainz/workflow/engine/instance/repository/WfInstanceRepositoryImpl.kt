package co.brainz.workflow.engine.instance.repository

import co.brainz.workflow.engine.document.entity.QWfDocumentEntity
import co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.engine.instance.entity.QWfInstanceEntity
import co.brainz.workflow.engine.token.entity.QWfTokenEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class WfInstanceRepositoryImpl : QuerydslRepositorySupport(WfInstanceListViewDto::class.java),
    WfInstanceRepositoryCustom {
    override fun findTodoInstances(status: String): List<WfInstanceListViewDto> {
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

}
