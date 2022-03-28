/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.service

import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentRepository
import org.springframework.stereotype.Service

@Service
class WfComponentService(
    private val wfComponentRepository: WfComponentRepository
) {
    fun getComponent(componentId: String): WfComponentEntity {
        return wfComponentRepository.findByComponentId(componentId)
    }

    fun getComponentTypeById(componentId: String): String {
        return this.getComponent(componentId).componentType
    }
}
