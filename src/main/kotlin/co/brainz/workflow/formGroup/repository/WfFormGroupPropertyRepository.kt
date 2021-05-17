/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.formGroup.repository

import co.brainz.workflow.formGroup.entity.WfFormGroupPropertyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfFormGroupPropertyRepository : JpaRepository<WfFormGroupPropertyEntity, String> {

    @Query("select g from WfFormGroupPropertyEntity g where g.formGroupId = :formGroupId")
    fun findByFormGroupId(formGroupId: String): List<WfFormGroupPropertyEntity>
}
