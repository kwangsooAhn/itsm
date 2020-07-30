package co.brainz.workflow.tag.repository

import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
import co.brainz.workflow.tag.entity.QWfTagEntity
import co.brainz.workflow.tag.entity.QWfTagMapEntity
import co.brainz.workflow.tag.entity.WfTagMapEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfTagRepositoryImpl : QuerydslRepositorySupport(WfTagMapEntity::class.java),
    WfTagRepositoryCustom {

    val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
    val tagMap: QWfTagMapEntity = QWfTagMapEntity.wfTagMapEntity
    val tag: QWfTagEntity = QWfTagEntity.wfTagEntity

    override fun findByInstanceId(instanceId: String): List<RestTemplateTagViewDto> {
        return from(tagMap)
            .select(
                Projections.constructor(
                    RestTemplateTagViewDto::class.java,
                    tag.tagId.`as`("id"),
                    tag.tagContent.`as`("value")
                )
            )
            .innerJoin(tag).on(tagMap.tagId.eq(tag.tagId))
            .fetchJoin()
            .leftJoin(instance).on(tagMap.instance.eq(instance))
            .where(tagMap.instance.instanceId.eq(instanceId))
            .fetch()
    }
}
