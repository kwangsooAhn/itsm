/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.QWfComponentEntity
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.formGroup.entity.QWfFormGroupEntity
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class WfComponentRepositoryImpl : QuerydslRepositorySupport(WfComponentEntity::class.java),
    WfComponentRepositoryCustom {

    override fun findByFormIds(formIds: Set<String>): List<WfComponentEntity> {
        val component = QWfComponentEntity.wfComponentEntity
        val formGroup = QWfFormGroupEntity.wfFormGroupEntity
        return from(component)
            .innerJoin(component.formRow).fetchJoin()
            .where(
                component.formRow.formGroup.`in`(
                    JPAExpressions
                        .select(formGroup)
                        .from(formGroup)
                        .where(formGroup.eq(component.formRow.formGroup))
                )
            )
            .where(component.form.formId.`in`(formIds))
            .fetch()
    }

    override fun findByComponentIdsAndComponentType(componentIds: Set<String>, componentType: String): List<WfComponentEntity> {
        val component = QWfComponentEntity.wfComponentEntity
        return from(component)
            .where(component.componentId.`in`(componentIds))
            .where(component.componentType.eq(componentType))
            .fetch()
    }
}
