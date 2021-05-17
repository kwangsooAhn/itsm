/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.formRow.repository

import co.brainz.workflow.formRow.entity.WfFormRowEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WfFormRowRepository : JpaRepository<WfFormRowEntity, String> {

    @Query("select r from WfFormRowEntity r where r.formGroup.formGroupId = :groupId")
    fun findByGroupId(groupId: String): List<WfFormRowEntity>
}
