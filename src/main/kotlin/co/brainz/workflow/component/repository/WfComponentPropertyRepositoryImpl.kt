/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.QWfComponentEntity
import co.brainz.workflow.component.entity.QWfComponentPropertyEntity
import co.brainz.workflow.component.entity.WfComponentPropertyEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class WfComponentPropertyRepositoryImpl : QuerydslRepositorySupport(WfComponentPropertyEntity::class.java),
    WfComponentPropertyRepositoryCustom {

    override fun findComponentTypeAndProperty(compoType: String, compoProperty: String): List<String> {
        val component = QWfComponentEntity.wfComponentEntity
        val componentProperty = QWfComponentPropertyEntity.wfComponentPropertyEntity
        val query = from(componentProperty)
            .select(componentProperty.propertyOptions)
            .join(component).on(componentProperty.componentId.eq(component.componentId)).fetchJoin()
            .where(component.componentType.eq(compoType))
            .where(componentProperty.propertyType.eq(compoProperty))

        return query.fetch()
    }
}
