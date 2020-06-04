package co.brainz.workflow.engine.tag.repository

import co.brainz.workflow.engine.instance.entity.QWfInstanceEntity
import co.brainz.workflow.engine.tag.entity.QWfTagDataEntity
import co.brainz.workflow.engine.tag.entity.QWfTagEntity
import co.brainz.workflow.engine.tag.entity.WfTagEntity
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfTagRepositoryImpl : QuerydslRepositorySupport(WfTagEntity::class.java),
	WfTagRepositoryCustom {

    val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
    val tag: QWfTagEntity = QWfTagEntity.wfTagEntity
    val tagData: QWfTagDataEntity = QWfTagDataEntity.wfTagDataEntity

	override fun findByInstanceId(instanceId: String): List<RestTemplateTagViewDto> {
        return from(tag)
            .select(
                Projections.constructor(
                    RestTemplateTagViewDto::class.java,
                    tagData.tagId.`as`("id"),
					tagData.tagContent.`as`("value")
                )
            )
			.innerJoin(tagData).on(tag.tagId.eq(tagData.tagId))
			.fetchJoin()
            .leftJoin(instance).on(tag.instance.eq(instance))
            .orderBy(tag.tagId.desc())
            .where(tag.instance.instanceId.eq(instanceId))
            .fetch()
	}
}
