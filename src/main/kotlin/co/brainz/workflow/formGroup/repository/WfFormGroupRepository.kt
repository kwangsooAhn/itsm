/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.formGroup.repository

import co.brainz.workflow.formGroup.entity.WfFormGroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfFormGroupRepository : JpaRepository<WfFormGroupEntity, String> {

    @Query("select g from WfFormGroupEntity g where g.form.formId = :formId")
    fun findByFormId(formId: String): List<WfFormGroupEntity>
}
