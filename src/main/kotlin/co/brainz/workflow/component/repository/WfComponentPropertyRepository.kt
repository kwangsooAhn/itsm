/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.WfComponentPropertyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfComponentPropertyRepository : JpaRepository<WfComponentPropertyEntity, String> {

    @Query("select g from WfComponentPropertyEntity g where g.componentId = :componentId")
    fun findByComponentId(componentId: String): List<WfComponentPropertyEntity>
}
