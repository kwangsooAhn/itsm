/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.QWfComponentDataEntity
import co.brainz.workflow.component.entity.QWfComponentEntity
import co.brainz.workflow.component.entity.WfComponentDataEntity
import co.brainz.workflow.provider.dto.RestTemplateFormComponentDataDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfComponentDataRepositoryImpl : QuerydslRepositorySupport(WfComponentDataEntity::class.java),
    WfComponentDataRepositoryCustom {
    override fun findComponentTypeAndAttributeId(
        componentType: String?,
        attributeId: String?
    ): List<RestTemplateFormComponentDataDto> {
        val component = QWfComponentEntity.wfComponentEntity
        val componentData = QWfComponentDataEntity.wfComponentDataEntity
        val query = from(componentData)
            .select(
                Projections.constructor(
                    RestTemplateFormComponentDataDto::class.java,
                    componentData.componentId,
                    componentData.attributeId,
                    componentData.attributeValue
                )
            )
            .join(component).on(componentData.componentId.eq(component.componentId)).fetchJoin()
        if (componentType != null) {
            query.where(component.componentType.eq(componentType))
        }
        if (attributeId != null) {
            query.where(componentData.attributeId.eq(attributeId))
        }
        return query.fetch()
    }
}
